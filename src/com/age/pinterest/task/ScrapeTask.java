package com.age.pinterest.task;

import java.io.IOException;

import com.age.help.ImageScraper;
import com.age.ui.Log;

public class ScrapeTask extends Task {
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
			Log.log("Image scrape task failed.  " +e.getMessage());
		}
	}

}
