package de.fhkoeln.gm.findyourcamp.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Datenbanktabelle zum Mietauftrag
 *
 */
public class RentalPropertiesTable extends Table {

	public static final String TABLE_NAME = "rental_properties";

	// Spalten zur Zuordnung
	public static final String COLUMN_NAME_RENTAL_PROPERTY_ID = "rental_property_id";
	public static final String COLUMN_NAME_RENTAL_PROPERTY_LOCATION = "location";
	public static final String COLUMN_NAME_RENTAL_PROPERTY_REMOTE_ID = "rental_property_remote_id";
	public static final String COLUMN_NAME_RENTAL_PROPERTY_GROUP_SIZE = "group_size";
	public static final String COLUMN_NAME_RENTAL_PROPERTY_MIN_PRICE = "min_price";
	public static final String COLUMN_NAME_RENTAL_PROPERTY_MAX_PRICE = "max_price";

	// Features
	public static final String COLUMN_NAME_FEATURE_TOILETS_AVAILABLE_KEY = "toilets_available";
	public static final String COLUMN_NAME_FEATURE_BBQ_ALLOWED_KEY = "bbq_allowed";
	public static final String COLUMN_NAME_FEATURE_INTERNET_AVAILABLE_KEY = "internet_available";
	public static final String COLUMN_NAME_FEATURE_CHILDREN_ALLOWED_KEY = "children_allowed";
	public static final String COLUMN_NAME_FEATURE_PUBLIC_TRANSPORT_NEARBY_KEY = "public_transport_nearby";
	public static final String COLUMN_NAME_FEATURE_SHOWER_BATH_AVAILABLE_KEY = "shower_bath_available";
	public static final String COLUMN_NAME_FEATURE_WATER_PROVIDED_KEY = "water_provided";
	public static final String COLUMN_NAME_FEATURE_TENTS_PROVIDED_KEY = "tents_provided";
	public static final String COLUMN_NAME_FEATURE_POWER_PROVIDED_KEY = "power_provided";
	public static final String COLUMN_NAME_FEATURE_DOGS_ALLOWED_KEY = "dogs_allowed";
	public static final String COLUMN_NAME_FEATURE_CARAVAN_PARKING_POSSIBLE_KEY = "caravan_parking_possible";
	public static final String COLUMN_NAME_FEATURE_CAMPFIRE_ALLOWED_KEY = "campfire_allowed";

	// Neue Tabelle erstellen
	private static final String CREATE_TABLE =
			"CREATE TABLE rental_properties (" +
				COLUMN_NAME_RENTAL_PROPERTY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				COLUMN_NAME_RENTAL_PROPERTY_LOCATION + " TEXT NOT NULL, " +
				COLUMN_NAME_RENTAL_PROPERTY_GROUP_SIZE + " INTEGER NOT NULL DEFAULT 0, " +
				COLUMN_NAME_RENTAL_PROPERTY_MIN_PRICE + " INTEGER NOT NULL DEFAULT 0, " +
				COLUMN_NAME_RENTAL_PROPERTY_MAX_PRICE + " INTEGER NOT NULL DEFAULT 0, " +
				COLUMN_NAME_RENTAL_PROPERTY_REMOTE_ID + " INTEGER DEFAULT 0, " +
				COLUMN_NAME_FEATURE_TOILETS_AVAILABLE_KEY + " INTEGER NOT NULL DEFAULT 0, " +
				COLUMN_NAME_FEATURE_BBQ_ALLOWED_KEY + " INTEGER NOT NULL DEFAULT 0, " +
				COLUMN_NAME_FEATURE_INTERNET_AVAILABLE_KEY + " INTEGER NOT NULL DEFAULT 0, " +
				COLUMN_NAME_FEATURE_CHILDREN_ALLOWED_KEY + " INTEGER NOT NULL DEFAULT 0, " +
				COLUMN_NAME_FEATURE_PUBLIC_TRANSPORT_NEARBY_KEY + " INTEGER NOT NULL DEFAULT 0, " +
				COLUMN_NAME_FEATURE_SHOWER_BATH_AVAILABLE_KEY + " INTEGER NOT NULL DEFAULT 0, " +
				COLUMN_NAME_FEATURE_WATER_PROVIDED_KEY + " INTEGER NOT NULL DEFAULT 0, " +
				COLUMN_NAME_FEATURE_TENTS_PROVIDED_KEY + " INTEGER NOT NULL DEFAULT 0, " +
				COLUMN_NAME_FEATURE_POWER_PROVIDED_KEY + " INTEGER NOT NULL DEFAULT 0, " +
				COLUMN_NAME_FEATURE_DOGS_ALLOWED_KEY + " INTEGER NOT NULL DEFAULT 0, " +
				COLUMN_NAME_FEATURE_CARAVAN_PARKING_POSSIBLE_KEY + " INTEGER NOT NULL DEFAULT 0, " +
				COLUMN_NAME_FEATURE_CAMPFIRE_ALLOWED_KEY + " INTEGER NOT NULL DEFAULT 0" +
			")";

	// Vorhandene Tabelle loeschen
	private static final String DROP_TABLE =
			"DROP TABLE IF EXISTS rental_properties";

	public RentalPropertiesTable(Context context) {
		super(context);
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
