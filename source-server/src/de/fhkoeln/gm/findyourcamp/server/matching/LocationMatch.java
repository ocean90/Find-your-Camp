package de.fhkoeln.gm.findyourcamp.server.matching;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.fhkoeln.gm.findyourcamp.server.db.DbConnection;
import de.fhkoeln.gm.findyourcamp.server.db.LocationsTable;
import de.fhkoeln.gm.findyourcamp.server.db.RentalPropertiesTable;

/**
 * Matching des Ortes der Suchanfrage mit den Orten zu registrierten
 * Grundstuecken
 *
 */
public class LocationMatch {

	public static List<Integer> getMatches(String location) {
		DbConnection dbConnection = DbConnection.getInstance();

		List<Integer> userIds = new ArrayList<Integer>();

		try {
			Statement statement = dbConnection.createStatement();
			// Zugehörige ID holen.
			ResultSet locationResultSet = statement.executeQuery("SELECT * FROM " + LocationsTable.TABLE_NAME
					+ " WHERE " + LocationsTable.COLUMN_NAME_LOCATION_NAME
					+ "='" + location + "'");

			locationResultSet.next();
			int locationId = locationResultSet.getInt(1);

			// Zugehörige Mietobjekte und deren Vermieter holen.
			ResultSet RentalPropertiesResultSet = statement.executeQuery("SELECT * FROM " + RentalPropertiesTable.TABLE_NAME
					+ " WHERE " + RentalPropertiesTable.COLUMN_NAME_LOCATION_ID
					+ "=" + locationId );

			while (RentalPropertiesResultSet.next()) {
				userIds.add(RentalPropertiesResultSet.getInt(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return userIds;
	}

}
