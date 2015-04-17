package com.age.help;


public class FileLogger {
	private final String logName;

	public FileLogger(String logName) {
		this.logName = logName;
	}

	public void log(String text) {
		System.out.println(text);
		FileUtill.appendToFile(logName, text + "\n");
	}

	public void log(Object o) {
		this.log(o.toString());
	}

	public void log(String text, Throwable t) {
		log(text);
		log(t.toString());
		for (StackTraceElement el : t.getStackTrace()) {
			log(el.toString());
		}
	}

}
