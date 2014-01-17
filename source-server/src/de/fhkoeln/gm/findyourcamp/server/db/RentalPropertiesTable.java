package de.fhkoeln.gm.findyourcamp.server.db;


public class RentalPropertiesTable {

	public static final String TABLE_NAME = "rental_properties";
	public static final String COLUMN_NAME_RENTAL_PROPERTY_ID = "rental_property_id";
	public static final String COLUMN_NAME_LOCATION_ID = "location_id";
	public static final String COLUMN_NAME_USER_ID = "user_id";

	public static final String TABLE_CREATE =
			"CREATE TABLE " + TABLE_NAME + " (" +
				COLUMN_NAME_RENTAL_PROPERTY_ID + " bigint(20) unsigned NOT NULL AUTO_INCREMENT," +
				COLUMN_NAME_LOCATION_ID + " bigint(20) unsigned NOT NULL," +
				COLUMN_NAME_USER_ID + " bigint(20) unsigned NOT NULL," +
				"PRIMARY KEY(" + COLUMN_NAME_RENTAL_PROPERTY_ID + ")" +
			") DEFAULT CHARACTER SET utf8;";

	public static final String TABLE_DROP =
			"DROP TABLE IF EXISTS " + TABLE_NAME;

	public RentalPropertiesTable() {
	}

}
