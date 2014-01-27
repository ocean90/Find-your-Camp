package de.fhkoeln.gm.findyourcamp.app.gson;

import java.util.HashMap;

final public class JsonDatatypes {
	private int min_price = 0;
	private int max_price = 0;
	private int group_size = 0;
	private int action = 0;
	private HashMap<String, Boolean> features = null;
	private int rental_property_remote_id = 0;
	private int rental_property_local_id = 0;
	private int user_id = 0;
	private int found_items = 0;
	private int host_user_id = 0;
	private int rent_user_id = 0;

	/**
	 * @return the min_price
	 */
	public int getMinPrice() {
		return min_price;
	}

	/**
	 * @return the max_price
	 */
	public int getMaxPrice() {
		return max_price;
	}

	/**
	 * @return the group_size
	 */
	public int getGroupSize() {
		return group_size;
	}

	/**
	 * @return the action
	 */
	public int getAction() {
		return action;
	}

	/**
	 * @return the features
	 */
	public HashMap<String, Boolean> getFeatures() {
		return features;
	}

	/**
	 * @return the rental_property_remote_id
	 */
	public int getRentalPropertyRemoteId() {
		return rental_property_remote_id;
	}

	/**
	 * @return the rental_property_local_id
	 */
	public int getRentalPropertyLocalId() {
		return rental_property_local_id;
	}

	/**
	 * @return the user_id
	 */
	public int getUserId() {
		return user_id;
	}

	/**
	 * @return the found_items
	 */
	public int getFoundItems() {
		return found_items;
	}

	/**
	 * @return the host_user_id
	 */
	public int getHostUserId() {
		return host_user_id;
	}

	/**
	 * @return the host_user_id
	 */
	public int getRentUserId() {
		return rent_user_id;
	}

}
