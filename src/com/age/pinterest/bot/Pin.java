package com.age.pinterest.bot;

public class Pin {
	private final String board;
	private final String description;
	private final String pathToFile;
	private final String location;
	private final String source;

	public Pin(String board, String pathToFile, String description, String location, String source) {
		this.board = board;
		this.pathToFile = pathToFile;
		this.description = description;
		this.location = location;
		this.source = source;
	}

	public String getBoard() {
		return this.board;
	}

	public String getPathToFile() {
		return this.pathToFile;
	}

	public String getDescription() {
		return this.description;
	}

	public String getLocation() {
		return this.location;
	}

	public String getSource() {
		return this.source;
	}
}
