package com.age.pinterest.task;

import java.util.concurrent.TimeUnit;

import com.age.help.BotPaths;
import com.age.help.FileLogger;

public abstract class Task implements Runnable {
	protected final FileLogger logger;

	protected Task(String pathToLog) {
		logger = new FileLogger(BotPaths.LOGS + pathToLog);
	}

	protected void sleep(long interval) {
		logger.log(this.getType() + " task is sleeping");
		long startTime = System.currentTimeMillis();
		long passedTime = 0;
		logger.log("Will wait " + TimeUnit.MILLISECONDS.toSeconds(interval - passedTime) + " seconds");
		while (passedTime < interval) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.log("Wait was interupted");
				e.printStackTrace();
			}
			passedTime = System.currentTimeMillis() - startTime;
			if (passedTime % 10000 == 0) {
				logger.log("Will wait " + TimeUnit.MILLISECONDS.toSeconds(interval - passedTime) + " more seconds");
			}
		}
	}

	public abstract TaskType getType();

}