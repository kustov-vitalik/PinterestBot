package com.age.data;

public class Pinner {
	private String username;
	private int followers;
	private String id;
	private int pins;
	private String fullName;

	/**
	 * @param username
	 * @param followers
	 * @param id
	 * @param pins
	 * @param fullName
	 */
	public Pinner(String username, int followers, String id, int pins, String fullName) {
		this.username = username;
		this.followers = followers;
		this.id = id;
		this.pins = pins;
		this.fullName = fullName;
	}

	public Pinner() {

	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getFollowers() {
		return this.followers;
	}

	public void setFollowers(int followers) {
		this.followers = followers;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPins() {
		return this.pins;
	}

	public void setPins(int pins) {
		this.pins = pins;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return "User: " + this.fullName + "  followers:" + this.followers + "  pins:" + this.pins + "  username:" + this.username + "  ID:"
				+ this.id;
	}

	@Override
	public boolean equals(Object p) {
		if (p instanceof Pinner) {
			Pinner pinner = (Pinner) p;
			if (pinner.getUsername().equals(this.getUsername())) {
				return true;
			}
		}
		return false;
	}

}
