package com.age.pinterest.task;

import org.apache.log4j.Logger;

public abstract class Task implements Runnable {
	private long lastTimeOut = System.currentTimeMillis();

	protected boolean intervalPassed(long interval) {
		if (System.currentTimeMillis() - lastTimeOut > interval) {
			lastTimeOut = System.currentTimeMillis();
			return true;
		}
		return false;
	}

	public abstract Logger getLog();
}