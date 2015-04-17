package com.age.pinterest.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.age.data.Pin;
import com.age.help.BotPaths;
import com.age.help.FileUtill;
import com.age.param.GeneratePinsParam;
import com.age.ui.UI;

public class GenerateBasicPinsTask extends Task {
	private final GeneratePinsParam generateParam;

	public GenerateBasicPinsTask(GeneratePinsParam generateParam) {
		super("GeneratePinsLog");
		this.generateParam = generateParam;
	}

	@Override
	public void run() {
		try {
			UI.syslog.log("Generating pins for " + generateParam.getTag());
			List<String> imagePaths = FileUtill.getAllFiles(BotPaths.IMAGES_ROOT + generateParam.getTag());
			List<String> quotes = this.getQuotes(generateParam.getTag());
			UI.syslog.log("Will generate " + imagePaths.size() + " pins");
			Iterator<String> imageIter = imagePaths.iterator();
			Iterator<String> quoteIter = quotes.iterator();
			ObjectMapper mapper = new ObjectMapper();
			Random r = new Random();
			while (imageIter.hasNext()) {
				if (!quoteIter.hasNext()) {
					quoteIter = quotes.iterator();
				}
				String imagePath = imageIter.next();
				String quote = quoteIter.next();
				Pin pin = new Pin();
				pin.setDescription(quote);
				pin.setImage(imagePath);
				pin.setSource(generateParam.getSource());
				String tagDir = BotPaths.PINS_ROOT + generateParam.getTag();
				File pathDirFile = new File(tagDir);
				pathDirFile.mkdirs();
				File jsonFile = new File(pathDirFile, r.nextInt() + ".json");
				mapper.writeValue(jsonFile, pin);
			}
		} catch (Exception e) {
			UI.syslog.log("Failed to generate pins", e);
			e.printStackTrace();
		}
		UI.syslog.log("Done generating pins for  " + generateParam.getTag());
	}

	@Override
	public TaskType getType() {
		return TaskType.GENERATE_PIN;
	}

	public List<String> getQuotes(String keyword) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
		UI.syslog.log("Generating quotes for " + keyword);
		ArrayList<String> allQuotes = new ArrayList<String>();
		try {
			for (int i = 1; i <= 10; i++) {
				allQuotes.addAll(getQuotesOnPage(keyword, i));
			}
		} catch (Exception e) {
			UI.syslog.log("Failed when collecting quotes", e);
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
