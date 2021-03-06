package de.fhkoeln.gm.findyourcamp.app.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Model zur Ausstattung des Mietobjektes
 * 
 */
public class RentalPropertyFeatures {
	final public static String TOILETS_AVAILABLE_KEY = "toilets_available";
	final public static String BBQ_ALLOWED_KEY = "bbq_allowed";
	final public static String INTERNET_AVAILABLE_KEY = "internet_available";
	final public static String CHILDREN_ALLOWED_KEY = "children_allowed";
	final public static String PUBLIC_TRANSPORT_NEARBY_KEY = "public_transport_nearby";
	final public static String SHOWER_BATH_AVAILABLE_KEY = "shower_bath_available";
	final public static String WATER_PROVIDED_KEY = "water_provided";
	final public static String TENTS_PROVIDED_KEY = "tents_provided";
	final public static String POWER_PROVIDED_KEY = "power_provided";
	final public static String DOGS_ALLOWED_KEY = "dogs_allowed";
	final public static String CARAVAN_PARKING_POSSIBLE_KEY = "caravan_parking_possible";
	final public static String CAMPFIRE_ALLOWED_KEY = "campfire_allowed";

	private HashMap<String, Boolean> features;

	public RentalPropertyFeatures() {
		this.features = new HashMap<String, Boolean>();

		this.setDefaultValues();
	}

	// Defaultwerte setzen
	private void setDefaultValues() {
		for ( Field field : RentalPropertyFeatures.class.getDeclaredFields() ) {
			field.setAccessible( true );
			try {
				setFeature( (String) field.get( null ), false );
			} catch ( Exception e ) {
			}
		}
	}

	// Features hinzufuegen
	public void setFeature( String key, Boolean value ) {
		this.features.put( key, value );
	}

	// Features ausgeben
	public boolean getFeature( String key ) {
		return this.features.get( key );
	}

	// Alle vorhanden Features ausgeben. (Wert = true)
	public ArrayList<String> getAvailableFeatures() {
		ArrayList<String> availableFeatures = new ArrayList<String>();

		for ( Entry<String, Boolean> feature : this.features.entrySet() ) {
			if ( feature.getValue() ) {
				availableFeatures.add( feature.getKey() );
			}
		}

		return availableFeatures;
	}

	public HashMap<String, Boolean> toMap() {
		return this.features;
	}

}
