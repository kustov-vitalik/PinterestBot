package com.age.pinterest.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.age.help.PinUtils;

public class FollowTask extends Task {
	private final WebDriver driver;
	private final long interval;
	private ArrayList<String> users = new ArrayList<String>();

	public FollowTask(WebDriver driver, long interval) {
		this.interval = interval;
		this.driver = driver;
	}

	@Override
	public void execute() {
		try {
			if (users.size() <= 0) {
				users.addAll(getListFromUser("https://www.pinterest.com/deniz2679/followers/"));
				users.addAll(getListFromUser("https://www.pinterest.com/12e1qnqc8qv0yyw/followers/"));
				users.addAll(getListFromUser("https://www.pinterest.com/perjet89/followers/"));
				users.addAll(getListFromUser("https://www.pinterest.com/vitamerisia/followers/"));
				users.addAll(getListFromUser("https://www.pinterest.com/leenmoraly/followers/"));
				users.addAll(getListFromUser("https://www.pinterest.com/AmagaliBrasil/followers/"));
				users.addAll(getListFromUser("https://www.pinterest.com/BradLouisPerrie/followers/"));
			}
			if (!this.intervalPassed(interval)) {
				return;
			}
			follow();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void follow() throws InterruptedException {
		System.out.println("Follow");
		System.out.println("You have  " + users.size() + "  targets.");
		String userItem = users.get(0);
		driver.get(userItem);
		PinUtils.waitForPage(driver);
		String btnXpath = "/html/body/div[1]/div[2]/div[1]/div[2]/div[2]/div/div/div[2]/div[1]/div[2]/button[1]";
		WebElement btn = PinUtils.waitForWithTimeout(By.xpath(btnXpath), driver, 1000 * 15);
		if (btn != null && btn.getText().equals("Follow")) {
			btn.click();
			System.out.println("Followed  " + userItem);
		}
		users.remove(0);
	}

	private ArrayList<String> getListFromUser(String user) throws InterruptedException {
		System.out.println(user);
		driver.get(user);
		PinUtils.waitForPage(driver);
		String rootDivXpath = "/html/body/div[1]/div[2]/div[1]/div[2]/div[4]/div";
		System.out.println("Next");
		PinUtils.waitForPage(driver);
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		for (int j = 0; j < 50; j++) {
			System.out.println("Scroll  " + j);
			jse.executeScript("window.scrollBy(0,1500)", "");
			Thread.sleep(2000);
		}
		Thread.sleep(10000);
		ArrayList<String> targets = new ArrayList<String>();
		System.out.println("Here");
		WebElement rootDiv = PinUtils.waitFor(By.xpath(rootDivXpath), driver);
		System.out.println("Heree2");
		List<WebElement> items = rootDiv.findElements(By.tagName("button"));
		System.out.println("Processing   " + items.size());
		PinUtils.waitForPage(driver);
		for (WebElement el : items) {
			if (el.getText().equals("Follow")) {
				WebElement parrent = el.findElement(By.xpath(".."));
				WebElement a = parrent.findElement(By.tagName("a"));
				targets.add(a.getAttribute("href"));
			}
		}
		return targets;
	}
}
