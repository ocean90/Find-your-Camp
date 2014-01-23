package de.fhkoeln.gm.findyourcamp.server.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.fhkoeln.gm.findyourcamp.server.db.DbConnection;
import de.fhkoeln.gm.findyourcamp.server.db.LocationsTable;
import de.fhkoeln.gm.findyourcamp.server.utils.Logger;

public class Location {

	public Location() {

	}

	public static int getIdByName( String locationName ) {
		DbConnection dbConnection = DbConnection.getInstance();

		try {
			Statement statement = dbConnection.createStatement();
			ResultSet locationResultSet = statement.executeQuery( "SELECT " + LocationsTable.COLUMN_NAME_LOCATION_ID
				+ " FROM " + LocationsTable.TABLE_NAME + " WHERE " + LocationsTable.COLUMN_NAME_LOCATION_NAME + "='"
				+ locationName + "'" );

			boolean locationFound = locationResultSet.next();
			if ( !locationFound ) {
				return 0;
			}

			int locationId = locationResultSet.getInt( 1 );
			statement.close();
			Logger.log( "Location gefunden: " + locationId );
			return locationId;
		} catch ( SQLException e ) {
			e.printStackTrace();
		}

		return 0;
	}

	public static int createLocation( String rentalPropertyLocation ) {
		DbConnection dbConnection = DbConnection.getInstance();

		try {
			Statement statement = dbConnection.createStatement();
			// Neuer Eintrag wird in Tabelle angelegt
			statement.executeUpdate( "INSERT INTO " + LocationsTable.TABLE_NAME + " ("
				+ LocationsTable.COLUMN_NAME_LOCATION_NAME + ") " + "VALUES ( '" + rentalPropertyLocation + "');",
				Statement.RETURN_GENERATED_KEYS );
			ResultSet keys = statement.getGeneratedKeys();

			// ZeilenID
			keys.next();
			int locationId = keys.getInt( 1 );
			statement.close();
			Logger.log( "Location angelegt: " + locationId );
			return locationId; // ID der Location zurückgeben
		} catch ( SQLException e ) {
			e.printStackTrace();
		}

		return 0;
	}

}
