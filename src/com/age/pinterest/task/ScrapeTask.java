package com.age.pinterest.task;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.age.help.ImageScraper;
import com.age.help.PinUtils;

public class ScrapeTask extends Task {
	private static final Logger logger =  Logger.getLogger(ScrapeTask.class);
	private final String keyword;
	private final String tag;

	public ScrapeTask(String keyword, String tag) {
		this.keyword = keyword;
		this.tag = tag;
	}

	@Override
	public void run() {
		WebDriver driver = PinUtils.getChrome();
		ImageScraper scrapper = new ImageScraper(driver, keyword, tag);
		try {
			scrapper.scrape();
		} catch (InterruptedException | IOException e) {
			logger.error("",e);
		}
	}
}
