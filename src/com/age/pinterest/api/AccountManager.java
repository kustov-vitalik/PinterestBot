package com.age.pinterest.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
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

	private List<Pinner> getFollowers(String user, int max) {
		System.out.println("Getting followers for  " + user);
		ArrayList<Pinner> result = new ArrayList<Pinner>();
		String username = account.getUser();
		String bookmark = "";
		while (!bookmark.equals("-end-")) {
			try {
				System.out.println(result.size() + "  followers from " + user);
				String num = Long.toString(System.currentTimeMillis());
				String url = "";
				if (bookmark.isEmpty()) {
					url = "https://www.pinterest.com/resource/UserResource/get/?source_url=%2F"
							+ username
							+ "%2F&data=%7B%22options%22%3A%7B%22username%22%3A%22"
							+ username
							+ "%22%2C%22invite_code%22%3Anull%7D%2C%22context%22%3A%7B%7D%2C%22module%22%3A%7B%22name%22%3A%22UserProfileContent%22%2C%22options%22%3A%7B%22tab%22%3A%22followers%22%7D%7D%2C%22render_type%22%3A1%2C%22error_strategy%22%3A0%7D&module_path=App()%3EUserProfilePage(resource%3DUserResource(username%3D"
							+ username + "))%3EUserInfoBar(tab%3Dfollowers%2C+spinner%3D%5Bobject+Object%5D%2C+resource%3DUserResource(username%3D" + username
							+ "%2C+invite_code%3Dnull))&_=" + num;
				} else {
					url = "https://www.pinterest.com/resource/UserFollowersResource/get/?source_url=%2F" + username
							+ "%2Ffollowers%2F&data=%7B%22options%22%3A%7B%22username%22%3A%22" + username + "%22%2C%22bookmarks%22%3A%5B%22" + bookmark
							+ "%22%5D%7D%2C%22context%22%3A%7B%7D%7D&module_path=App()%3EUserProfilePage(resource%3DUserResource(username%3D" + username
							+ "))%3EUserInfoBar(tab%3Dfollowers%2C+spinner%3D%5Bobject+Object%5D%2C+resource%3DUserResource(username%3D" + username
							+ "%2C+invite_code%3Dnull))&_=" + num;
				}
				HttpGet httpget = new HttpGet(url);
				httpget.setHeader("User-Agent", USER_AGENT);
				httpget.setHeader("X-NEW-APP", "1");
				httpget.setHeader("Referer", "http://www.pinterest.com/");
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
				if (bookmark.isEmpty()) {
					JSONObject module = jsonObject.getJSONObject("module");
					JSONObject tree = module.getJSONObject("tree");
					JSONArray children = tree.getJSONArray("children");
					JSONObject empty = children.getJSONObject(0);
					JSONObject resource = empty.getJSONObject("resource");
					JSONObject options = resource.getJSONObject("options");
					JSONArray bookmarks = options.getJSONArray("bookmarks");
					bookmark = bookmarks.getString(0);
				} else {
					JSONObject resource_response = jsonObject.getJSONObject("resource_response");
					JSONArray data = resource_response.getJSONArray("data");
					for (int i = 0; i < data.length(); i++) {
						JSONObject obj = data.getJSONObject(i);
						String userStr = obj.getString("username");
						String full = obj.getString("full_name");
						int followers = Integer.parseInt(obj.getString("follower_count"));
						int pinCount = Integer.parseInt(obj.getString("pin_count"));
						String id = obj.getString("id");
						Pinner pinner = new Pinner();
						pinner.setFollowers(followers);
						pinner.setFullName(full);
						pinner.setId(id);
						pinner.setUsername(userStr);
						pinner.setPins(pinCount);
						if (!result.contains(pinner)) {
							result.add(pinner);
						}
						if (result.size() >= max) {
							System.out.println("Got  " + result.size() + "  followers from " + user);
							return result;
						}
					}
					JSONObject resource = jsonObject.getJSONObject("resource");
					JSONObject options = resource.getJSONObject("options");
					JSONArray bookmarks = options.getJSONArray("bookmarks");
					bookmark = bookmarks.getString(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Got  " + result.size() + "  followers from " + user);
		return result;
	}

	public List<Pinner> getPinnersByKeyword(int size, String keyword) {
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
				List<Pinner> pinners = this.getFollowed(usr, size - followList.size(), -1);
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

	public List<Pinner> getFollowList(int size) {
		String history = this.getFollowHistory();
		if (size > 1000) {
			size = 1000;
		}
		int targetCount = size;
		List<Pinner> userFollowers = this.getFollowers(account.getUser(), targetCount);
		ArrayList<Pinner> targets = new ArrayList<Pinner>();
		for (Pinner p : userFollowers) {
			List<Pinner> part = this.getFollowers(p.getUsername(), size - targets.size());
			for (Pinner pnr : part) {
				if (!history.contains(pnr.getUsername())) {
					targets.add(pnr);
				} else {
					System.out.println(pnr.getUsername() + "  was already followed");
				}
			}
			System.out.println("You have  " + targets.size() + "  targets");
		}
		return targets;

	}

	private List<Pinner> getFollowed(String user, int maxListSize, int minFollowers) {
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
		List<Pinner> pinners = this.getFollowed(account.getUser(), -1, -1);
		for (Pinner p : pinners) {
			FileUtill.appendToFile(String.format(FollowTask.PATH_TO_HISTORY_FORMAT, account.getUser()), p.getUsername());
		}
	}

	private String getFollowHistory() {
		String history = "";
		try {
			history = FileUtill.getFileContents(String.format(FollowTask.PATH_TO_HISTORY_FORMAT, account.getUser()));
		} catch (IOException e) {
			System.out.println("Failed to get history for  " + account.getUser());
			e.printStackTrace();
		}
		return history;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private void printKeys(JSONObject obj) {
		Iterator<String> iter = obj.keys();
		while (iter.hasNext()) {
			System.out.print(iter.next() + "   ");
		}
		System.out.println();
	}

	@SuppressWarnings({ "unused" })
	private void printJsonArray(JSONArray arr) {
		for (int i = 0; i < arr.length(); i++) {
			try {
				System.out.println(arr.get(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public List<Pinner> getUnfollowList(int followers) {
		return this.getFollowed(account.getUser(), -1, followers);
	}

	public void follow() throws ClientProtocolException, IOException, JSONException {
		String username = "Orientart";
		String id = "362680713650133627";
		String url = "https://www.pinterest.com/resource/UserFollowResource/create/";
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("User-Agent", USER_AGENT);
		httpPost.setHeader("X-NEW-APP", "1");
		httpPost.setHeader("Accept-Language", "en-gb,en;q=0.5");
		httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
		httpPost.setHeader("X-APP-VERSION", "f1ab27d");
		httpPost.setHeader("X-NEW-APP", "1");
		httpPost.setHeader("Cookie", account.getSslToken() + ";");
		httpPost.setHeader("Accept-Encoding", "gzip, deflate");
		httpPost.setHeader("Host", "www.pinterest.com");
		httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
		ArrayList<NameValuePair> postParameters;
		postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("source_url", "/bertyalvarez/"));
		postParameters.add(new BasicNameValuePair("data", "{\"options\":{\"user_id\":\"120893708655660286\"},\"context\":{}}"));
		postParameters
				.add(new BasicNameValuePair(
						"module_path",
						"App()>UserProfilePage(resource=UserResource(username=bertyalvarez))>UserProfileContent(resource=UserResource(username=bertyalvarez))>Grid(resource=UserFollowingResource(username=bertyalvarez))>GridItems(resource=UserFollowingResource(username=bertyalvarez))>User(resource=UserResource(username=bertyalvarez))>UserFollowButton(followed=false, class_name=gridItem, unfollow_text=Unfollow, follow_ga_category=user_follow, unfollow_ga_category=user_unfollow, disabled=false, is_me=false, follow_class=default, log_element_type=62, text=Follow, user_id=120893708655660286, follow_text=Follow, color=default)"));
		httpPost.setEntity(new UrlEncodedFormEntity(postParameters));

		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = httpclient.execute(httpPost);
		InputStream instream = response.getEntity().getContent();
		StringWriter writer = new StringWriter();

		IOUtils.copy(instream, writer, "utf-8");
		String theString = writer.toString();
		JSONObject jsonObject = new JSONObject(theString);
		System.out.println(theString);
		this.printKeys(jsonObject);
		JSONObject response_res = jsonObject.getJSONObject("resource_response");
		this.printKeys(response_res);
		String err = response_res.getString("error");
		System.out.println(err);
		// JSONArray error = response_res.getJSONArray("error");
		// this.printJsonArray(error);
		String data = response_res.getString("data");
		System.out.println(data);

	}
}
