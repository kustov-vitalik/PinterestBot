package com.age.data;

public class UserConfig {
	private long unfollowTime;
	private int minFollowers;
	private long followTime;
	private int followCount;
	private long pinTime;
	private Board pinBoard;

	public UserConfig() {
		this.unfollowTime = 1000 * 11;
		this.followTime = 1000 * 11;
		this.pinTime = 1000 * 60 * 10;
		this.followCount = 5000;
		this.minFollowers = 5000;
		this.pinBoard = null;

	}

	public void setUnfollowTime(long time) {
		this.unfollowTime = time;
	}

	public long getUnfollowTime() {
		return this.unfollowTime;
	}

	public void setMinFollowers(int followers) {
		this.minFollowers = followers;
	}

	public int getMinFollowers() {
		return this.minFollowers;
	}

	public void setFollowTime(long time) {
		this.followTime = time;
	}

	public long getFollowTime() {
		return this.followTime;
	}

	public void setFollowCount(int count) {
		this.followCount = count;
	}

	public int getFollowCount() {
		return this.followCount;
	}

	public void setPinTime(long time) {
		this.pinTime = time;
	}

	public long getPinTime() {
		return this.pinTime;
	}

	public void setPinBoard(Board board) {
		this.pinBoard = board;
	}

	public Board getPinBoard() {
		return this.pinBoard;
	}

}
