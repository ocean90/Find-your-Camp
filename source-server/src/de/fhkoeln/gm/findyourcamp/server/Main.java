package de.fhkoeln.gm.findyourcamp.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmlpull.v1.XmlPullParser;

import de.fhkoeln.gm.findyourcamp.server.db.DbConnection;
import de.fhkoeln.gm.findyourcamp.server.db.DevicesTable;
import de.fhkoeln.gm.findyourcamp.server.db.LocationsTable;
import de.fhkoeln.gm.findyourcamp.server.db.RentalPropertiesTable;
import de.fhkoeln.gm.findyourcamp.server.db.UsersTable;
import de.fhkoeln.gm.findyourcamp.server.gcm.GcmConfig;
import de.fhkoeln.gm.findyourcamp.server.gcm.GcmPacketExtension;
import de.fhkoeln.gm.findyourcamp.server.gcm.GcmXmppConnection;
import de.fhkoeln.gm.findyourcamp.server.utils.Logger;

/**
 * Ausfuehrung des Clients fuer GCM Cloud Connection Server.
 * Baut Verbindung zur Datenbank und GCM CSS auf und richtet bei Bedarf neue Tabellen ein.
 *
 */
public class Main {

	// Neue Datenbankverbindung
	public DbConnection dbConnection;
	
	// Neue GCMConnection
	public GcmXmppConnection gmcConnection;

	/**
	 * Methode richtet Verbindung zur Datenbank und GCM ein. 
	 * Bei Fehler Rueckgabe der Problemstelle.
	 */
	public Main() {
		// GcmPacketExtension registrieren 
		ProviderManager.getInstance().addExtensionProvider(GcmPacketExtension.GCM_ELEMENT_NAME,
				GcmPacketExtension.GCM_NAMESPACE, new PacketExtensionProvider() {

					@Override
					public PacketExtension parseExtension(XmlPullParser parser)
							throws Exception {
						String json = parser.nextText();
						GcmPacketExtension packet = new GcmPacketExtension(json);
						return packet;
					}
				});
		
		// Aufbau der Datenbankverbindung
		System.out.println("Datenbanverbindung wird aufgebaut...");
		dbConnection = DbConnection.getInstance();
		//Prueft ob Verbindung aufgebaut werden kann
		if ( ! dbConnection.connect() ) {
			// Bei Verbindungsfehler Ausgabe der errorMessage 
			exit(dbConnection.errorMessage);
		}
		System.out.println("Datenbanverbindung erfolgreich aufgebaut.");

		System.out.println("App-Check...");
		// Prueft ob bereits Tabellen in der Datenbank angelegt wurden, sonst neu anlegen
		checkAppStatus();
		System.out.println("App-Check beendet.");

		// Aufbau der GCM CCS Verbindung
		System.out.println("Verbindung zu CCS wird aufgebaut...");
		gmcConnection = GcmXmppConnection.getInstance();
		// Prueft ob Verbindung aufgebaut werden kann
		if ( ! gmcConnection.connect(GcmConfig.CSS_USERNAME, GcmConfig.CSS_PASSWORD) ) {
			// Bei Verbindungsfehler Ausgabe der errorMessage 
			exit( gmcConnection.errorMessage );
		}
		System.out.println("CCS Verbindung erfolgreich aufgebaut.");
		System.out.println("Die Anwendung läuft nun...");
	}

	/**
	 * Prüft, ob eine beliebige Tabelle existiert.
	 * Ansonsten Aufbau neuer Tabellen.
	 */
	private void checkAppStatus() {
		try {
			// Prueft ob Tabellen bereits existieren
			Statement statement = dbConnection.createStatement();
			ResultSet tablesResult = statement.executeQuery("SHOW TABLES LIKE '" + UsersTable.TABLE_NAME + "'");
			boolean tableExists = tablesResult.first();
			statement.close();

			if (tableExists) {
				// Tabelle existiert, nichts weiter zu tun.
				return;
			}
			
			// Tabellen der Datenbank werden neu eingerichtet
			setUpDatebaseTables();
		} catch (SQLException e) {
			e.printStackTrace();
			Logger.err(e.getMessage());
			return;
		}
	}

	/**
	 * Datenbanktabellen werden neu angelegt, sofern Tabelle nicht exisitert
	 */
	private void setUpDatebaseTables() {
		System.out.println("Installation begonnen...");

		try {
			// Statement zur Ausfuehrung des SQL Befehls
			Statement statement = dbConnection.createStatement();
			// Usertabelle wird angelegt
			statement.execute(UsersTable.TABLE_CREATE);
			// Devicetabelle wird angelegt
			statement.execute(DevicesTable.TABLE_CREATE);
			// Objektausstattungstabelle wurd angelegt
			statement.execute(RentalPropertiesTable.TABLE_CREATE);
			// Ortstabelle (Standort der Mietobjekte) wird angelegt
			statement.execute(LocationsTable.TABLE_CREATE);
			statement.close();
			System.out.println("Installation abgeschlossen...");
		} catch (SQLException e) {
			Logger.err(e.getMessage());
		}
	}

	/**
	 * Ausfuehrung der Serveranwendung.
	 * Status der Zwischenschritte werden über Konsolenausgabe notiert.
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("---------------------------------------------");
		System.out.println("Enter drücken, um die Anwendung zu schließen.");
		System.out.println("---------------------------------------------");
		
		// Einrichten der Verbindung
		new Main();

		
		// Benutzereingabe zum Abbruch
		try {
			System.in.read();
		} catch (Exception e) {
		} finally {
			exit(null);
		}
	}

	/**
	 * Beenden der laufenden Serveranwendung
	 * @param message
	 */
	private static void exit(String message) {
		// Benutzergesteuertes Ende der Serveranwendung
		if (null == message) {
			System.err.println("Anwendung wurde beendet.");
			System.exit(0);
		} else {
			// Ende der Serveranwendung durch Fehler
			System.err.println("Anwendung auf Grund eines Fehlers beendet: " + message);
			System.exit(1);
		}
	}
}
