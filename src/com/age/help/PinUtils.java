package com.age.help;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.age.ui.Log;

public class PinUtils {


	public static FirefoxDriver getFirefoxDriver() {
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("permissions.default.stylesheet", "2");
		profile.setPreference("permissions.default.image", "2");
		FirefoxDriver driver = new FirefoxDriver(profile);
		driver.get("https://pinterest.com/");
		return driver;
	}

	public static void waitForElement(By by, WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(by));
	}

	public static WebElement waitForWithTimeout(By by, WebDriver driver, long timeout) {
		long now = System.currentTimeMillis();
		WebElement element = null;
		while (driver.findElements(by).size() <= 0 && System.currentTimeMillis() - now < timeout) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Log.log(e.getMessage());
			}
		}
		try {
			element = driver.findElement(by);
		} catch (Exception e) {
			Log.log("waitForWithTimeout exceptio " + e.getMessage());
		}
		return element;
	}

	public static WebElement waitFor(By by, WebDriver driver) {
		while (driver.findElements(by).size() <= 0) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Log.log(e.getMessage());
			}
		}
		return driver.findElement(by);
	}

	public static void waitForPage(WebDriver driver) throws InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		while (!js.executeScript("return document.readyState").equals("complete")) {
			Thread.sleep(500);
		}

	}

}
