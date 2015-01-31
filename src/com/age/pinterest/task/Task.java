package com.age.pinterest.task;

public abstract class Task {
	private long lastTimeOut = System.currentTimeMillis();

	public abstract void execute();

	protected boolean intervalPassed(long interval) {
		if (System.currentTimeMillis() - lastTimeOut > interval) {
			lastTimeOut = System.currentTimeMillis();
			return true;
		}
		return false;
	}
}