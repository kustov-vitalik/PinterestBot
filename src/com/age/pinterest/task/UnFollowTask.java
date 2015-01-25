package com.age.pinterest.task;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.age.pinterest.bot.PinUtils;

public class UnFollowTask extends Task {

	public UnFollowTask(long interval) {
		super(interval);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}
	public void unfollowAll() throws InterruptedException {
		WebDriver driver = PinUtils.getPhantomDriver();
		PinUtils.login(driver, "Linda1234Williams@gmail.com", "iskamparola!");
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		while (true) {
			driver.get("http://www.pinterest.com/globalamericase/following/");

			String xpath = "/html/body/div[1]/div[2]/div[1]/div[2]/div[4]/div[1]/div/div/a[2]";
			while (driver.findElements(By.xpath(xpath)).size() <= 0) {
				Thread.sleep(500);
				System.out.println("Wainting");
			}
			driver.findElement(By.xpath(xpath)).click();
			String rootXpath = "body > div.App.full.AppBase.Module > div.appContent > div.mainContainer > div.UserProfilePage.Module > div.UserProfileContent.ajax.Module > div.Module.Grid > div";
			Thread.sleep(2000);
			for (int i = 0; i < 5; i++) {
				jse.executeScript("window.scrollBy(0,2000)", "");
				Thread.sleep(1800);
			}
			WebElement divRoot = driver.findElement(By.cssSelector(rootXpath));
			List<WebElement> list = divRoot.findElements(By.cssSelector("*"));
			for (WebElement e : list) {
				try {
					if (e.getTagName().equals("button") && e.getText().equals("Unfollow")) {
						e.click();
						System.out.println("UnFolowed");
						Thread.sleep(500);
					}
				} catch (Exception e1) {
					System.out.println("Failed to unfollow");
				}
			}
			System.out.println("End");
		}

	}

}
