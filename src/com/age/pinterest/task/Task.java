package com.age.pinterest.task;

public abstract class Task {
	private final long interval;
	private long lastTimeOut = System.currentTimeMillis();

	public Task(long interval) {
		this.interval = interval;
	}

	public abstract void execute();

	public boolean intervalPassed() {
		if (System.currentTimeMillis() - lastTimeOut > interval) {
			lastTimeOut = System.currentTimeMillis();
			return true;
		}
		return false;
	}
}