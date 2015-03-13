package com.age.data;

public class CookieList {
	private Cookie bCookie;
	private Cookie sslCookie;
	private Cookie sessionCookie;

	public void setBCookie(Cookie b) {
		this.bCookie = b;
	}

	public Cookie getBCookie() {
		return this.bCookie;
	}

	public void setSslCookie(Cookie sslCookie) {
		this.sslCookie = sslCookie;
	}

	public Cookie getSslCookie() {
		return this.sslCookie;
	}

	public void setSessionCookie(Cookie sessionCookie) {
		this.sessionCookie = sessionCookie;
	}

	public Cookie getSessionCookie() {
		return this.sessionCookie;
	}

	@Override
	public String toString() {
		return sslCookie + " " + sessionCookie + " " + bCookie;
	}

}
