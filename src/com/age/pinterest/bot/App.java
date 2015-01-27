package com.age.pinterest.bot;

import java.io.IOException;

import org.openqa.selenium.WebDriver;

public class App {

	public static void main(String[] args) throws InterruptedException, IOException {
		 WebDriver driver = PinUtils.getChrome();
//		WebDriver driver = PinUtils.getPhantomDriver();
//		PinBot bot = new PinBot(driver, "Stacey123Gray@gmail.com", "iskamparola1");
//		bot.addFollowTask("D:\\Stacey.txt");
//		PinBot bot = new PinBot(driver, "globalamericaselfdefensejohn@gmail.com", "Geni0us!");
//		bot.addFollowTask("D:\\Coco.txt");
		PinBot bot = new PinBot(driver, "Linda1234Williams@gmail.com", "iskamparola");
		bot.addFollowTask("D:\\Linda.txt");
		
		// bot.addPinTask("D://configCoco.txt");
		bot.start();
	}
}
