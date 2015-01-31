package com.age.help;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DescriptionGenerator {
	private final WebDriver driver;
	private final String location;

	public DescriptionGenerator(WebDriver driver, String location) {
		this.driver = driver;
		this.location = location;
	}

	public void buildDescriptionList(String keyword) throws FileNotFoundException, UnsupportedEncodingException {
		String num = "";
		String urlFormat = "http://www.brainyquote.com/quotes/topics/topic_" + keyword + "%s.html";
		driver.get(String.format(urlFormat, num));
		for (int i = 2; i <= 2; i++) {
			PinUtils.waitForElement(By.id("quotesList"), driver);
			List<WebElement> elements = driver.findElement(By.id("quotesList")).findElements(By.cssSelector("*"));
			for (WebElement we : elements) {
				try {
					if (we.getTagName().equals("span")) {
						String quote = we.getText();
						if (quote.length() > 0 && !quote.equals("Share")) {
							FileUtill.saveToFile(location + "//" + keyword, System.currentTimeMillis() + ".txt", quote);
							System.out.println(quote);
						}
					}
				} catch (Exception e) {
				}

			}
			num = Integer.toString(i);
			driver.get(String.format(urlFormat, num));
		}
	}
}
