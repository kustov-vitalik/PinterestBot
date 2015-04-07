package com.age.param;

import com.age.data.User;

public class UnfollowParam {
	private long interval;
	private int minFollowers;
	private User user;

	public UnfollowParam(User user, int minFollowers, long interval) {
		this.user = user;
		this.minFollowers = minFollowers;
		this.interval = interval;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public long getInterval() {
		return this.interval;
	}

	public void setMinFollowers(int minFollowers) {
		this.minFollowers = minFollowers;
	}

	public int getMinFollowers() {
		return this.minFollowers;
	}
}
