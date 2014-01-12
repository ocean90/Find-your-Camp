package de.fhkoeln.gm.findyourcamp.model;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import de.fhkoeln.gm.findyourcamp.utils.Logger;

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

	private Map<String,Boolean> features;

	public RentalPropertyFeatures() {
		this.features = new HashMap<String,Boolean>();

		this.setDefaultValues();
	}

	private void setDefaultValues() {
		for (Field field : RentalPropertyFeatures.class.getDeclaredFields()) {
			field.setAccessible(true);
           try {
        	   setFeature( (String)field.get(null), false);
           } catch (Exception e) {}
        }
	}

	public void setFeature(String key, Boolean value) {
		this.features.put(key, value);
	}

	public boolean getFeature(String key) {
		return this.features.get(key);
	}
}
