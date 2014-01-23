package de.fhkoeln.gm.findyourcamp.server.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.fhkoeln.gm.findyourcamp.server.db.DbConnection;
import de.fhkoeln.gm.findyourcamp.server.db.UsersTable;

/**
 * Anlegen eines neuen Usereintrages bei Registrierung
 * 
 */
public class User {

	public User() {

	}

	/**
	 * Legt einen neuen Eintrag für einen User an.
	 * 
	 * @param userName
	 * @param userEmail
	 */
	public static int createUser( String userName, String userEmail ) {
		DbConnection dbConnection = DbConnection.getInstance();

		try {
			Statement statement = dbConnection.createStatement();
			// Neuer Usereintrag wird in Tabelle angelegt
			statement.executeUpdate(
				"INSERT INTO " + UsersTable.TABLE_NAME + " (" + UsersTable.COLUMN_NAME_USER_NAME + ","
					+ UsersTable.COLUMN_NAME_USER_EMAIL + ") " + "VALUES ( '" + userName + "', '" + userEmail + "');",
				Statement.RETURN_GENERATED_KEYS );
			ResultSet keys = statement.getGeneratedKeys();

			// ZeilenID
			keys.next();
			int userId = keys.getInt( 1 );
			statement.close();

			return userId; // ID des Users zurückgeben.

		} catch ( SQLException e ) {
			e.printStackTrace();
		}

		return 0;
	}
}
