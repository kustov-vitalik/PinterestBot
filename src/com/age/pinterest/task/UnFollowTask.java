package com.age.pinterest.task;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.age.help.PinUtils;

public class UnFollowTask extends Task {
	private final long interval;
	private final WebDriver driver;
	private final String user;
	private ArrayList<String> trash = new ArrayList<String>();

	public UnFollowTask(WebDriver driver, String user, long interval) {
		this.interval = interval;
		this.driver = driver;
		this.user = user;
	}

	@Override
	public void execute() {
		try {
			if (trash == null || trash.size() == 0) {
				trash = this.getTrash(4000, 25);
			}
			if (!this.intervalPassed(interval)) {
				return;
			}
			System.out.println("Accounts to unfollow  " + trash.size());
			if (trash.size() <= 0) {
				System.out.println("No more trash. Exiting");
				return;
			}
			String trashItem = trash.get(0);
			try {
				driver.navigate().to(trashItem);
				PinUtils.waitForPage(driver);
				String btnXpath = "/html/body/div[1]/div[2]/div[1]/div[2]/div[2]/div/div/div[2]/div[1]/div[2]/button[1]";
				WebElement btn = PinUtils.waitForWithTimeout(By.xpath(btnXpath), driver, 1000 * 15);
				if (btn != null && btn.getText().equals("Unfollow")) {
					btn.click();
					System.out.println("UnFollow  " + trashItem);
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		} catch (Exception e) {
			System.out.println("Failed to unfollow  " + e.getMessage());
		}
		trash.remove(0);

	}

	private ArrayList<String> getTrash(int minFollowers, int depth) throws InterruptedException {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		driver.get("http://www.pinterest.com/" + user + "/following/");

		PinUtils.waitForPage(driver);
		String xpath = "/html/body/div[1]/div[2]/div[1]/div[2]/div[4]/div[1]/div/div/a[2]";
		PinUtils.waitFor(By.xpath(xpath), driver).click();
		Thread.sleep(2000);
		for (int j = 0; j < depth; j++) {
			System.out.println("Scroll  " + j);
			jse.executeScript("window.scrollBy(0,4000)", "");
			Thread.sleep(2000);
		}
		String rootCss = "body > div.App.full.AppBase.Module > div.appContent > div.mainContainer > div.UserProfilePage.Module > div.UserProfileContent.ajax.Module > div.Module.Grid > div";
		WebElement divRoot = PinUtils.waitFor(By.cssSelector(rootCss), driver);
		List<WebElement> list = divRoot.findElements(By.tagName("div"));
		System.out.println("Scaning  " + list.size());
		ArrayList<String> junk = new ArrayList<String>();
		for (WebElement e : list) {
			try {
				if (e.getAttribute("class").equals("Module User gridItem draggable")) {
					PinUtils.waitForPage(driver);
					WebElement a = e.findElement(By.tagName("a"));
					WebElement p = a.findElement(By.tagName("p"));
					List<WebElement> spans = p.findElements(By.tagName("span"));

					PinUtils.waitForPage(driver);
					String followerString = spans.get(2).getText();
					int followersCount = Integer.parseInt(followerString);

					if (followersCount < minFollowers) {
						String href = a.getAttribute("href");
						System.out.println("Adding " + href + " to the list.");
						junk.add(href);
					}
				}
			} catch (Exception expe) {
				System.out.println("Failed to get user href  " + expe.getMessage());
			}
		}
		return junk;

	}
}
