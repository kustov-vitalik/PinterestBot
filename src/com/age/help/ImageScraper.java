package com.age.help;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ImageScraper {
	// private final ArrayList<String> images;
	private final WebDriver driver;
	private final String keywords;
	private final String dowloadLocation;

	public ImageScraper(WebDriver driver, String keywords, String tag) {
		this.driver = driver;
		this.keywords = keywords;
		this.dowloadLocation = BotPaths.IMAGES_DIR + tag;
		// images = new ArrayList<String>();
	}

	public String getDownloadLocation() {
		return this.dowloadLocation;
	}

	public void scrape() throws InterruptedException, IOException {
		ArrayList<String> images = new ArrayList<String>();
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
		long last = System.currentTimeMillis();
		int max = 500;
		for (int i = 0; i < max; i++) {
			try {
				WebElement btn = PinUtils.waitFor(By.id("irc_ra"), driver);
				WebElement div = btn.findElement(By.tagName("div"));
				div.click();
				images.add(driver.getCurrentUrl());
				if (System.currentTimeMillis() - last > 1000 * 1) {
					System.out.println((i * 100.0f) / max + "%");
					last = System.currentTimeMillis();
				}
			} catch (Exception e) {
			}
		}
		System.out.println("Downloading");
		driver.quit();
		downloadAll(images);

	}

	private void downloadAll(List<String> images) throws IOException {
		new File(dowloadLocation).mkdirs();
		ExecutorService pool = Executors.newFixedThreadPool(20);
		for (String str : images) {
			try {
				String imgDestination = this.cutImageUrl(str);
				pool.submit(new DownloadCallable(imgDestination));
			} catch (Exception e) {
				System.out.println("Failed to start download " + e.getMessage());
			}
		}
		pool.shutdown();
		while (!pool.isShutdown()) {

		}
		System.out.println("Scrape for  " + this.keywords + "  completed");
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

	private class DownloadCallable implements Callable<String> {
		private final String imgDestination;

		public DownloadCallable(String imgDestination) {
			this.imgDestination = imgDestination;
		}

		@Override
		public String call() {
			Random r = new Random();
			try {
				System.out.println("Downloading  " + imgDestination);
				BufferedImage image1 = null;
				URL url = new URL(imgDestination);
				image1 = ImageIO.read(url);
				if (image1 != null) {
					File file = new File(dowloadLocation + "/" + System.currentTimeMillis() + r.nextInt() + ".png");
					file.createNewFile();
					ImageIO.write(image1, "png", file);
					System.out.println("Downloaded " + imgDestination);
				}
			} catch (Exception e) {
				System.out.println("Failed to download:  " + imgDestination + "  " + e.getMessage());
			}
			return "OK";
		}

	}
}