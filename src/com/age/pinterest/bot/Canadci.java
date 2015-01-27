package com.age.pinterest.bot;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Canadci {
	public static void main(String[] args) throws InterruptedException {
		WebDriver driver = PinUtils.getChrome();
		driver.get("https://www.freephoneline.ca/login");
		PinUtils.waitForPage(driver);
		PinUtils.waitFor(By.id("doLogin_loginEmail"), driver).sendKeys("renata_oliver@abv.bg");
		PinUtils.waitFor(By.id("doLogin_loginPassword"), driver).sendKeys("Geni0us!");
		PinUtils.waitFor(By.id("doLogin_0"), driver).click();

		PinUtils.waitForPage(driver);
		PinUtils.waitFor(
				By.cssSelector("body > div > table > tbody > tr:nth-child(4) > td > table:nth-child(2) > tbody > tr:nth-child(2) > td > table > tbody > tr:nth-child(1) > td > table > tbody > tr:nth-child(6) > td > a > img"),
				driver).click();

		String code = "";
		for (int i = 0; i < 1000; i++) {
			WebElement codeBox = PinUtils.waitFor(By.id("doRegisterDesktopAccountStepThree_confirmationCode"), driver);
			if (i / 10 == 0) {
				code = "00" + Integer.toString(i);
			} else if (i / 100 == 0) {
				code = "0" + Integer.toString(i);
			} else {
				code = Integer.toString(i);
			}
			codeBox.sendKeys(code);
			PinUtils.waitFor(By.id("doRegisterDesktopAccountStepThree_0"), driver).click();
			driver.get(driver.getCurrentUrl());
			PinUtils.waitForPage(driver);
		}
	}
}
