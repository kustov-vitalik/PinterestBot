package com.age.pinterest.task;

import java.util.ArrayList;
import java.util.List;

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
				users = this.getListFromUser("https://www.pinterest.com/NewportSkinnyT/followers");
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
		driver.get(user);
		PinUtils.waitForPage(driver);
		String rootDivXpath = "/html/body/div[1]/div[2]/div[1]/div[2]/div[4]/div";
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		for (int j = 0; j < 170; j++) {
			System.out.println("Scroll  " + j);
			jse.executeScript("window.scrollBy(0,500)", "");
			Thread.sleep(200);
		}
		Thread.sleep(10000);
		ArrayList<String> targets = new ArrayList<String>();
		WebElement rootDiv = PinUtils.waitFor(By.xpath(rootDivXpath), driver);
		List<WebElement> items = rootDiv.findElements(By.tagName("button"));
		System.out.println("Total   " + items.size());
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
