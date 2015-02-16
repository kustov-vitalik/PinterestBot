package com.age.help;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DescriptionGenerator {

	public List<String> getQuotes(String keyword, int size) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
		System.out.println("Generating quotes for " + keyword);
		WebDriver driver = PinUtils.getChrome();
		ArrayList<String> quotes = new ArrayList<String>();
		String num = "";
		String urlFormat = "http://www.brainyquote.com/quotes/keywords/" + keyword + "%s.html";
		driver.get(String.format(urlFormat, num));
		int i = 0;
		while (quotes.size() < size) {
			PinUtils.waitForElement(By.id("quotesList"), driver);
			List<WebElement> elements = driver.findElement(By.id("quotesList")).findElements(By.cssSelector("*"));
			for (WebElement we : elements) {
				try {
					if (we.getTagName().equals("span")) {
						String quote = we.getText();
						if (quote.length() > 0 && !quote.equals("Share")) {
							quotes.add(quote);
						}
					}
				} catch (Exception e) {
				}

			}
			PinUtils.waitForPage(driver);
			num = Integer.toString(i);
			String url = String.format(urlFormat, "_" + num);
			driver.get(url);
			System.out.println("Got  " + quotes.size() + "  quotes. Remaining " + (size - quotes.size()));
			i++;
		}
		driver.quit();
		return quotes;
	}
}
