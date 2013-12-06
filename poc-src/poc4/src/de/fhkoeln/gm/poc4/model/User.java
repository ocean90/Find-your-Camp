package de.fhkoeln.gm.poc4.model;

public class User {
	private int id;
	private String name;

	public User() {
	}

	public void setId( int id ) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
