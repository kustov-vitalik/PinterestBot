package com.age.pinterest.task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.age.pinterest.bot.FileUtill;
import com.age.pinterest.bot.PinUtils;

public class FollowTask extends Task {
	private final WebDriver driver;
	private final String keyword;
	Random r = new Random();

	public FollowTask(WebDriver driver, String keyword, long interval) {
		super(interval);
		this.driver = driver;
		this.keyword = keyword;
	}

	@Override
	public void execute() {
		try {
			followAll(keyword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void followAll(String path) throws IOException, InterruptedException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println("Now  " + line);
			driver.navigate().to(line);
			String btnXpath = "/html/body/div[1]/div[2]/div[1]/div[2]/div[2]/div/div/div[2]/div[1]/div[2]/button[1]";
			WebElement btn = PinUtils.waitForWithTimeout(By.xpath(btnXpath), driver, 1000 * 15);
			if (btn == null) {
				continue;
			}
			if (btn.getText().equals("Follow")) {
				btn.click();
				System.out.println("Follow  " + line);
				Thread.sleep(1000 * 10);
			}
		}
		br.close();
	}

	public void follow() throws InterruptedException {
		List<String> targets = this.buildList(driver, 1000, keyword);
		System.out.println("You have  " + targets.size() + "  targets.");
		for (String userItem : targets) {
			System.out.println("Now  " + userItem);
			driver.get(userItem + "/followers");
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			System.out.println("Scrolling");
			for (int i = 0; i < 25; i++) {
				jse.executeScript("window.scrollBy(0,2000)", "");
				Thread.sleep(1800);
			}
			System.out.println("Scrolled");
			PinUtils.waitForPage(driver);
			System.out.println("Page loaded");
			String xpath = "/html/body/div[1]/div[2]/div[1]/div[2]/div[4]/div/div";
			for (WebElement e : PinUtils.waitFor(By.xpath(xpath), driver).findElements(By.tagName("span"))) {
				try {
					if (e.getTagName().equals("span") && e.getText().equals("Follow")) {
						WebElement btn = e.findElement(By.xpath(".."));
						WebElement div = btn.findElement(By.xpath(".."));
						WebElement a = div.findElement(By.tagName("a"));
						FileUtill.appendToFile("D://fishesStacey.txt", a.getAttribute("href"));
						System.out.println(a.getAttribute("href"));
					}
				} catch (Exception ex) {
					System.out.println("Fail " + ex.getMessage());
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

	private void followUser(String url) {
		driver.get(url);
	}

}
