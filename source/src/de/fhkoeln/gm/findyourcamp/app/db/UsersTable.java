package de.fhkoeln.gm.findyourcamp.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Datenbanktabelle zum Mietauftrag
 *
 */
public class UsersTable extends Table {

	public static final String TABLE_NAME = "users";
	public static final String COLUMN_NAME_ID = "id";
	public static final String COLUMN_NAME_USER_NAME = "name";

	private static final String TABLE_CREATE =
			"CREATE TABLE " + TABLE_NAME + " (" +
				COLUMN_NAME_ID + " integer primary key autoincrement," +
				COLUMN_NAME_USER_NAME + " text NOT NULL" +
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
	
	public void insert() {
		SQLiteDatabase db = getWritableDatabase();
	}

}
