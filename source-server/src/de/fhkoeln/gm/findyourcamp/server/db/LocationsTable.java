package de.fhkoeln.gm.findyourcamp.server.db;

/**
 * Formaler Aufbau der Ortstabelle.
 * Festlegen geeigneter Konstanten.
 */
public class LocationsTable {

	public static final String TABLE_NAME = "locations";
	
	// Spalten zur Zuordnung
	public static final String COLUMN_NAME_LOCATION_ID = "location_id";
	public static final String COLUMN_NAME_LOCATION_NAME = "location_name";

	// Konstante zum Aufbau der gesamten Tabelle mit Spalten
	public static final String TABLE_CREATE =
			"CREATE TABLE " + TABLE_NAME + " (" +
				COLUMN_NAME_LOCATION_ID + " bigint(20) unsigned NOT NULL AUTO_INCREMENT," +
				COLUMN_NAME_LOCATION_NAME + " text NOT NULL," +
				"PRIMARY KEY(" + COLUMN_NAME_LOCATION_ID + ")" +
			") DEFAULT CHARACTER SET utf8;";

	// Loeschen der Tabelle
	public static final String TABLE_DROP =
			"DROP TABLE IF EXISTS " + TABLE_NAME;

	// TODO?
	public LocationsTable() {
	}

}
