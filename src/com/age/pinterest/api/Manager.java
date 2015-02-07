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
import com.age.help.PinUtils;
import com.age.pinterest.config.PinterestAccount;

public class Manager {
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0";
	private final PinterestAccount account;
	private final WebDriver driver;

	public Manager(PinterestAccount account, WebDriver driver) {
		this.account = account;
		this.driver = driver;
		manageSsl();
	}

	public List<Pinner> getFollowList(int size, String keyword) throws ClientProtocolException, IOException, JSONException {
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
			List<Pinner> pinners = getFollowerList(usr, size - followList.size());
			for (Pinner p : pinners) {
				if (!p.getFollowed()) {
					followList.add(p);
				}
			}
			System.out.println("Follow list is:  " + followList.size());
		}
		return followList;
	}

	private List<Pinner> getFollowerList(String user, int max) throws JSONException, IOException {
		ArrayList<Pinner> followers = new ArrayList<Pinner>();
		String bookmark = "";
		while (!bookmark.equals("-end-")) {
			System.out.println("Discovered " + followers.size() + "  followers from " + user);
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
						+ account.getUser()
						+ "%2C+invite_code%3Dnull))%3EFollowingSwitcher()%3EButton(class_name%3DnavScopeBtn%2C+text%3DPinners%2C+element_type%3Da%2C+rounded%3Dfalse)&_="
						+ num;
			} else {
				url = "https://www.pinterest.com/resource/UserFollowingResource/get/?source_url=%2F" + user
						+ "%2Ffollowing%2F&data=%7B%22options%22%3A%7B%22username%22%3A%22" + user + "%22%2C%22bookmarks%22%3A%5B%22"
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
					if (followers.size() > max) {
						return followers;
					}
					JSONObject current = arr.getJSONObject(i);
					boolean followed = Boolean.parseBoolean(current.getString("explicitly_followed_by_me"));
					int userFollowers = Integer.parseInt(current.getString("follower_count"));
					Pinner pinner = new Pinner();
					pinner.setFollowers(userFollowers);
					pinner.setFullName(current.getString("full_name"));
					pinner.setId(current.getString("id"));
					pinner.setUsername(current.getString("username"));
					pinner.setFollowed(followed);
					followers.add(pinner);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return followers;
	}

	public List<Pinner> getUnfollowList(int followers) throws IOException, JSONException, InterruptedException {
		ArrayList<Pinner> targets = new ArrayList<Pinner>();
		String bookmark = "";
		while (!bookmark.equals("-end-")) {
			System.out.println("Target list:  " + targets.size());
			try {
				String num = Long.toString(System.currentTimeMillis());
				String url = "";
				if (bookmark.isEmpty()) {
					url = "https://www.pinterest.com/resource/UserFollowingResource/get/?source_url=%2F"
							+ account.getUser()
							+ "%2Ffollowing%2F&data=%7B%22options%22%3A%7B%22username%22%3A%22"
							+ account.getUser()
							+ "%22%7D%2C%22context%22%3A%7B%7D%7D&module_path=App()%3EUserProfilePage(resource%3DUserResource(username%3D"
							+ account.getUser()
							+ "))%3EUserProfileContent(resource%3DUserResource(username%3D"
							+ account.getUser()
							+ "%2C+invite_code%3Dnull))%3EFollowingSwitcher()%3EButton(class_name%3DnavScopeBtn%2C+text%3DPinners%2C+element_type%3Da%2C+rounded%3Dfalse)&_="
							+ num;
				} else {
					url = "https://www.pinterest.com/resource/UserFollowingResource/get/?source_url=%2F" + account.getUser()
							+ "%2Ffollowing%2F&data=%7B%22options%22%3A%7B%22username%22%3A%22" + account.getUser()
							+ "%22%2C%22bookmarks%22%3A%5B%22" + bookmark
							+ "%3D%3D%22%5D%7D%2C%22context%22%3A%7B%7D%7D&module_path=App(module%3D%5Bobject+Object%5D)&_=" + num;
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
				// System.out.println(theString);
				JSONObject jsonObject = new JSONObject(theString);
				JSONObject resource = jsonObject.getJSONObject("resource");
				JSONObject options = resource.getJSONObject("options");
				JSONArray markArr = options.getJSONArray("bookmarks");
				bookmark = markArr.getString(0);

				JSONObject resp = jsonObject.getJSONObject("resource_response");
				JSONArray arr = resp.getJSONArray("data");
				for (int i = 1; i < arr.length(); i++) {
					try {
						JSONObject user = arr.getJSONObject(i);
						System.out.print(user.get("username") + "   --   ");
						int userFollowers = Integer.parseInt(user.getString("follower_count"));
						System.out.println(userFollowers);
						System.out.println();
						if (userFollowers < followers) {
							Pinner pinner = new Pinner();
							pinner.setFollowers(userFollowers);
							pinner.setFullName(user.getString("full_name"));
							pinner.setId(user.getString("id"));
							pinner.setUsername(user.getString("username"));
							targets.add(pinner);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return targets;
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

	private String getAllCookies() {
		WebDriver driver = PinUtils.getPhantomDriver();
		PinUtils.login(driver, account);
		String cookieList = "";
		for (Cookie cookie : driver.manage().getCookies()) {
			if (cookie.getName().equals("csrftoken") || cookie.getName().equals("_pinterest_sess")) {
				cookieList = cookieList + cookie.getName() + "=" + cookie.getValue() + "; ";
			}
		}
		driver.quit();
		return cookieList;
	}

	private void manageSsl() {
		String sslToken = account.getSslToken();
		System.out.println(sslToken);
		// TODO Remove when sslToken is passed sucesfully to the browser
		sslToken = null;
		if (sslToken == null || sslToken.isEmpty()) {
			sslToken = this.getSslToken();
			account.setSslToken(sslToken);
			ObjectMapper mapper = new ObjectMapper();
			try {
				mapper.writeValue(new File("D:\\PinBot\\" + account.getUser() + "\\acc.json"), account);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// else {
		// String[] parts = sslToken.split("=");
		// String name = parts[0];
		// String value = parts[1];
		// Cookie c = new Cookie(name, value);
		// System.out.println(c);
		// driver.manage().addCookie(c);
		// }
	}

	public void unfollow(String userId) throws ClientProtocolException, IOException {
		String url = "https://www.pinterest.com/?source_url%3D%2Flinda1234willia%2Ffollowing%2F%26data%3D%7B%22options%22%3A%7B%22user_id%22%3A%22"
				+ userId
				+ "%22%7D%2C%22context%22%3A%7B%7D%7D%26module_path%3DApp%28%29%3EUserProfilePage%28resource%3DUserResource%28username%3Dlinda1234willia%29%29%3EUserProfileContent%28resource%3DUserResource%28username%3Dlinda1234willia%29%29%3EGrid%28resource%3DUserFollowingResource%28username%3Dlinda1234willia%29%29%3EGridItems%28resource%3DUserFollowingResource%28username%3Dlinda1234willia%29%29%3EUser%28resource%3DUserResource%28username%3Dsamie71999%29%29%3EUserFollowButton%28followed%3Dtrue%2C+class_name%3DgridItem%2C+unfollow_text%3DUnfollow%2C+follow_ga_category%3Duser_follow%2C+unfollow_ga_category%3Duser_unfollow%2C+disabled%3Dfalse%2C+is_me%3Dfalse%2C+follow_class%3Ddefault%2C+log_element_type%3D62%2C+text%3DUnfollow%2C+user_id%3D"
				+ userId + "%2C+follow_text%3DFollow%2C+color%3Ddim%29";
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("User-Agent", USER_AGENT);
		httpGet.setHeader("X-NEW-APP", "1");
		httpGet.setHeader("Referer", "http://www.pinterest.com/");
		httpGet.setHeader("Origin", "http://www.pinterest.com/");
		httpGet.setHeader("X-CSRFToken", account.getSslToken());
		httpGet.setHeader("Accept-Language", "en-gb,en;q=0.5");
		httpGet.setHeader("X-Requested-With", "XMLHttpRequest");
		httpGet.setHeader("X-APP-VERSION", "0cc7472");
		httpGet.setHeader("X-NEW-APP", "1");
		httpGet.setHeader("Cookie", account.getSslToken() + ";");
		httpGet.setHeader("Accept-Encoding", "gzip, deflate");
		httpGet.setHeader("Host", "www.pinterest.com");
		httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = httpclient.execute(httpGet);
		System.out.println(response.getStatusLine());
		InputStream instream = response.getEntity().getContent();
		StringWriter writer = new StringWriter();

		IOUtils.copy(instream, writer, "utf-8");
		String theString = writer.toString();
		System.out.println(theString);

	}

	@SuppressWarnings("unchecked")
	private static void printKeys(JSONObject obj) {
		Iterator<String> iter = obj.keys();
		while (iter.hasNext()) {
			System.out.print(iter.next() + "   ");
		}
		System.out.println();
	}

}
