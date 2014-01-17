package de.fhkoeln.gm.findyourcamp.server.db;


public class UsersTable {

	public static final String TABLE_NAME = "users";
	public static final String COLUMN_NAME_USER_ID = "user_id";
	public static final String COLUMN_NAME_USER_NAME = "user_name";
	public static final String COLUMN_NAME_USER_EMAIL = "user_email";

	public static final String TABLE_CREATE =
			"CREATE TABLE " + TABLE_NAME + " (" +
				COLUMN_NAME_USER_ID + " bigint(20) unsigned NOT NULL AUTO_INCREMENT," +
				COLUMN_NAME_USER_NAME + " varchar(60) NOT NULL DEFAULT ''," +
				COLUMN_NAME_USER_EMAIL + " varchar(254) NOT NULL DEFAULT ''," +
				"PRIMARY KEY(" + COLUMN_NAME_USER_ID + ")" +
			") DEFAULT CHARACTER SET utf8;";

	public static final String TABLE_DROP =
			"DROP TABLE IF EXISTS " + TABLE_NAME;

	public UsersTable() {
	}

}
