package com.age.data;

import java.util.List;

public class User {
	private Cookies cookies;
	private PinterestAccount account;
	private List<Board> boards;

	public User() {
	}

	public void setCookies(Cookies cookies) {
		this.cookies = cookies;
	}

	public Cookies getCookies() {
		return this.cookies;
	}

	public void setAccount(PinterestAccount account) {
		this.account = account;
	}

	public PinterestAccount getAccount() {
		return this.account;
	}

	public void setBoards(List<Board> boards) {
		this.boards = boards;
	}

	public List<Board> getBoards() {
		return this.boards;
	}

}
