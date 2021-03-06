package de.fhkoeln.gm.findyourcamp.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Klasse zum Aufbau der Verbindung zur MySQL Datenbank
 * 
 */
public class DbConnection {

	private static DbConnection instance;

	// A connection (session) with a specific database.
	// SQL statements are executed and results are returned within the context
	// of a connection.
	private Connection connection;
	public String errorMessage = "";

	private DbConnection() {

		try {
			// Datenbanktreiber wird zur Laufzeit geladen
			Class.forName( "com.mysql.jdbc.Driver" );
		} catch ( ClassNotFoundException e ) {
			// Fehler: Klasse nicht gefunden.
		}

	}

	/**
	 * Singleton. Erzeugt genau eine Instanz des Objektes für diese Klasse.
	 * 
	 * @return Instanz der Datenbankverbindung
	 */
	public static DbConnection getInstance() {
		if ( DbConnection.instance == null ) {
			DbConnection.instance = new DbConnection();
		}
		return DbConnection.instance;
	}

	/**
	 * Verbindungsaufbau zur Datenbank mit bestikkter URL und Account
	 * 
	 * @return Status zum Ergebnis des Verbindungsaufbaus
	 */
	public boolean connect() {
		try {// Driver für die Datenbankverbindung getConnection baut Verbindung
				// zur angegeben Datenbank URL auf
			connection = DriverManager.getConnection( "jdbc:mysql://" + DbConfig.HOST + ":" + DbConfig.PORT + "/"
				+ DbConfig.DATABASE_NAME, DbConfig.USERNAME, DbConfig.PASSWORD );
		} catch ( SQLException e ) {
			// Fehler: Fehlgeschlagene Datenbankverbindung.
			this.errorMessage = e.getMessage();
			return false;
		}

		return true;
	}

	/**
	 * Erstellen eines Statementobjektes zum Senden von SQL Befehlen
	 * 
	 * @return Statement
	 * @throws SQLException
	 */
	public Statement createStatement() throws SQLException {
		// Ein Statement zum ausführen der SQL Befehle wird der
		// Datenbankverbindung zugeordnet.
		return connection.createStatement();
	}

}
