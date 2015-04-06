package com.age.pinterest.task;

import java.util.concurrent.TimeUnit;

public abstract class Task implements Runnable {
	private long lastTimeOut = 0;

	protected boolean intervalPassed(long interval) {
		if (System.currentTimeMillis() - lastTimeOut > interval) {
			lastTimeOut = System.currentTimeMillis();
			return true;
		}
		return false;
	}

	protected void sleep(long interval) {
		long startTime = System.currentTimeMillis();
		long waitTime = interval / (1000 * 2);
		long passedTime = 0;
		System.out.println("Will wait " + TimeUnit.MILLISECONDS.toSeconds(interval - passedTime) + " seconds");
		while (passedTime < interval) {
			try {
				Thread.sleep(waitTime * 1000);
			} catch (InterruptedException e) {
				System.out.println("Wait was interupted");
				e.printStackTrace();
			}
			passedTime = System.currentTimeMillis() - startTime;
			System.out.println("Will wait " + TimeUnit.MILLISECONDS.toSeconds(interval - passedTime) + " more seconds");
		}
	}

	public abstract TaskType getType();

}