package com.age.data;

public class PinterestAccount {
	private String email;
	private String password;
	private String user;

	public PinterestAccount(String email, String password, String user) {
		this.email = email;
		this.password = password;
		this.user = user;

	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return this.user;
	}

}
