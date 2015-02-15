package com.age.pinterest.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.age.data.Pinner;
import com.age.help.FileUtill;
import com.age.help.PinUtils;
import com.age.pinterest.api.AccountManager;
import com.age.pinterest.bot.PinBot;
import com.age.pinterest.config.PinterestAccount;

public class FollowTask extends Task {
	public static final String PATH_TO_HISTORY_FORMAT = PinBot.ROOT_DIR + "/Users/%s/followed.txt";
	private static final String USER_URL_FORMAT = "https://www.pinterest.com/%s/";

	private final String keyword;
	private final int size;
	private final PinterestAccount acc;
	private final long interval;

	public FollowTask(PinterestAccount acc, String keyword, int size, long interval) {
		this.interval = interval;
		this.acc = acc;
		this.keyword = keyword;
		this.size = size;
	}

	@Override
	public void run() {
		WebDriver driver = PinUtils.getChrome();
		AccountManager manager = new AccountManager(acc, driver);
		List<Pinner> followList = manager.getFollowList(size);
		while (!followList.isEmpty()) {
			if (this.intervalPassed(interval)) {
				try {
					Pinner pinner = followList.get(0);
					manager.follow(pinner);
					followList.remove(0);
				} catch (Exception e) {
					System.out.println(acc.getUser() + "  Failed to follow");
					e.printStackTrace();
				}
			}
		}
		driver.quit();
	}
}
