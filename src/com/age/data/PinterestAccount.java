package com.age.data;

public class PinterestAccount {
	private String email;
	private String password;
	private String username;

	public PinterestAccount() {
	}

	public PinterestAccount(String email, String password, String username) {
		this.email = email;
		this.password = password;
		this.username = username;

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

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

}
