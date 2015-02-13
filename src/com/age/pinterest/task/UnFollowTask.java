package com.age.pinterest.task;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.age.data.Pinner;
import com.age.help.PinUtils;
import com.age.pinterest.api.AccountManager;
import com.age.pinterest.config.PinterestAccount;

public class UnfollowTask extends Task {
	private static final String USER_URL_FORMAT = "https://www.pinterest.com/%s/";
	private final long interval;
	private final PinterestAccount acc;
	private final int minFollowers;

	public UnfollowTask(PinterestAccount acc, int minFollowers, long interval) {
		this.interval = interval;
		this.acc = acc;
		this.minFollowers = minFollowers;
	}

	@Override
	public void run() {
		WebDriver driver = PinUtils.getChrome();
		AccountManager manager = new AccountManager(acc, driver);
		List<Pinner> trashPinners = manager.getUnfollowList(minFollowers);
		ArrayList<String> trash = new ArrayList<String>();
		for (Pinner p : trashPinners) {
			trash.add(String.format(USER_URL_FORMAT, p.getUsername()));
		}
		while (!trash.isEmpty()) {
			if (this.intervalPassed(interval)) {
				try {
					boolean unfollowed = false;
					while (!unfollowed) {
						unfollowed = this.unfollow(trash, driver);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		driver.quit();
	}

	private boolean unfollow(List<String> trash, WebDriver driver) throws InterruptedException {
		String trashItem = trash.get(0);
		boolean result = false;
		driver.navigate().to(trashItem);
		PinUtils.waitForPage(driver);
		String btnXpath = "/html/body/div[1]/div[2]/div[1]/div[2]/div[2]/div/div/div[2]/div[1]/div[2]/button[1]";
		WebElement btn = PinUtils.waitForWithTimeout(By.xpath(btnXpath), driver, 1000 * 15);
		if (btn != null && btn.getText().equals("Unfollow")) {
			btn.click();
			System.out.println("UnFollow  " + trashItem);
			result = true;
		}
		trash.remove(0);
		return result;
	}
}