package de.fhkoeln.gm.findyourcamp.server.db;


public class DevicesTable {

	public static final String TABLE_NAME = "devices";
	public static final String COLUMN_NAME_DEVICE_ID = "device_id";
	public static final String COLUMN_NAME_REG_ID = "reg_id";
	public static final String COLUMN_NAME_USER_ID = "user_id";

	public static final String TABLE_CREATE =
			"CREATE TABLE " + TABLE_NAME + " (" +
				COLUMN_NAME_DEVICE_ID + " bigint(20) unsigned NOT NULL AUTO_INCREMENT," +
				COLUMN_NAME_REG_ID + " varchar(300) NOT NULL," +
				COLUMN_NAME_USER_ID + " bigint(20) unsigned NOT NULL," +
				"PRIMARY KEY(" + COLUMN_NAME_DEVICE_ID + ")" +
			") DEFAULT CHARACTER SET utf8;";

	public static final String TABLE_DROP =
			"DROP TABLE IF EXISTS " + TABLE_NAME;

	public DevicesTable() {
	}

}
