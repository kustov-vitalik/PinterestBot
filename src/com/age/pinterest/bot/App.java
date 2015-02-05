package com.age.pinterest.bot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.age.help.ImageScraper;
import com.age.help.PinGenerator;
import com.age.help.PinUtils;
import com.age.pinterest.config.PinterestAccount;

public class App {
	private static final String COCO = "globalamericase";
	private static final String LINDA = "linda1234willia";
	private static final String STACEY = "stacey123gray";
	private static final String COCO_BOARD = "dreamy-jewelry";
	private static final String LINDA_BOARD = "jewelry";
	private static final String STACEY_BOARD = "jewelry-that-i-would-like-to-wear";

	public static void main(String[] args) throws InterruptedException, IOException {
		WebDriver driver = PinUtils.getFirefoxDriver();
		// ImageScraper scrapper = new ImageScraper(driver, "wheat", "grass");
		// scrapper.scan();
		// PinGenerator gen = new PinGenerator();
		// gen.generatePin("amulets");
		PinBot bot = new PinBot(driver, STACEY);
		// bot.addPinTask(COCO_BOARD, 1000 * 60 * 75);
		// bot.addFollowTask(15000);
		// bot.start();
		// PinBot bot = new PinBot(driver, "stacey123gray");
		// bot.addPinTask("dreamy-jewelry", 1000 * 60 * 45);
		bot.addUnfollowTask(12000);
		bot.start();

	}

	private static PinterestAccount getAccount(String jsonFile) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(new File(jsonFile), PinterestAccount.class);

	}

	private static void generatePins() throws JsonGenerationException, JsonMappingException, InterruptedException, IOException {
		PinGenerator generator = new PinGenerator();
		ArrayList<String> categories = new ArrayList<String>();
		categories.add("valentines-day-offers");
		for (String cat : categories) {
			generator.generatePin(cat);
		}
	}
}
