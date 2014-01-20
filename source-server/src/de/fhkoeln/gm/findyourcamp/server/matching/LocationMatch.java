package de.fhkoeln.gm.findyourcamp.server.matching;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.fhkoeln.gm.findyourcamp.server.db.DbConnection;
import de.fhkoeln.gm.findyourcamp.server.db.DevicesTable;
import de.fhkoeln.gm.findyourcamp.server.db.LocationsTable;
import de.fhkoeln.gm.findyourcamp.server.db.RentalPropertiesTable;

/**
 * Matching des Ortes der Suchanfrage mit den Orten zu registrierten
 * Grundstuecken
 *
 */
public class LocationMatch {

	public static List<String> getMatches(String location) {
		DbConnection dbConnection = DbConnection.getInstance();

		List<String> registrationIds = new ArrayList<String>();

		try {
			Statement statement = dbConnection.createStatement();
			// Zugehörige Location ID holen.
			ResultSet locationResultSet = statement.executeQuery("SELECT * FROM " + LocationsTable.TABLE_NAME
					+ " WHERE " + LocationsTable.COLUMN_NAME_LOCATION_NAME
					+ "='" + location + "'");

			if (!locationResultSet.next()) {
				return registrationIds;
			}
			
			int locationId = locationResultSet.getInt(1);

			// Zugehörige Mietobjekte und deren Vermieter holen.
			ResultSet RentalPropertiesResultSet = statement.executeQuery("SELECT * FROM " + RentalPropertiesTable.TABLE_NAME
					+ " WHERE " + RentalPropertiesTable.COLUMN_NAME_LOCATION_ID
					+ "=" + locationId );

			StringBuilder userIds = new StringBuilder();
			while (RentalPropertiesResultSet.next()) {
				userIds.append(RentalPropertiesResultSet.getInt(3) + ",");
			}

			// Zugehörige Devices und deren Registierungs-ID holen.
			String sql = "SELECT * FROM " + DevicesTable.TABLE_NAME
					+ " WHERE " + DevicesTable.COLUMN_NAME_DEVICE_ID
					+ " IN (" + userIds.toString().replaceAll(",$", "") + ");";
			ResultSet DevicesResultSet = statement.executeQuery(sql);

			while (DevicesResultSet.next()) {
				registrationIds.add(DevicesResultSet.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return registrationIds;
	}

}
