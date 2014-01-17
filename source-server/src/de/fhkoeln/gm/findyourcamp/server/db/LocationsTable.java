package de.fhkoeln.gm.findyourcamp.server.db;


public class LocationsTable {

	public static final String TABLE_NAME = "locations";
	public static final String COLUMN_NAME_LOCATION_ID = "location_id";
	public static final String COLUMN_NAME_LOCATION_NAME = "location_name";

	public static final String TABLE_CREATE =
			"CREATE TABLE " + TABLE_NAME + " (" +
				COLUMN_NAME_LOCATION_ID + " bigint(20) unsigned NOT NULL AUTO_INCREMENT," +
				COLUMN_NAME_LOCATION_NAME + " text NOT NULL," +
				"PRIMARY KEY(" + COLUMN_NAME_LOCATION_ID + ")" +
			") DEFAULT CHARACTER SET utf8;";

	public static final String TABLE_DROP =
			"DROP TABLE IF EXISTS " + TABLE_NAME;

	public LocationsTable() {
	}

}
