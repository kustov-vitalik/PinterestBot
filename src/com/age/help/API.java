package com.age.help;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import com.age.pinterest.config.PinterestAccount;

public class API {
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0";

	private static final String email = "Linda1234Williams@gmail.com";
	private static final String pass = "iskamparola";

	public static void main(String[] args) throws IOException {
		WebDriver driver = PinUtils.getChrome();
		PinterestAccount acc = new PinterestAccount();
		acc.setEmail(email);
		acc.setPassword(pass);
		PinUtils.login(driver, acc);
		String cookieList = API.buildCookieString(driver.manage().getCookies());
		driver.quit();

		String url = "https://www.pinterest.com/resource/UserFollowingResource/get/?source_url=%2Flinda1234willia%2Ffollowing%2F&data=%7B%22options%22%3A%7B%22username%22%3A%22linda1234willia%22%7D%2C%22context%22%3A%7B%7D%2C%22module%22%3A%7B%22name%22%3A%22Grid%22%2C%22options%22%3A%7B%22scrollable%22%3Atrue%2C%22show_grid_footer%22%3Afalse%2C%22centered%22%3Atrue%2C%22reflow_all%22%3Atrue%2C%22virtualize%22%3Atrue%2C%22item_options%22%3A%7B%7D%2C%22layout%22%3A%22fixed_height%22%7D%7D%2C%22render_type%22%3A1%2C%22error_strategy%22%3A0%7D&module_path=App()%3EUserProfilePage(resource%3DUserResource(username%3Dlinda1234willia))%3EUserProfileContent(resource%3DUserResource(username%3Dlinda1234willia))%3EFollowingSwitcher()%3EButton(class_name%3DnavScopeBtn%2C+text%3DPinners%2C+element_type%3Da%2C+rounded%3Dfalse)&_=1423166393196";

		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("User-Agent", USER_AGENT);
		httpget.setHeader("X-NEW-APP", "1");
		httpget.setHeader("Referer", "http://www.pinterest.com/linda1234willia/following/");
		httpget.setHeader("Accept-Language", "en-gb,en;q=0.5");
		httpget.setHeader("X-Requested-With", "XMLHttpRequest");
		httpget.setHeader("X-APP-VERSION", "d79f67c");
		httpget.setHeader("Cookie", cookieList);
		httpget.setHeader("Accept-Encoding", "gzip, deflate");
		httpget.setHeader("Host", "www.pinterest.com");

		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = httpclient.execute(httpget);
		System.out.println(response.getEntity().getContentType().getName());
		System.out.println(response.getEntity().getContentType().getValue());
		InputStream instream = response.getEntity().getContent();
		StringWriter writer = new StringWriter();
		IOUtils.copy(instream, writer, "utf-8");
		String theString = writer.toString();
		System.out.println(theString.length());
		System.out.println(theString);
	}

	private static String buildCookieString(Set<Cookie> set) {
		String cookieList = "";
		for (Cookie cookie : set) {
			cookieList = cookieList + cookie.getName() + "=" + cookie.getValue() + "; ";
		}
		return cookieList;
	}
}
