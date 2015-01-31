package com.age.pinterest.bot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openqa.selenium.WebDriver;

import com.age.help.FileUtill;
import com.age.help.PinGenerator;
import com.age.help.PinUtils;
import com.age.pinterest.config.PinterestAccount;

public class App {

	public static void main(String[] args) throws InterruptedException, IOException {
//		if (args.length == 0 || args[0].isEmpty()) {
//			System.out.println("Invalid user");
//			return;
//		}
//		String user = args[0];
		WebDriver driver = PinUtils.getFirefoxDriver();
		PinBot bot = new PinBot(driver, "globalamericase");
		// bot.addPinTask("jewelry", 1000 * 60);
		bot.addUnfollowTask(7000);
		bot.start();

	}

	private static PinterestAccount getAccount(String jsonFile) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(new File(jsonFile), PinterestAccount.class);

	}

	private static void generatePins() throws JsonGenerationException, JsonMappingException, InterruptedException, IOException {
		WebDriver driver = PinUtils.getChrome();
		PinGenerator generator = new PinGenerator();
		ArrayList<String> categories = new ArrayList<String>();
		categories.add("necklaces");
		categories.add("pendants");
		categories.add("fascinators");
		categories.add("rings");
		categories.add("earrings");
		categories.add("cufflinks");
		categories.add("bracelets");
		categories.add("bellychains");
		categories.add("bangles");
		categories.add("Anklets");
		categories.add("amulets");
		for (String cat : categories) {
			generator.generatePin(cat);
		}
	}
}
