package com.age.pinterest.task;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.age.pinterest.bot.PinUtils;

public class FollowTask extends Task {

	public FollowTask(long interval) {
		super(interval);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	public void follow(String email, String password, String keywords) throws InterruptedException {
		WebDriver driver = PinUtils.getPhantomDriver();
		PinUtils.login(driver, email, password);
		List<String> targets = this.buildList(driver, 1000, keywords);
		System.out.println("You have  " + targets.size() + "  targets.");
		for (String userItem : targets) {
			System.out.println("Now  " + userItem);
			driver.get(userItem + "/followers");
			String xpath = "/html/body/div[1]/div[2]/div[1]/div[2]/div[4]/div/div";
			while (driver.findElements(By.xpath(xpath)).size() <= 0) {
				Thread.sleep(400);
			}
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			System.out.println("Scrolling");
			for (int i = 0; i < 30; i++) {
				jse.executeScript("window.scrollBy(0,2000)", "");
				Thread.sleep(1800);
			}
			System.out.println("Scrolled");
			PinUtils.waitForPage(driver);
			System.out.println("Page loaded");
			for (WebElement e : driver.findElement(By.xpath(xpath)).findElements(By.cssSelector("*"))) {
				try {
					if (e.getTagName().equals("span") && e.getText().equals("Follow")) {
						e.click();
						System.out.println("Folowed");
						Thread.sleep(5000);
					}
				} catch (Exception ex) {
					System.out.println("Fail");
				}
			}
			System.out.println("Done for  " + userItem);
		}
	}

	private List<String> buildList(WebDriver driver, int size, String keyword) throws InterruptedException {
		ArrayList<String> targets = new ArrayList<String>();
		System.out.println("Building target list with keyword  " + keyword);
		String url = "http://www.pinterest.com/search/people/?q=" + keyword;
		driver.get(url);
		String listXpath = "/html/body/div[1]/div[2]/div[1]/div[2]/div[3]/div/div";
		WebElement list = PinUtils.waitFor(By.xpath(listXpath), driver);
		List<WebElement> lsTarget = list.findElements(By.tagName("a"));
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		int count = 0;
		while (lsTarget.size() < size) {
			jse.executeScript("window.scrollBy(0,4000)", "");
			Thread.sleep(1800);
			count++;
			lsTarget = list.findElements(By.tagName("a"));
			if (count > 20) {
				System.out.println("Not enought targets to reach  " + size);
				break;
			}
		}
		for (WebElement element : lsTarget) {
			targets.add(element.getAttribute("href"));
		}
		System.out.println("List ready in  " + count + "  scrolls.");
		return targets;
	}

}
