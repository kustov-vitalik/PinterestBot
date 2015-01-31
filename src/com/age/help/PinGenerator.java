package com.age.help;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.age.pinterest.bot.PinBot;
import com.age.pinterest.config.Pin;

public class PinGenerator {
	private final String PINS_ROOT = PinBot.ROOT_DIR + "/pins";
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
			System.out.println("Now  " + href);
			try {
				driver.navigate().to(href);
				PinUtils.waitForPage(driver);
				String imgXpath = "#kd_image_large_gallery > div.owl-wrapper-outer > div > div:nth-child(1) > div";
				WebElement pic = PinUtils.waitFor(By.cssSelector(imgXpath), driver);
				String image = pic.findElement(By.tagName("a")).getAttribute("href");
				WebElement descrDiv = PinUtils.waitFor(By.id("tab-description"), driver);
				String descr = descrDiv.findElement(By.tagName("p")).getText();

				Pin pin = new Pin();
				pin.setDescription(descr);
				pin.setSource(href);
				pin.setImage(image);
				pins.add(pin);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		for (int i = 0; i < pins.size(); i++) {
			String path = PINS_ROOT + "/" + category + "/";
			File file = new File(PINS_ROOT + "/" + category + "/");
			file.mkdirs();
			path = path + Integer.toString(i) + ".json";
			mapper.writeValue(new File(path), pins.get(i));
		}
	}
}
