package com.age.pinterest.task;

import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.age.help.FileUtill;
import com.age.help.PinUtils;
import com.age.pinterest.api.Manager;
import com.age.pinterest.bot.PinBot;

public class FollowTask extends Task {
	private final String user;
	private final WebDriver driver;
	private final long interval;
	private final ArrayList<String> users;

	public FollowTask(WebDriver driver, long interval, String user, ArrayList<String> users) {
		this.interval = interval;
		this.driver = driver;
		this.users = users;
		this.user = user;
	}

	@Override
	public void execute() {
		try {

			if (!this.intervalPassed(interval)) {
				return;
			}
			follow();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void follow() throws InterruptedException, IOException {
		while (true) {
			System.out.println("You have  " + users.size() + "  targets.");
			String userItem = users.get(0);
			driver.get(userItem);
			PinUtils.waitForPage(driver);
			String btnXpath = "/html/body/div[1]/div[2]/div[1]/div[2]/div[2]/div/div/div[2]/div[1]/div[2]/button[1]";
			WebElement btn = PinUtils.waitForWithTimeout(By.xpath(btnXpath), driver, 1000 * 15);
			if (btn != null && btn.getText().equals("Follow")) {
				btn.click();
				System.out.println("Followed  " + userItem);
				FileUtill.appendToFile(PinBot.ROOT_DIR+"/USERS/"+this.user+"/followed.txt", userItem);
				return;
			}
			users.remove(0);
		}
	}
}
