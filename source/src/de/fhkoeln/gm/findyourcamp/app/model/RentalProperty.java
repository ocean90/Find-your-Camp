package de.fhkoeln.gm.findyourcamp.app.model;

import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.fhkoeln.gm.findyourcamp.app.db.RentalPropertiesTable;
import de.fhkoeln.gm.findyourcamp.app.utils.Logger;

/**
 * Modell zum Mietobjekt
 * 
 */
public class RentalProperty {

	private long id = 0;
	private long remoteId = 0;
	private RentalPropertyFeatures features = null;
	private String location = "";
	private int groupSize = 0;
	private int minPrice;
	private int maxPrice;

	public RentalProperty() {
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId( int id ) {
		this.id = id;
	}

	/**
	 * @return the remoteId
	 */
	public long getRemoteId() {
		return remoteId;
	}

	/**
	 * @param remoteId
	 *            the remoteId to set
	 */
	public void setRemoteId( int remoteId ) {
		this.remoteId = remoteId;
	}

	/**
	 * Location setzen
	 * 
	 * @param location
	 *            to set
	 */
	public void setLocation( String location ) {
		this.location = location;
	}

	/**
	 * Location ausgeben
	 * 
	 * @return location to get
	 */
	public String getLocation() {
		return this.location;
	}

	/**
	 * Ausstattungsfeatures setzen
	 * 
	 * @param features
	 */
	public void setFeatures( RentalPropertyFeatures features ) {
		this.features = features;
	}

	/**
	 * Ausstattungsfeatures ausgeben
	 * 
	 * @return features to get
	 */
	public RentalPropertyFeatures getFeatures() {
		return this.features;
	}

	/**
	 * Gruppengroeße setzen
	 * 
	 * @param groupSize
	 */
	public void setGroupSize( int groupSize ) {
		this.groupSize = groupSize;
	}

	/**
	 * Gruppengroeße ausgeben
	 * 
	 * @return gruppengroeße to get
	 */
	public int getGroupSize() {
		return this.groupSize;
	}

	/**
	 * Preispanne setzen
	 * 
	 * @param minPrice
	 * @param maxPrice
	 */
	public void setPriceRange( int minPrice, int maxPrice ) {
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
	}

	/**
	 * Preispanne ausgeben
	 */
	public int[] getPriceRange() {
		int[] priceRange = new int[2];

		priceRange[0] = minPrice;
		priceRange[1] = maxPrice;

		return priceRange;
	}

	public boolean isValid() {
		if ( this.location.isEmpty() )
			return false;

		if ( this.features == null )
			return false;

		if ( this.groupSize == 0 )
			return false;

		return true;
	}

	public String toString() {
		return this.location;
	}

	public HashMap<String, Object> toMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put( "location", location );
		map.put( "group_size", groupSize );
		map.put( "min_price", minPrice );
		map.put( "max_price", maxPrice );
		map.put( "features", features.toMap() );

		return map;
	}

	public static RentalProperty getFromId( long rental_property_id, Context appContext ) {
		RentalProperty rentalProperty = null;

		RentalPropertiesTable rentalPropertiesTable = new RentalPropertiesTable( appContext );
		SQLiteDatabase rentalPropertiesDatabase = rentalPropertiesTable.getWritableDatabase();

		String[] args = { String.valueOf( rental_property_id ) };
		Cursor cursor = rentalPropertiesDatabase.query( RentalPropertiesTable.TABLE_NAME, null,
			RentalPropertiesTable.COLUMN_NAME_RENTAL_PROPERTY_ID + "=?", args, null, null, null );

		cursor.moveToFirst();
		while ( !cursor.isAfterLast() ) {
			rentalProperty = cursorToRentalProperty( cursor );
			cursor.moveToNext();
		}
		cursor.close();

		return rentalProperty;
	}

	public static RentalProperty cursorToRentalProperty( Cursor cursor ) {
		RentalProperty rentalProperty = new RentalProperty();
		RentalPropertyFeatures rentalPropertyFeatures = new RentalPropertyFeatures();

		rentalProperty.setId( cursor.getInt( 0 ) );
		rentalProperty.setLocation( cursor.getString( 1 ) );
		rentalProperty.setGroupSize( cursor.getInt( 2 ) );
		rentalProperty.setPriceRange( cursor.getInt( 3 ), cursor.getInt( 4 ) );
		rentalProperty.setRemoteId( cursor.getInt( 5 ) );

		for ( int i = 6; i < cursor.getColumnCount(); i++ ) {
			Logger.info( "Spalte: " + cursor.getColumnName( i ) + "Wert: " + cursor.getInt( i ) + " Boolean: "
				+ ( cursor.getInt( i ) == 1 ) );
			rentalPropertyFeatures.setFeature( cursor.getColumnName( i ), ( cursor.getInt( i ) == 1 ) );
		}
		rentalProperty.setFeatures( rentalPropertyFeatures );

		return rentalProperty;
	}
}
