package de.fhkoeln.gm.findyourcamp.server.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.fhkoeln.gm.findyourcamp.server.db.DbConnection;
import de.fhkoeln.gm.findyourcamp.server.db.RentalPropertiesTable;

public class RentalProperty {

	public RentalProperty() {

	}

	/**
	 * Legt einen neuen Eintrag für ein Mietobjekt an.
	 *
	 * @param userName
	 * @param userEmail
	 */
	public static long createRentalPropertyEntry(long rentalPropertyLocalId,
			String rentalPropertyLocation, long userId) {
		DbConnection dbConnection = DbConnection.getInstance();

		int locationId = Location.getIdByName(rentalPropertyLocation);
		if (locationId == 0) {
			locationId = Location.createLocation(rentalPropertyLocation);
		}

		try {
			Statement statement = dbConnection.createStatement();
			statement.executeUpdate("INSERT INTO "
					+ RentalPropertiesTable.TABLE_NAME + " ("
					+ RentalPropertiesTable.COLUMN_NAME_LOCATION_ID + ","
					+ RentalPropertiesTable.COLUMN_NAME_USER_ID + ","
					+ RentalPropertiesTable.COLUMN_NAME_RENTAL_PROPERTY_LOCAL_ID + ") "
					+ "VALUES ( '" + locationId + "', '" + userId + "', '"
					+ rentalPropertyLocalId + "');",
					Statement.RETURN_GENERATED_KEYS);

			ResultSet keys = statement.getGeneratedKeys();
			keys.next();
			long deviceId = keys.getLong(1);
			statement.close();

			return deviceId; // Interne ID des Mietobjekts zurückgeben.
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}
}
