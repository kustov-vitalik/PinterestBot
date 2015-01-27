package com.age.pinterest.bot;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openqa.selenium.WebDriver;

import com.age.pinterest.config.PinterestAccount;

public class App {

	public static void main(String[] args) throws InterruptedException, IOException {
		WebDriver driver = PinUtils.getChrome();
		// WebDriver driver = PinUtils.getPhantomDriver();
		// PinBot bot = new PinBot(driver, "Stacey123Gray@gmail.com",
		// "iskamparola1");
		// bot.addFollowTask("D:\\Stacey.txt");
		// String path
		// PinBot bot = new PinBot(driver, getAccount("d:\\user.json"));
		// bot.addFollowTask("D:\\Coco.txt");
		PinterestAccount acc = new PinterestAccount();
		acc.setEmail("Linda1234Williams@gmail.com");
		acc.setPassword("iskamparola");
		acc.setUser("linda1234willia");
		PinBot bot = new PinBot(driver, acc);
		bot.addFollowTask("D:\\Linda.txt");
		// bot.addPinTask("D://configCoco.txt");
		bot.start();
		// EeryJwelryPin pin = new EeryJwelryPin(driver, "dreamy-jewelry");
		// pin.generatePin();
		// pin.startPining();
	}

	private static PinterestAccount getAccount(String jsonFile) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(new File(jsonFile), PinterestAccount.class);

	}

	private static void wirte() throws JsonGenerationException, JsonMappingException, IOException {
		PinterestAccount account = new PinterestAccount();
		account.setEmail("email!!!");
		account.setPassword("pass!!!!!!!!");
		account.setUser("global");
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File("d:\\user.json"), account);
	}
}
