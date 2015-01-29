package com.age.pinterest.task;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.age.pinterest.bot.PinUtils;
import com.age.pinterest.config.PinterestAccount;

public class UnFollowTask extends Task {

	public UnFollowTask(long interval) {
		super(interval);
	}

	@Override
	public void execute() {
		try {
			unfollowAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void unfollowAll() throws InterruptedException {
		WebDriver driver = PinUtils.getChrome();
		PinterestAccount acc = new PinterestAccount();
		acc.setEmail("globalamericaselfdefensejohn@gmail.com");
		acc.setPassword("Geni0us!");
		acc.setUser("globalamericase");
		PinUtils.login(driver, acc);
		for (int i = 0; i < 100; i++) {

			JavascriptExecutor jse = (JavascriptExecutor) driver;
			driver.get("http://www.pinterest.com/globalamericase/following/");

			String xpath = "/html/body/div[1]/div[2]/div[1]/div[2]/div[4]/div[1]/div/div/a[2]";
			PinUtils.waitFor(By.xpath(xpath), driver).click();
			Thread.sleep(2000);
			for (int i = 0; i < 15; i++) {
				System.out.println("Scroll");
				jse.executeScript("window.scrollBy(0,2000)", "");
				Thread.sleep(2000);
			}
			PinUtils.waitForPage(driver);
			String rootCss = "body > div.App.full.AppBase.Module > div.appContent > div.mainContainer > div.UserProfilePage.Module > div.UserProfileContent.ajax.Module > div.Module.Grid > div";
			WebElement divRoot = PinUtils.waitFor(By.cssSelector(rootCss), driver);
			List<WebElement> list = divRoot.findElements(By.tagName("div"));
			System.out.println("Scaning  " + list.size());
			ArrayList<String> trash = new ArrayList<String>();
			for (WebElement e : list) {
				if (e.getAttribute("class").equals("Module User gridItem draggable")) {
					// WebElement btn = e.findElement(By.tagName("button"));
					// System.out.println(btn.getText());
					WebElement a = e.findElement(By.tagName("a"));
					WebElement p = a.findElement(By.tagName("p"));
					List<WebElement> spans = p.findElements(By.tagName("span"));
					int followersCount = Integer.parseInt(spans.get(2).getText());
					if (followersCount < 2000) {
						trash.add(a.getAttribute("href"));
					}
				}
			}
			// driver.quit();
			// driver = PinUtils.getChrome();
			System.out.println("Accouints to unfollow  " + trash.size());
			for (String s : trash) {
				try {
					driver.navigate().to(s);
					PinUtils.waitForPage(driver);
					String btnXpath = "/html/body/div[1]/div[2]/div[1]/div[2]/div[2]/div/div/div[2]/div[1]/div[2]/button[1]";
					WebElement btn = PinUtils.waitForWithTimeout(By.xpath(btnXpath), driver, 1000 * 15);
					if (btn == null) {
						continue;
					}
					if (btn.getText().equals("Unfollow")) {
						btn.click();
						System.out.println("UnFollow  " + s);
						Thread.sleep(1000 * 5);
					}
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
	}

}
