package com.age.pinterest.task;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.age.help.PinUtils;

public class UnFollowTask extends Task {
	private final long interval;
	private final WebDriver driver;
	private final ArrayList<String> trash;

	public UnFollowTask(WebDriver driver, long interval, ArrayList<String> trash) {
		this.interval = interval;
		this.driver = driver;
		this.trash = trash;
	}

	public void execute() {
		try {
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}