package de.fhkoeln.gm.findyourcamp.app.model;

import java.util.HashMap;

/**
 * Modell zum Mietobjekt
 *
 */
public class RentalProperty {

	private RentalPropertyFeatures features = null;
	private String location = "";
	private int groupSize = 0;
	private int minPrice;
	private int maxPrice;

	public RentalProperty() {
	}

	/**
	 * Location setzen
	 * @param location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Location ausgeben
	 * @return location to get
	 */
	public String getLocation() {
		return this.location;
	}

	/**
	 * Ausstattungsfeatures setzen
	 * @param features
	 */
	public void setFeatures(RentalPropertyFeatures features) {
		this.features = features;
	}

	/**
	 * Ausstattungsfeatures ausgeben
	 * @return features to get
	 */
	public RentalPropertyFeatures getFeatures() {
		return this.features;
	}

	/**
	 * Gruppengroeße setzen
	 * @param groupSize
	 */
	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}

	/**
	 * Gruppengroeße ausgeben
	 * @return gruppengroeße to get
	 */
	public int getGroupSize() {
		return this.groupSize;
	}

	/**
	 * Preispanne setzen
	 * @param minPrice
	 * @param maxPrice
	 */
	public void setPriceRange(int minPrice, int maxPrice) {
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
	}

	public boolean isValid() {
		if (this.location.isEmpty())
			return false;

		if (this.features == null)
			return false;

		if (this.groupSize == 0)
			return false;

		return true;
	}

	public HashMap<String, Object> toMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("location", location);
		map.put("group_size", groupSize);
		map.put("min_price", minPrice);
		map.put("max_price", maxPrice);
		map.put("features", features.toMap());

		return map;
	}
}
