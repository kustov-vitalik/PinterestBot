package com.age.pinterest.config;

public class PinterestAccount {
	private String email;
	private String password;
	private String user;
	private String sslToken;
	private String sessionToken;
	private String bToken;

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

	public String getSslToken() {
		return this.sslToken;
	}

	public void setSslToken(String sslToken) {
		this.sslToken = sslToken;
	}

	public String getSessionToken() {
		return this.sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public String getBToken() {
		return this.bToken;
	}

	public void setBToken(String bToken) {
		this.bToken = bToken;
	}
}
