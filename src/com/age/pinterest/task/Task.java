package com.age.pinterest.task;


public abstract class Task implements Runnable {
	private long lastTimeOut = 0;

	protected boolean intervalPassed(long interval) {
		if (System.currentTimeMillis() - lastTimeOut > interval) {
			lastTimeOut = System.currentTimeMillis();
			return true;
		}
		return false;
	}
	public abstract TaskType getType();

}