package com.age.param;

import com.age.data.User;

public class Follow {
	private int size;
	private User user;
	private long interval;

	public Follow(User user, int size, long interval) {
		this.user = user;
		this.size = size;
		this.interval = interval;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return this.size;
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
}
