package com.age.pinterest.bot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ImageScraper {
	private static final String IMAGE_LOCATION = "D://image//";
	private final ArrayList<String> images;
	private final WebDriver driver;
	private final String keywords;
	private final String tag;
	private final String dowloadLocation;

	public ImageScraper(WebDriver driver, String keywords, String tag) {
		this.driver = driver;
		this.keywords = keywords;
		this.tag = tag;
		this.dowloadLocation = IMAGE_LOCATION + tag;
		images = new ArrayList<String>();
	}

	public String getDownloadLocation() {
		return this.dowloadLocation;
	}

	public void scan() throws InterruptedException, IOException {
		// if (!checkForTag()) {
		// return;
		// }
		String googleSearchFormat = "https://www.google.bg/search?as_st=y&tbm=isch&hl=en&as_q=" + keywords.replace(" ", "+")
				+ "&as_epq=&as_oq=&as_eq=&cr=&as_sitesearch=&safe=images&tbs=isz:l";
		driver.get(googleSearchFormat);
		System.out.println("Scanning for  " + keywords);
		WebElement root = PinUtils.waitFor(By.id("search"), driver);
		List<WebElement> list = root.findElements(By.cssSelector("*"));
		for (WebElement e : list) {
			Thread.sleep(400);
			if (e.getTagName().toLowerCase().contains("img")) {
				e.click();
				break;
			}
		}
		PinUtils.waitForPage(driver);
		for (int i = 0; i < 2000; i++) {
			try {
				System.out.println("Loop");
				WebElement btn = PinUtils.waitFor(By.id("irc_ra"), driver);
				WebElement div = btn.findElement(By.tagName("div"));
				div.click();
				addImage(driver.getCurrentUrl());
			} catch (Exception e) {
				System.out.println("Skip click");
				i--;
			}
		}
		System.out.println("Downloading");
		downloadAll();

	}

	private void downloadAll() throws IOException {
		File root = new File(dowloadLocation);
		if (!root.exists()) {
			root.mkdir();
		}
		for (String str : images) {
			String imgDestination = "not yet set!";
			try {
				imgDestination = this.cutImageUrl(str);
				System.out.println("Downloading  " + imgDestination);
				BufferedImage image1 = null;

				URL url = new URL(imgDestination);
				image1 = ImageIO.read(url);
				if (image1 != null) {
					File file = new File(dowloadLocation + "/" + System.currentTimeMillis() + ".jpg");
					file.createNewFile();
					ImageIO.write(image1, "jpg", file);
				}
			} catch (Exception e) {
				System.out.println("Failed to download:  " + imgDestination + "  " + e.getMessage());
			}
		}
		System.out.println("Go to  " + dowloadLocation + "   and remove ugly images.");
	}

	private String cutImageUrl(String url) throws UnsupportedEncodingException {
		String str = url;
		str = str.replace("https://www.google.bg/search", "");
		int start = str.indexOf("https");
		if (start == -1) {
			start = str.indexOf("http");
		}
		int end = -1;
		String tmpStr = str.toLowerCase();
		if (tmpStr.contains("gif")) {
			end = tmpStr.indexOf("gif");
		} else if (tmpStr.contains("jpg")) {
			end = tmpStr.indexOf("jpg");
		} else if (tmpStr.contains("png")) {
			end = tmpStr.indexOf("png");
		}
		str = str.substring(start, end + 3);
		String result = java.net.URLDecoder.decode(str, "UTF-8");
		result = java.net.URLDecoder.decode(result, "UTF-8");
		return result;

	}

	private boolean checkForTag() {
		File f = new File(dowloadLocation);
		if (f.exists()) {
			Scanner in = new Scanner(System.in);
			System.out.println(tag + " already has some files add more enter 'n' to skip or any key to continue.");
			String line = in.nextLine();
			in.close();
			if (line.toLowerCase().equals("n")) {
				return false;
			}
		}
		return true;
	}

	private void addImage(String image) {
		if (image != null && !images.contains(image)) {
			images.add(image);
		}
	}
}