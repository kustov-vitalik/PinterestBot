package com.age.help;

public class FileLogger {
	private final String logName;

	public FileLogger(String logName) {
		this.logName = BotPaths.LOGS + logName;
	}

	public void log(String text) {
		System.out.println(text);
		FileUtill.appendToFile(logName, text + "\n");
	}

}
