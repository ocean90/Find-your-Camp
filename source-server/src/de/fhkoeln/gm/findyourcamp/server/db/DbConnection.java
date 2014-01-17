package de.fhkoeln.gm.findyourcamp.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {

	private Connection connection;
	public String errorMessage = "";

	public DbConnection() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// Fehler: Klasse nicht gefunden.
		}

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
