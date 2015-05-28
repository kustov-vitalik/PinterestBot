package com.age.help;

import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ImaginativePinGenerator {
	public static void main(String[] args) throws IOException, InterruptedException {
		ArrayList<String> pages = new ArrayList<String>();
		pages.add("http://imaginativeshoes.com/product-category/woman/womansshoes/sandals/");
		for (String page : pages) {
			try {
				processPage(page);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	private static final void processPage(String page) throws IOException {
		int i = 1;
		while (true) {
			String srt = page + "page/" + Integer.toString(i) + "/";
			HttpURLConnection con = (HttpURLConnection) new URL(srt).openConnection();
			con.setRequestMethod("GET");
			con.connect();
			StringWriter writer = new StringWriter();
			IOUtils.copy(con.getInputStream(), writer, "utf-8");
			String theString = writer.toString();
			i++;

			Document doc = Jsoup.parse(theString);

			ArrayList<Element> lis = new ArrayList<Element>();
			for (Element e : doc.getAllElements()) {
				if (e.tagName().equals("li") && e.attr("class").equals("product-small  grid1 grid-normal")) {
					lis.add(e);
				}
			}
			ArrayList<String> productUrls = new ArrayList<String>();
			for (Element product : lis) {
				String productUrl = null;
				for (Element e : product.getAllElements()) {
					for (Element el : e.getAllElements()) {
						if (el.tagName().equals("div")
								&& el.attr("class").equals("add_to_cart_button product_type_variable add-to-cart-grid  clearfix")
								&& el.attr("rel").equals("nofollow")) {
							productUrl = el.attr("href");
						}
					}
					if (!productUrls.contains(productUrl)) {
						productUrls.add(productUrl);
					}
				}
			}
			for (String url : productUrls) {
				System.out.println(url);
				System.out.println(getItemImageUrl(url));
				System.out.println();
			}
		}
	}

	private static String getItemImageUrl(String itemUrl) throws IOException {
		HttpURLConnection con = (HttpURLConnection) new URL(itemUrl).openConnection();
		con.setRequestMethod("GET");
		con.connect();
		StringWriter writer = new StringWriter();
		IOUtils.copy(con.getInputStream(), writer, "utf-8");
		String theString = writer.toString();
		Document doc = Jsoup.parse(theString);
		for (Element e : doc.getAllElements()) {
			if (e.tagName().equals("img") && e.attr("itemprop").equals("image")) {
				return e.attr("src");
			}
		}
		return "Could not get Image url";
	}

}
