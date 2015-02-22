package com.age.pinterest.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.age.data.Pinner;
import com.age.help.PinUtils;
import com.age.pinterest.api.AccountManager;
import com.age.pinterest.config.PinterestAccount;

public class UnFollowTask extends Task {
	private static final Logger logger =  Logger.getLogger(UnFollowTask.class);
	private static final String USER_URL_FORMAT = "https://www.pinterest.com/%s/";
	private final long interval;
	private final PinterestAccount acc;
	private final int minFollowers;

	public UnFollowTask(PinterestAccount acc, int minFollowers, long interval) {
		this.interval = interval;
		this.acc = acc;
		this.minFollowers = minFollowers;
	}

	@Override
	public void run() {
		WebDriver driver = PinUtils.getChrome();
		AccountManager manager = new AccountManager(acc, driver);
		List<Pinner> trashPinners = manager.getUnfollowList(minFollowers);
		while (!trashPinners.isEmpty()) {
			if (this.intervalPassed(interval)) {
				try {
					Pinner pinner = trashPinners.get(0);
					manager.unfollow(pinner);
					trashPinners.remove(0);
				} catch (Exception e) {
					logger.error("",e);
				}
			}
		}
		driver.quit();
	}

}