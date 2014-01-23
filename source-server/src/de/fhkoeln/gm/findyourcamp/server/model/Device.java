package de.fhkoeln.gm.findyourcamp.server.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.fhkoeln.gm.findyourcamp.server.db.DbConnection;
import de.fhkoeln.gm.findyourcamp.server.db.DevicesTable;

/**
 * In der Datenbank wird die Zuordnung der deviceId mit der userID erstellt.
 * 
 */
public class Device {

	public Device() {

	}

	/**
	 * Legt einen neuen Eintrag für ein Device an.
	 * 
	 * @param userName
	 * @param userEmail
	 */
	public static int assignDeviceToUser( String regId, int userId ) {
		DbConnection dbConnection = DbConnection.getInstance();

		try {
			Statement statement = dbConnection.createStatement();
			// Zuordnung UserID und DeviceID wird in Tabelle angelegt
			statement.executeUpdate( "INSERT INTO " + DevicesTable.TABLE_NAME + " (" + DevicesTable.COLUMN_NAME_REG_ID
				+ "," + DevicesTable.COLUMN_NAME_USER_ID + ") " + "VALUES ( '" + regId + "', '" + userId + "');",
				Statement.RETURN_GENERATED_KEYS );
			ResultSet keys = statement.getGeneratedKeys();
			keys.next();
			int deviceId = keys.getInt( 1 );
			statement.close();

			return deviceId; // Interne ID des Devices zurückgeben.
		} catch ( SQLException e ) {
			e.printStackTrace();
			return 0;
		}
	}
}
