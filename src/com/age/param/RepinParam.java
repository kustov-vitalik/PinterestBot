package com.age.param;

import com.age.data.Board;
import com.age.data.User;

public class RepinParam {
	private User user;
	private Board board;
	private String keyword;
	private String link;
	private int repinCount;
	private long interval;

	/**
	 * @param user
	 * @param board
	 * @param keyword
	 * @param link
	 * @param repinCount
	 * @param interval
	 */
	public RepinParam(User user, Board board, String keyword, String link, int repinCount, long interval) {
		this.user = user;
		this.board = board;
		this.keyword = keyword;
		this.link = link;
		this.repinCount = repinCount;
		this.interval = interval;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Board getBoard() {
		return this.board;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getLink() {
		return this.link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getRepinCount() {
		return this.repinCount;
	}

	public void setRepinCount(int repinCount) {
		this.repinCount = repinCount;
	}

	public long getInterval() {
		return this.interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}
}
