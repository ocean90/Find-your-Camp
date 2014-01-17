package de.fhkoeln.gm.findyourcamp.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {

	private static DbConnection instance;

	private Connection connection;
	public String errorMessage = "";

	private DbConnection() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// Fehler: Klasse nicht gefunden.
		}

	}

	/**
	 * Singleton.
	 * Erzeugt genau eine Instanz des Objektes für diese Klasse.
	 *
	 * @return
	 */
	public static DbConnection getInstance() {
		if (DbConnection.instance == null) {
			DbConnection.instance = new DbConnection();
		}

		return DbConnection.instance;
	}

	public boolean connect() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://"
					+ DbConfig.HOST + ":" + DbConfig.PORT + "/"
					+ DbConfig.DATABASE_NAME, DbConfig.USERNAME,
					DbConfig.PASSWORD);
		} catch (SQLException e) {
			// Fehler: Fehlgeschlagene Datenbankverbindung.
			this.errorMessage = e.getMessage();
			return false;
		}

		return true;
	}

	public Statement createStatement() throws SQLException {
		return connection.createStatement();
	}

}
