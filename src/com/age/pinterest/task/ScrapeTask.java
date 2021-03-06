package com.age.pinterest.task;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.age.help.BotPaths;
import com.age.param.ScrapeParam;
import com.age.ui.LogConsole;
import com.age.ui.UI;

public class ScrapeTask extends Task {
	private final ScrapeParam scrapeParam;
	private final String dowloadLocation;

	public ScrapeTask(ScrapeParam scrapeParam) {
		super("Scrape-" + scrapeParam.getKeyword());
		this.scrapeParam = scrapeParam;
		this.dowloadLocation = BotPaths.IMAGES_ROOT + scrapeParam.getTag();
		new LogConsole("Scrape-" + scrapeParam.getKeyword());
	}

	@Override
	public void run() {
		try {
			ArrayList<String> images = new ArrayList<String>();
			UI.syslog.log("Scraping for  " + scrapeParam.getKeyword());
			for (int i = 0; images.size() < scrapeParam.getCount(); i++) {
				List<String> newImages = scrapeIndex(scrapeParam.getKeyword(), i);
				if (newImages.isEmpty()) {
					UI.syslog.log("No more images for " + scrapeParam.getKeyword() + "  got " + images.size());
					break;
				}
				images.addAll(scrapeIndex(scrapeParam.getKeyword(), i));
				UI.syslog.log("Found  " + images.size() + " images.   Remaining  " + (scrapeParam.getCount() - images.size()));
			}
			UI.syslog.log("Downloading images for " + scrapeParam.getKeyword());
			downloadAll(images);
		} catch (Exception e) {
			UI.syslog.log("Failed when scraping ", e);
		}
		UI.syslog.log("Scrape completed");
	}

	private List<String> scrapeIndex(String keywords, int index) throws IOException {
		ArrayList<String> results = new ArrayList<String>();
		String googleSearchFormat = "https://www.google.bg/search?as_st=y&tbm=isch&hl=en&as_q=" + keywords.replace(" ", "+")
				+ "&as_epq=&as_oq=&as_eq=&cr=&as_sitesearch=&safe=images&tbs=isz:m&ijn=" + Integer.toString(index);
		URL requestUrl = new URL(googleSearchFormat);
		HttpURLConnection cox = (HttpURLConnection) requestUrl.openConnection();
		cox.setRequestProperty("Cache-Control", "max-age=0");
		cox.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		cox.setRequestProperty("Accept-Encoding", "text, deflate, sdch");
		cox.setRequestProperty("Accept-Language", "en-US,en;q=0.8,bg;q=0.6");
		cox.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36");
		InputStream instream = cox.getInputStream();
		StringWriter writer = new StringWriter();

		IOUtils.copy(instream, writer, "utf-8");
		String theString = writer.toString();
		Document doc = Jsoup.parse(theString);
		for (Element e : doc.getAllElements()) {
			if (e.nodeName().equals("a")) {
				String href = e.attr("href");
				if (href.contains("imgurl")) {
					String imgUrl = extractImg(e.attr("href"));
					if (!imgUrl.isEmpty()) {
						results.add(extractImg(imgUrl));
					}
				}
			}
		}
		return results;
	}

	@Override
	public TaskType getType() {
		return TaskType.SCRAPE;
	}

	private String extractImg(String url) throws UnsupportedEncodingException {
		String imgUrl = url;
		imgUrl = imgUrl.toLowerCase();
		imgUrl = imgUrl.replace("http://www.google.bg/imgres?imgurl=", "");
		String imgType = "";
		if (imgUrl.contains("jpg")) {
			imgType = "jpg";
		} else if (imgUrl.contains("png")) {
			imgType = "png";
		} else if (imgUrl.contains("jpeg")) {
			imgType = "jpeg";
		} else if (imgUrl.contains("gif")) {
			imgType = "gif";
		}
		if (imgType.isEmpty()) {
			UI.syslog.log("empty for   " + imgUrl);
			return "";
		}
		imgUrl = imgUrl.substring(0, imgUrl.indexOf(imgType) + imgType.length());
		imgUrl = java.net.URLDecoder.decode(imgUrl, "UTF-8");
		imgUrl = java.net.URLDecoder.decode(imgUrl, "UTF-8");
		imgUrl = java.net.URLDecoder.decode(imgUrl, "UTF-8");
		return imgUrl;
	}

	private void downloadAll(List<String> images) throws IOException {
		new File(dowloadLocation).mkdirs();
		ExecutorService pool = Executors.newFixedThreadPool(20);
		for (String str : images) {
			try {
				String imgDestination = this.cutImageUrl(str);
				pool.submit(new DownloadCallable(imgDestination));
			} catch (Exception e) {
				UI.syslog.log("Failed to start download", e);
			}
		}
		pool.shutdown();
		while (!pool.isTerminated()) {

		}
		UI.syslog.log("Scrape completed");
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
				BufferedImage image1 = null;
				URL url = new URL(imgDestination);
				image1 = ImageIO.read(url);
				if (image1 != null) {
					File file = new File(dowloadLocation + "/" + System.currentTimeMillis() + r.nextInt() + ".png");
					file.createNewFile();
					ImageIO.write(image1, "png", file);
					UI.syslog.log("Downloaded " + imgDestination);
				}
			} catch (Exception e) {
				UI.syslog.log("Failed to download:  " + imgDestination, e);
			}
			return "SCRAPE";
		}

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

}
