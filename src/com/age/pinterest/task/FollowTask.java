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
		List<String> users = new ArrayList<String>();
		for (Pinner pinner : followList) {
			users.add(String.format(USER_URL_FORMAT, pinner.getUsername()));
		}
		while (!users.isEmpty()) {
			if (this.intervalPassed(interval)) {
				try {
					boolean followed = false;
					while (!followed) {
						followed = this.follow(users, driver);
					}
				} catch (Exception e) {
					System.out.println(acc.getUser() + "  Failed to follow");
					e.printStackTrace();
				}
			}
		}
	}

	private boolean follow(List<String> users, WebDriver driver) throws InterruptedException, IOException {
		System.out.println("You have  " + users.size() + "  targets.");
		String userItem = users.get(0);
		driver.get(userItem);
		PinUtils.waitForPage(driver);
		String btnXpath = "/html/body/div[1]/div[2]/div[1]/div[2]/div[2]/div/div/div[2]/div[1]/div[2]/button[1]";
		WebElement btn = PinUtils.waitForWithTimeout(By.xpath(btnXpath), driver, 1000 * 15);
		boolean result = false;
		if (btn != null && btn.getText().equals("Follow")) {
			btn.click();
			System.out.println("Followed  " + userItem);
			FileUtill.appendToFile(String.format(PATH_TO_HISTORY_FORMAT, this.acc.getUser()), userItem);
			result = true;
		}
		users.remove(0);
		return result;
	}

}
