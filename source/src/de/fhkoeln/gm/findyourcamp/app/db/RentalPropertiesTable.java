package de.fhkoeln.gm.findyourcamp.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class RentalPropertiesTable extends Table {

	public RentalPropertiesTable(Context context) {
		super(context);
	}

	private static final String CREATE_TABLE =
			"CREATE TABLE rental_properties (" +
			"_id integer primary key autoincrement," +
			"name text NOT NULL" +
			")";

	private static final String DROP_TABLE =
			"DROP TABLE IF EXISTS rental_properties";


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
