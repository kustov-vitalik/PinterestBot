package com.age.pinterest.task;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.age.help.ImageScraper;

public class ScrapeTask extends Task {
	private static final Logger logger = Logger.getLogger(ScrapeTask.class);
	private final String keyword;
	private final String tag;
	private final int count;

	public ScrapeTask(String keyword, String tag, int count) {
		this.keyword = keyword;
		this.tag = tag;
		this.count = count;
	}

	@Override
	public void run() {
		ImageScraper scrapper = new ImageScraper(tag);
		try {
			scrapper.scrape(keyword, count);
		} catch (InterruptedException | IOException e) {
			logger.error("Image scrape task failed.", e);
		}
	}

	@Override
	public Logger getLog() {
		return logger;
	}
}
