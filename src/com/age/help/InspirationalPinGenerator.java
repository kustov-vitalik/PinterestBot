package com.age.help;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class InspirationalPinGenerator {
	public static void main(String[] args) throws MalformedURLException, IOException {
		// night-out-and-cocktail
		List<String> items = getItems("night-out-and-cocktail");
		for (String url : items) {
			System.out.println("Location " + url);
			System.out.println("Image " + getUrlForItem(url));
		}

	}

	private static String getUrlForItem(String itemUrl) throws IOException {
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
}
