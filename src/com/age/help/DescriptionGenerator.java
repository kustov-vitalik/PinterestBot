package com.age.help;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.age.ui.Log;

public class DescriptionGenerator {

	public List<String> getQuotes(String keyword) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
		Log.log("Generating quotes for " + keyword);
		ArrayList<String> allQuotes = new ArrayList<String>();
		try {
			for (int i = 1; i <= 10; i++) {
				allQuotes.addAll(getQuotesOnPage(keyword, i));
			}
		} catch (Exception e) {
			Log.log("Failed when collecting quotes  " + e.getMessage());
		}
		return allQuotes;
	}

	private List<String> getQuotesOnPage(String word, int page) throws IOException {
		ArrayList<String> quotes = new ArrayList<String>();
		String url = "http://www.brainyquote.com/search_results.html?q=" + word + "&pg=" + Integer.toString(page);
		URL requestUrl = new URL(url);
		HttpURLConnection cox = (HttpURLConnection) requestUrl.openConnection();
		cox.setRequestProperty("Cache-Control", "max-age=0");
		cox.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		cox.setRequestProperty("Accept-Encoding", "text, deflate, sdch");
		cox.setRequestProperty("Accept-Language", "en-US,en;q=0.8,bg;q=0.6");
		cox.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36");
		cox.setRequestProperty("Host", "www.brainyquote.com");
		InputStream instream = cox.getInputStream();
		StringWriter writer = new StringWriter();

		IOUtils.copy(instream, writer, "utf-8");
		String theString = writer.toString();
		Document doc = Jsoup.parse(theString);

		for (Element node : doc.getAllElements()) {
			if (node.nodeName().equals("a")) {
				List<Attribute> attrs = node.attributes().asList();
				for (Attribute a : attrs) {
					if (a.getKey().equals("title") && a.getValue().equals("view quote") && attrs.size() == 3) {
						quotes.add(node.text());
						break;
					}
				}
			}
		}
		return quotes;
	}

}
