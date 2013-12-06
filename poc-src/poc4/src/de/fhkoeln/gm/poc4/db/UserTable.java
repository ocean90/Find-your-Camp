package de.fhkoeln.gm.poc4.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserTable extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "poc4.db";
	private static final int DATABASE_VERSION = 1;

	private static final String CREATE_TABLE =
			"CREATE TABLE user (" +
			"_id integer primary key autoincrement," +
			"name text NOT NULL" +
			")";

	private static final String DROP_TABLE =
			"DROP TABLE IF EXISTS user";

	public UserTable(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE);
		onCreate(db);
	}

}
