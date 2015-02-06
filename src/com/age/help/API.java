package com.age.help;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import com.age.pinterest.config.PinterestAccount;

public class API {
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0";

	private static final String email = "Linda1234Williams@gmail.com";
	private static final String pass = "iskamparola";

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException, JSONException, InterruptedException {
		WebDriver driver = PinUtils.getPhantomDriver();
		PinterestAccount acc = new PinterestAccount();
		acc.setEmail(email);
		acc.setPassword(pass);
		PinUtils.login(driver, acc);
		String cookieList = API.buildCookieString(driver.manage().getCookies());
		driver.quit();
		long first = 1423250578945l;
		String bookmark = "PyExMnxjZTU0MTkzYTA2OGNhYjVjMWRlMTE4MzkxMmYxYzMyYzBjNzQzZDlmZjQxNjkxZjc1YWMzYzZjNDRkZjJhMjA3";
		while (true) {
			String url = "https://www.pinterest.com/resource/UserFollowersResource/get/?source_url=%2Flinda1234willia%2Ffollowers%2F&data=%7B%22options%22%3A%7B%22username%22%3A%22linda1234willia%22%2C%22bookmarks%22%3A%5B%22"
					+ bookmark
					+ "%22%5D%7D%2C%22context%22%3A%7B%7D%7D&module_path=App()%3EUserProfilePage(resource%3DUserResource(username%3Dlinda1234willia))%3EUserInfoBar(tab%3Dfollowers%2C+spinner%3D%5Bobject+Object%5D%2C+resource%3DUserResource(username%3Dlinda1234willia%2C+invite_code%3Dnull))&_="
					+ Long.toString(first);

			HttpGet httpget = new HttpGet(url);
			httpget.setHeader("User-Agent", USER_AGENT);
			httpget.setHeader("X-NEW-APP", "1");
			httpget.setHeader("Referer", "https://www.pinterest.com");
			httpget.setHeader("Accept-Language", "en-gb,en;q=0.5");
			httpget.setHeader("X-Requested-With", "XMLHttpRequest");
			httpget.setHeader("X-APP-VERSION", "d79f67c");
			httpget.setHeader("Cookie", cookieList);
			httpget.setHeader("Accept-Encoding", "gzip, deflate");
			httpget.setHeader("Host", "www.pinterest.com");

			CloseableHttpClient httpclient = HttpClients.createDefault();
			CloseableHttpResponse response = httpclient.execute(httpget);
			InputStream instream = response.getEntity().getContent();
			StringWriter writer = new StringWriter();

			IOUtils.copy(instream, writer, "utf-8");
			String theString = writer.toString();

			JSONObject jsonObject = new JSONObject(theString);
			printKeys(jsonObject);
			JSONObject resource = jsonObject.getJSONObject("resource");
			JSONObject options = resource.getJSONObject("options");
			printKeys(options);
			JSONArray markArr = options.getJSONArray("bookmarks");
			bookmark = markArr.getString(0);
			System.out.println(bookmark);

			JSONObject resp = jsonObject.getJSONObject("resource_response");
			printKeys(resp);
			JSONArray arr = resp.getJSONArray("data");
			for (int i = 1; i < arr.length(); i++) {
				try {
					JSONObject cObject = arr.getJSONObject(i);
					FileUtill.appendToFile("D:\\acc.txt", cObject.get("username").toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			first++;
			// JSONArray cache = jsonObject.getJSONArray("resource_data_cache");
			// for (int i = 0; i < cache.length(); i++) {
			// System.out.println(i);
			// System.out.println(cache.get(i));
			// }
			// JSONObject tree = module.getJSONObject("tree");
			// JSONArray data = cache.getJSONArray("data");
			// for (int i = 1; i < data.length(); i++) {
			// JSONObject user = data.getJSONObject(i);
			// System.out.println(user.get("username"));
			// Iterator<String> iter = user.keys();
			// System.out.println("========================");
			// while (iter.hasNext()) {
			// System.out.print(iter.next() + "   ");
			// }
			// System.out.println("========================");
		}
	}

	private static void printKeys(JSONObject obj) {
		Iterator<String> iter = obj.keys();
		while (iter.hasNext()) {
			System.out.print(iter.next() + "   ");
		}
		System.out.println();
	}

	private static String buildCookieString(Set<Cookie> set) {
		String cookieList = "";
		for (Cookie cookie : set) {
			if (cookie.getName().equals("csrftoken") || cookie.getName().equals("_pinterest_sess")) {
				cookieList = cookieList + cookie.getName() + "=" + cookie.getValue() + "; ";
			}
		}
		return cookieList;
	}
}
