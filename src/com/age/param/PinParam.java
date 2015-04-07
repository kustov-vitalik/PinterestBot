package com.age.param;

import com.age.data.Board;
import com.age.data.User;

public class PinParam {
	private long interval;
	private Board board;
	private User user;

	public PinParam(User user, Board board, long interval) {
		this.user = user;
		this.board = board;
		this.interval = interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public long getInterval() {
		return this.interval;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Board getBoard() {
		return this.board;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}
}
