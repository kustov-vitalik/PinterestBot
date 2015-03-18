package com.age.data;

public class Board {
	private String id;
	private String name;

	public Board(String name, String id) {
		this.name = name;
		this.id = id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setName(String name) {
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
