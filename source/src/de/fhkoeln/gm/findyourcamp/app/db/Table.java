package de.fhkoeln.gm.findyourcamp.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class Table extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "data.db";
	private static final int DATABASE_VERSION = 3;

	public Table(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
}
