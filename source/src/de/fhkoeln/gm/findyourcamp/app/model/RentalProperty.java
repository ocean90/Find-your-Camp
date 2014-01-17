package de.fhkoeln.gm.findyourcamp.app.model;

import java.util.HashMap;
import java.util.Map;

public class RentalProperty {

	private RentalPropertyFeatures features = null;
	private String location = "";
	private int groupSize = 0;
	private int minPrice;
	private int maxPrice;

	public RentalProperty() {
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return this.location;
	}

	public void setFeatures(RentalPropertyFeatures features) {
		this.features = features;
	}

	public RentalPropertyFeatures getFeatures() {
		return this.features;
	}

	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}

	public int getGroupSize() {
		return this.groupSize;
	}

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
