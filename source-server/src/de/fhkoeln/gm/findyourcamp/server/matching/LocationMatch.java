package de.fhkoeln.gm.findyourcamp.server.matching;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

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

	public static HashMap<String,ArrayList<Object>> getMatches(String location) {
		DbConnection dbConnection = DbConnection.getInstance();

		ArrayList<Object> registrationIds = new ArrayList<Object>();
		ArrayList<Object> localRentalPropertyIds = new ArrayList<Object>();
		HashMap<String,ArrayList<Object>> result = new HashMap<String,ArrayList<Object>>();
		result.put("registrationIds", registrationIds);
		result.put("localRentalPropertyIds", localRentalPropertyIds);

		try {
			Statement statement = dbConnection.createStatement();
			// Zugehörige Location ID holen.
			ResultSet locationResultSet = statement.executeQuery("SELECT * FROM " + LocationsTable.TABLE_NAME
					+ " WHERE " + LocationsTable.COLUMN_NAME_LOCATION_NAME
					+ "='" + location + "'");

			if (!locationResultSet.next()) {
				return result;
			}

			int locationId = locationResultSet.getInt(1);

			// Zugehörige Mietobjekte und deren Vermieter holen.
			ResultSet RentalPropertiesResultSet = statement.executeQuery("SELECT * FROM " + RentalPropertiesTable.TABLE_NAME
					+ " WHERE " + RentalPropertiesTable.COLUMN_NAME_LOCATION_ID
					+ "=" + locationId );

			StringBuilder userIds = new StringBuilder();
			while (RentalPropertiesResultSet.next()) {
				userIds.append(RentalPropertiesResultSet.getInt(4) + ",");
				localRentalPropertyIds.add(RentalPropertiesResultSet.getInt(2));
			}

			// Zugehörige Devices und deren Registierungs-ID holen.
			String sql = "SELECT * FROM " + DevicesTable.TABLE_NAME
					+ " WHERE " + DevicesTable.COLUMN_NAME_DEVICE_ID
					+ " IN (" + userIds.toString().replaceAll(",$", "") + ");";
			ResultSet DevicesResultSet = statement.executeQuery(sql);

			while (DevicesResultSet.next()) {
				registrationIds.add(DevicesResultSet.getString(2));
			}

			result.put("registrationIds", registrationIds);
			result.put("localRentalPropertyIds", localRentalPropertyIds);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

}
