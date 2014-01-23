package de.fhkoeln.gm.findyourcamp.server.db;

/**
 * Formaler Aufbau der Devicetabelle. Festlegen geeigneter Konstanten.
 * 
 */
public class DevicesTable {

	public static final String TABLE_NAME = "devices";

	// Spalten zur Zuordnung
	public static final String COLUMN_NAME_DEVICE_ID = "device_id";
	public static final String COLUMN_NAME_REG_ID = "reg_id";
	public static final String COLUMN_NAME_USER_ID = "user_id";

	// Konstante zum Aufbau der gesamten Tabelle mit Spalten
	public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_NAME_DEVICE_ID
		+ " bigint(20) unsigned NOT NULL AUTO_INCREMENT," + COLUMN_NAME_REG_ID + " varchar(300) NOT NULL,"
		+ COLUMN_NAME_USER_ID + " bigint(20) unsigned NOT NULL," + "PRIMARY KEY(" + COLUMN_NAME_DEVICE_ID + ")"
		+ ") DEFAULT CHARACTER SET utf8;";

	// Loeschen der Tabelle
	public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

	// TODO?
	public DevicesTable() {
	}

}
