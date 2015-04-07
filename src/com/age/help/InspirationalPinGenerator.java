package com.age.help;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.age.data.Pin;

public class InspirationalPinGenerator {
	public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {
		List<String> categories = Arrays.asList("casual", "wear-to-work", "special-occasion", "night-out-and-cocktail");

		DescriptionGenerator generator = new DescriptionGenerator();
		List<String> quotes = generator.getQuotes("dress");
		Iterator<String> iter = quotes.iterator();

		ObjectMapper mapper = new ObjectMapper();
		File root = new File("D:/pin");
		root.mkdirs();
		for (String category : categories) {
			List<String> items = getItems(category);
			int i = items.size();
			for (String url : items) {
				if (!iter.hasNext()) {
					iter = quotes.iterator();
				}
				System.out.println("Remaining for " + category + "  " + i);
				File subDir = new File(root, category);
				subDir.mkdirs();
				String imgUrl = getImageUrlForItem(url);
				Pin pin = new Pin();
				pin.setSource(url);
				pin.setImage(downloadUrl(imgUrl, subDir));
				pin.setDescription(iter.next());
				mapper.writeValue(new File(subDir, Long.toHexString(System.currentTimeMillis()) + ".json"), pin);
				i--;
			}
		}
	}

	private static String getImageUrlForItem(String itemUrl) throws IOException {
		HttpURLConnection con = (HttpURLConnection) new URL(itemUrl).openConnection();
		con.setRequestMethod("GET");
		con.connect();
		StringWriter writer = new StringWriter();
		IOUtils.copy(con.getInputStream(), writer, "utf-8");
		String theString = writer.toString();
		Document doc = Jsoup.parse(theString);
		for (Element e : doc.getAllElements()) {
			if (e.className().equals("item")) {
				for (Element el : e.children()) {
					if (el.tagName().equals("a") && el.className().equals("woocommerce-main-image zoom")) {
						return el.attr("href");
					}
				}
			}
		}
		return null;
	}

	private static List<String> getItems(String category) throws IOException {
		ArrayList<String> items = new ArrayList<String>();
		for (int i = 1; true; i++) {
			String url = "http://inspirationaldresses.com/product-category/" + category + "/page/" + Integer.toString(i) + "/";
			HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
			con.setRequestMethod("GET");
			con.connect();
			StringWriter writer = new StringWriter();
			try {
				IOUtils.copy(con.getInputStream(), writer, "utf-8");
			} catch (FileNotFoundException e) {
				System.out.println("No more pages");
				return items;
			}
			System.out.println("Response from getn pin " + con.getResponseMessage());
			con.disconnect();
			String theString = writer.toString();
			Document doc = Jsoup.parse(theString);
			for (Element e : doc.getAllElements()) {
				if (e.className().equals("kd_hp_item") && e.tagName().equals("div") && e.children().size() == 3) {
					for (Element sube : e.children()) {
						if (sube.className().equals("kd_hp_item_image")) {
							for (Element node : sube.children()) {
								if (node.tagName().equals("a")) {
									System.out.println(node.attr("href"));
									items.add(node.attr("href"));
								}
							}
						}
					}
				}
			}
		}
	}

	private static String downloadUrl(String urlParam, File root) throws IOException {
		Random r = new Random();
		BufferedImage image1 = null;
		URL url = new URL(urlParam);
		File file = null;
		image1 = ImageIO.read(url);
		if (image1 != null) {
			file = new File(root, System.currentTimeMillis() + r.nextInt() + ".png");
			file.createNewFile();
			ImageIO.write(image1, "png", file);
			System.out.println("Downloaded " + urlParam);
		}
		return file.getAbsolutePath();
	}
}
