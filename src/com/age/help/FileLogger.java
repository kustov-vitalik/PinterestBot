package com.age.help;

import java.io.IOException;

public class FileLogger {
	private final String pathToLog;

	public FileLogger(String pathToLog) {
		this.pathToLog = pathToLog;
	}

	public void log(String text) {
		System.out.println(text);
		try {
			FileUtill.appendToFile(pathToLog, text + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
