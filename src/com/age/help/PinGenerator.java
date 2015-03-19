package com.age.help;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.age.data.Pin;
import com.age.pinterest.bot.PinBot;
import com.age.ui.Log;

public class PinGenerator {

	private final WebDriver driver;

	public PinGenerator() {
		this.driver = PinUtils.getFirefoxDriver();
	}

	public void generatePin(String category) throws InterruptedException, JsonGenerationException, JsonMappingException, IOException {
		String nextPage = "";
		ArrayList<String> hrefs = new ArrayList<String>();
		ArrayList<Pin> pins = new ArrayList<Pin>();
		for (int i = 2; true; i++) {
			driver.get("http://eeryjewelry.com/product-category/" + category + "/" + nextPage);
			String bodyText = PinUtils.waitFor(By.tagName("body"), driver).getAttribute("class");
			if (bodyText.equals("error404")) {
				break;
			}
			WebElement content = PinUtils.waitFor(By.id("content"), driver);
			for (WebElement element : content.findElements(By.tagName("h2"))) {
				WebElement a = element.findElement(By.tagName("a"));
				hrefs.add(a.getAttribute("href"));
			}

			nextPage = "page/" + Integer.toString(i);
		}
		for (String href : hrefs) {
			Log.log("Now  " + href);
			try {
				driver.navigate().to(href);
				PinUtils.waitForPage(driver);
				String imgXpath = "#kd_image_large_gallery > div.owl-wrapper-outer > div > div:nth-child(1) > div";
				WebElement pic = PinUtils.waitForWithTimeout(By.cssSelector(imgXpath), driver, 1000 * 5);
				String image = pic.findElement(By.tagName("a")).getAttribute("href");
				WebElement descrDiv = PinUtils.waitForWithTimeout(By.id("tab-description"), driver, 1000 * 5);
				String descr = descrDiv.findElement(By.tagName("p")).getText();

				Pin pin = new Pin();
				pin.setDescription(descr);
				pin.setSource(href);
				pin.setImage(image);
				pins.add(pin);
			} catch (Exception e) {
				Log.log(e.getMessage());
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		for (int i = 0; i < pins.size(); i++) {
			String path = BotPaths.PINS_ROOT + category + "/";
			File file = new File(path);
			file.mkdirs();
			path = path + Integer.toString(i) + ".json";
			mapper.writeValue(new File(path), pins.get(i));
		}
	}

	public void test() {
		String href = "http://eeryjewelry.com/product/towall-womens-gold-belly-chain-sexy-bikini-body-chain-infinity-belly-chain/";
		Log.log("Now  " + href);
		try {
			driver.navigate().to(href);
			PinUtils.waitForPage(driver);
			String imgXpath = "#kd_image_large_gallery > div.owl-wrapper-outer > div > div:nth-child(1) > div";
			WebElement pic = PinUtils.waitForWithTimeout(By.cssSelector(imgXpath), driver, 1000 * 5);
			String image = pic.findElement(By.tagName("a")).getAttribute("href");
			WebElement descrDiv = PinUtils.waitForWithTimeout(By.id("tab-description"), driver, 1000 * 5);
			String descr = descrDiv.findElement(By.tagName("p")).getText();

			Pin pin = new Pin();
			pin.setDescription(descr);
			pin.setSource(href);
			pin.setImage(image);
			Log.log(pin.toString());
		} catch (Exception e) {
			Log.log(e.getMessage());
		}
	}

}
