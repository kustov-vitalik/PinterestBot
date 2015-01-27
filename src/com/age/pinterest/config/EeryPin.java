package com.age.pinterest.config;

public class EeryPin {
	private String description;
	private String source;
	private String board;
	private String image;

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return this.source;
	}

	public void setBoard(String board) {
		this.board = board;
	}

	public String getBoard() {
		return this.board;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return this.image;
	}

	@Override
	public String toString() {
		return this.board + "\n" + this.image + "\n" + this.source + "\n" + this.description + "\n";
	}

}
