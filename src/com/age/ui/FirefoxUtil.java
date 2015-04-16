package com.age.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.age.data.User;

public class FirefoxUtil {
	public static void startBrowser(User user) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String email = user.getAccount().getEmail();
				String password = user.getAccount().getPassword();
				FirefoxProfile profile = new FirefoxProfile();
				WebDriver driver = new FirefoxDriver(profile);
				System.out.println("Loggin in with user " + email);
				System.out.println("Password is " + password);
				try {
					driver.get("https://www.pinterest.com/login/");
					while (driver.findElements(By.name("username_or_email")).size() <= 0) {
						Thread.sleep(400);
					}
					driver.findElement(By.name("username_or_email")).sendKeys(email);
					driver.findElement(By.name("password")).sendKeys(password);
					driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div/div/form/div[4]/div/button")).click();
				} catch (Exception e) {
					System.out.println("Failed to login");
					e.printStackTrace();
				}
				System.out.println("Logged as " + email);

			}
		}).start();
	}
}
