package com.age.pinterest.bot;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.openqa.selenium.WebDriver;

import com.age.help.ImageScraper;
import com.age.help.PinUtils;
import com.age.pinterest.api.AccountManager;
import com.age.pinterest.api.ApiLogin;
import com.age.pinterest.config.PinterestAccount;

public class App {
	private static final String COCO = "globalamericase";
	private static final String LINDA = "linda1234willia";
	private static final String STACEY = "stacey123gray";
	private static final String COCO_BOARD = "dreamy-jewelry";
	private static final String LINDA_BOARD = "jewelry";
	private static final String STACEY_BOARD = "jewelry-that-i-would-like-to-wear";

	public static void main(String[] args) throws InterruptedException, IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
		// WebDriver driver = PinUtils.getChrome();
		// ImageScraper srapper = new ImageScraper(driver, "cool", "cool");
		// srapper.scan();
		// WebDriver driver = PinUtils.getPhantomDriver();
		PinterestAccount acc = new PinterestAccount();
		acc.setEmail("globalamericaselfdefensejohn@gmail.com");
		acc.setPassword("Geni0us!");
		acc.setUser("globalamericase");
		AccountManager manager = new AccountManager(acc, PinUtils.getChrome());
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		System.setProperty("https.proxyPort", "8888");
		manager.pin();
		// ApiLogin.login();
		// manager.follow();
		// PinBot bot = new PinBot(driver, COCO);
		// bot.addPinTask(COCO_BOARD, 1000 * 60 * 40);
		// bot.start();
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
