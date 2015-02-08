package com.age.pinterest.bot;

import java.io.IOException;

import org.json.JSONException;
import org.openqa.selenium.WebDriver;

import com.age.help.PinUtils;

public class App {
	private static final String COCO = "globalamericase";
	private static final String LINDA = "linda1234willia";
	private static final String STACEY = "stacey123gray";
	private static final String COCO_BOARD = "dreamy-jewelry";
	private static final String LINDA_BOARD = "jewelry";
	private static final String STACEY_BOARD = "jewelry-that-i-would-like-to-wear";

	public static void main(String[] args) throws InterruptedException, IOException, JSONException {
		WebDriver driver = PinUtils.getChrome();
		PinBot bot = new PinBot(driver, STACEY);
		bot.addPinTask(STACEY_BOARD, 1000 * 60 * 40);
		bot.start();
		// PinGenerator gen = new PinGenerator();
		// gen.generatePin("bracelets");
		// gen.generatePin("bellychains");
		// gen.generatePin("bangles");
		// gen.generatePin("Anklets");
		// gen.generatePin("amulets");
		// gen.generatePin("pendants");
		// gen.generatePin("rings");
		// gen.generatePin("earrings");

	}

}
