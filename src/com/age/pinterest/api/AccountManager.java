package com.age.pinterest.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import com.age.data.Pinner;
import com.age.help.FileUtill;
import com.age.help.PinUtils;
import com.age.pinterest.bot.PinBot;
import com.age.pinterest.config.PinterestAccount;
import com.age.pinterest.task.FollowTask;

public class AccountManager {
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0";

	private final PinterestAccount account;
	private final WebDriver driver;

	public AccountManager(PinterestAccount account, WebDriver driver) {
		this.account = account;
		this.driver = driver;
		manageSsl();
	}

	public List<Pinner> getFollowList(int size, String keyword) {
		System.out.println("Collection users related to  " + keyword);
		ArrayList<Pinner> followList = new ArrayList<Pinner>();
		String url = "https://www.pinterest.com/search/boards/?q=" + keyword;
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("User-Agent", USER_AGENT);
		httpget.setHeader("X-NEW-APP", "1");
		httpget.setHeader("Referer", "http://www.pinterest.com/" + account.getUser() + "/following/");
		httpget.setHeader("Accept-Language", "en-gb,en;q=0.5");
		httpget.setHeader("X-Requested-With", "XMLHttpRequest");
		httpget.setHeader("X-APP-VERSION", "0cc7472");
		httpget.setHeader("X-NEW-APP", "1");
		httpget.setHeader("Cookie", account.getSslToken() + ";");
		httpget.setHeader("Accept-Encoding", "gzip, deflate");
		httpget.setHeader("Host", "www.pinterest.com");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			CloseableHttpResponse response = httpclient.execute(httpget);
			InputStream instream = response.getEntity().getContent();
			StringWriter writer = new StringWriter();

			IOUtils.copy(instream, writer, "utf-8");
			String theString = writer.toString();
			JSONObject jsonObject = new JSONObject(theString);
			JSONObject mod = jsonObject.getJSONObject("module");
			JSONObject tree = mod.getJSONObject("tree");
			JSONObject data = tree.getJSONObject("data");
			JSONArray results = data.getJSONArray("results");
			System.out.println("Found " + results.length() + "  users");
			for (int i = 1; i < results.length(); i++) {
				if (followList.size() > size) {
					break;
				}

				JSONObject user = results.getJSONObject(i);
				JSONObject owner = user.getJSONObject("owner");
				String usr = owner.getString("username");

				List<Pinner> pinners = this.getList(usr, size - followList.size(), -1);
				for (Pinner p : pinners) {
					followList.add(p);
				}
				System.out.println("Follow list is:  " + followList.size());
			}
			return followList;
		} catch (Exception e) {
			System.out.println("Account manager failed to get follow list. It will probably return empty list.");
			e.printStackTrace();
		}
		return new ArrayList<Pinner>();
	}

	private List<Pinner> getList(String user, int maxListSize, int minFollowers) {
		List<Pinner> resultList = new ArrayList<Pinner>();
		String bookmark = "";
		while (!bookmark.equals("-end-")) {
			System.out.println("Target list:  " + resultList.size());
			try {
				String num = Long.toString(System.currentTimeMillis());
				String url = "";
				if (bookmark.isEmpty()) {
					url = "https://www.pinterest.com/resource/UserFollowingResource/get/?source_url=%2F"
							+ user
							+ "%2Ffollowing%2F&data=%7B%22options%22%3A%7B%22username%22%3A%22"
							+ user
							+ "%22%7D%2C%22context%22%3A%7B%7D%7D&module_path=App()%3EUserProfilePage(resource%3DUserResource(username%3D"
							+ user
							+ "))%3EUserProfileContent(resource%3DUserResource(username%3D"
							+ user
							+ "%2C+invite_code%3Dnull))%3EFollowingSwitcher()%3EButton(class_name%3DnavScopeBtn%2C+text%3DPinners%2C+element_type%3Da%2C+rounded%3Dfalse)&_="
							+ num;
				} else {
					url = "https://www.pinterest.com/resource/UserFollowingResource/get/?source_url=%2F" + account.getUser()
							+ "%2Ffollowing%2F&data=%7B%22options%22%3A%7B%22username%22%3A%22" + account.getUser() + "%22%2C%22bookmarks%22%3A%5B%22"
							+ bookmark + "%3D%3D%22%5D%7D%2C%22context%22%3A%7B%7D%7D&module_path=App(module%3D%5Bobject+Object%5D)&_=" + num;
				}
				HttpGet httpget = new HttpGet(url);
				httpget.setHeader("User-Agent", USER_AGENT);
				httpget.setHeader("X-NEW-APP", "1");
				httpget.setHeader("Referer", "http://www.pinterest.com/" + account.getUser() + "/following/");
				httpget.setHeader("Accept-Language", "en-gb,en;q=0.5");
				httpget.setHeader("X-Requested-With", "XMLHttpRequest");
				httpget.setHeader("X-APP-VERSION", "0cc7472");
				httpget.setHeader("X-NEW-APP", "1");
				httpget.setHeader("Cookie", account.getSslToken() + ";");
				httpget.setHeader("Accept-Encoding", "gzip, deflate");
				httpget.setHeader("Host", "www.pinterest.com");

				CloseableHttpClient httpclient = HttpClients.createDefault();
				CloseableHttpResponse response = httpclient.execute(httpget);
				InputStream instream = response.getEntity().getContent();
				StringWriter writer = new StringWriter();

				IOUtils.copy(instream, writer, "utf-8");
				String theString = writer.toString();
				JSONObject jsonObject = new JSONObject(theString);
				JSONObject resource = jsonObject.getJSONObject("resource");
				JSONObject options = resource.getJSONObject("options");
				JSONArray markArr = options.getJSONArray("bookmarks");
				bookmark = markArr.getString(0);

				JSONObject resp = jsonObject.getJSONObject("resource_response");
				JSONArray arr = resp.getJSONArray("data");
				for (int i = 1; i < arr.length(); i++) {
					try {
						if (maxListSize > 0 && resultList.size() > maxListSize) {
							return resultList;
						}
						JSONObject root = arr.getJSONObject(i);
						System.out.print(root.get("username") + "   --   ");
						int userFollowers = Integer.parseInt(root.getString("follower_count"));
						System.out.println(userFollowers);
						if (minFollowers < 0 || userFollowers < minFollowers) {
							Pinner pinner = new Pinner();
							pinner.setFollowers(userFollowers);
							pinner.setFullName(root.getString("full_name"));
							pinner.setId(root.getString("id"));
							pinner.setUsername(root.getString("username"));
							resultList.add(pinner);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return resultList;
	}

	public String getSslToken() {
		PinUtils.login(driver, account);
		String sslToken = "";
		for (Cookie cookie : driver.manage().getCookies()) {
			if (cookie.getName().equals("csrftoken")) {
				sslToken = cookie.getName() + "=" + cookie.getValue();
			}
		}
		return sslToken;
	}

	private void manageSsl() {
		String sslToken = account.getSslToken();
		System.out.println(sslToken);
		// TODO Remove when sslToken is passed successfully to the browser
		sslToken = null;
		if (sslToken == null || sslToken.isEmpty()) {
			sslToken = this.getSslToken();
			account.setSslToken(sslToken);
			ObjectMapper mapper = new ObjectMapper();
			try {
				mapper.writeValue(new File(PinBot.ROOT_DIR + "/Users/" + account.getUser() + "/acc.json"), account);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void buildFollowHistory() throws ClientProtocolException, IOException, JSONException {
		List<Pinner> pinners = this.getList(account.getUser(), -1, -1);
		for (Pinner p : pinners) {
			FileUtill.appendToFile(String.format(FollowTask.PATH_TO_HISTORY_FORMAT, account.getUser()), p.getUsername());
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private static void printKeys(JSONObject obj) {
		Iterator<String> iter = obj.keys();
		while (iter.hasNext()) {
			System.out.print(iter.next() + "   ");
		}
		System.out.println();
	}

	public List<Pinner> getUnfollowList(int followers) {
		return this.getList(account.getUser(), -1, followers);
	}

}
