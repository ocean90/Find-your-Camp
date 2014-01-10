package de.fhkoeln.gm.findyourcamp.db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class Table extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "data.db";
	private static final int DATABASE_VERSION = 1;


	public Table(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
}
