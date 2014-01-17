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
 * Sample Smack implementation of a client for GCM Cloud Connection Server.
 *
 * <p>
 * For illustration purposes only.
 */
public class Main {

	public static final String GCM_SERVER = "gcm.googleapis.com";
	public static final int GCM_PORT = 5235;

	public static final String GCM_ELEMENT_NAME = "gcm";
	public static final String GCM_NAMESPACE = "google:mobile:data";

	public static final String CSS_USERNAME = "474445016109";
	public static final String CSS_PASSWORD = "AIzaSyCCkoUqGUd-O3RI3cf3ZZZme0dIOri5Mf0";

	public DbConnection dbConnection;
	public GcmXmppConnection gmcConnection;

	public Main() {
		// Add GcmPacketExtension
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

		System.out.println("Datenbanverbindung wird aufgebaut...");
		dbConnection = DbConnection.getInstance();
		if ( ! dbConnection.connect() ) {
			exit(dbConnection.errorMessage);
		}
		System.out.println("Datenbanverbindung erfolgreich aufgebaut.");

		System.out.println("App-Check...");
		checkAppStatus();
		System.out.println("App-Check beendet.");

		System.out.println("Verbindung zu CCS wird aufgebaut...");
		gmcConnection = GcmXmppConnection.getInstance();
		if ( ! gmcConnection.connect(GcmConfig.CSS_USERNAME, GcmConfig.CSS_PASSWORD) ) {
			exit( gmcConnection.errorMessage );
		}
		System.out.println("CCS Verbindung erfolgreich aufgebaut.");

		System.out.println("Die Anwendung läuft nun...");
	}

	/**
	 * Prüft, ob eine beliebige Tabelle existiert.
	 */
	private void checkAppStatus() {
		try {
			Statement statement = dbConnection.createStatement();
			ResultSet tablesResult = statement.executeQuery("SHOW TABLES LIKE '" + UsersTable.TABLE_NAME + "'");
			boolean tableExists = tablesResult.first();
			statement.close();

			if (tableExists) {
				// Tabelle existiert, nichts weiter zu tun.
				return;
			}

			setUpDatebaseTables();
		} catch (SQLException e) {
			e.printStackTrace();
			Logger.err(e.getMessage());
			return;
		}
	}

	private void setUpDatebaseTables() {
		System.out.println("Installation begonnen...");

		try {
			Statement statement = dbConnection.createStatement();
			statement.execute(UsersTable.TABLE_CREATE);
			statement.execute(DevicesTable.TABLE_CREATE);
			statement.execute(RentalPropertiesTable.TABLE_CREATE);
			statement.execute(LocationsTable.TABLE_CREATE);
			statement.close();
			System.out.println("Installation abgeschlossen...");
		} catch (SQLException e) {
			Logger.err(e.getMessage());
		}
	}

	public static void main(String[] args) {
		System.out.println("---------------------------------------------");
		System.out.println("Enter drücken, um die Anwendung zu schließen.");
		System.out.println("---------------------------------------------");

		new Main();

		try {
			System.in.read();
		} catch (Exception e) {
		} finally {
			exit(null);
		}
	}

	private static void exit(String message) {
		if (null == message) {
			System.err.println("Anwendung wurde beendet.");
			System.exit(0);
		} else {
			System.err.println("Anwendung auf Grund eines Fehlers beendet: " + message);
			System.exit(1);
		}
	}
}
