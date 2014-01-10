package de.fhkoeln.gm.findyourcamp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class UsersTable extends Table {


	private static final String TABLE_NAME = "users";

	private static final String TABLE_CREATE =
			"CREATE TABLE " + TABLE_NAME + " (" +
				"_id integer primary key autoincrement," +
				"name text NOT NULL" +
			");";

	private static final String TABLE_DROP =
			"DROP TABLE IF EXISTS " + TABLE_NAME;

	public UsersTable(Context context) {
		super(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(TABLE_DROP);
		onCreate(db);
	}

}
