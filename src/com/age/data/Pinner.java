package com.age.data;

public class Pinner {
	private String username;
	private int followers;
	private String id;
	private int pins;
	private String fullName;

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

}
