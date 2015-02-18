package com.age.pinterest.api;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import com.age.data.Pinner;
import com.age.help.BotPaths;
import com.age.help.FileUtill;
import com.age.help.PinUtils;
import com.age.pinterest.config.Pin;
import com.age.pinterest.config.PinterestAccount;
import com.age.pinterest.task.FollowTask;

public class AccountManager {
	private final PinterestAccount account;
	private final WebDriver driver;
	Cookie bCookie = null;
	Cookie sslCookie = null;
	Cookie sessionCookie = null;

	public AccountManager(PinterestAccount account, WebDriver driver) {
		this.account = account;
		this.driver = driver;
		manageSsl();
	}

	public List<Pinner> getFollowers(String user, int max) {
		return ApiGetFollowers.getFollowers(user, max, account.getSslToken());
	}

	public List<Pinner> getFollowList(int size) {
		String history = this.getFollowHistory();
		int targetCount = size;
		if (size > 100) {
			targetCount = 100;
		}
		List<Pinner> userFollowers = ApiGetFollowers.getFollowers(account.getUser(), targetCount, account.getSslToken());
		ArrayList<Pinner> targets = new ArrayList<Pinner>();
		for (Pinner p : userFollowers) {
			int remaining = size - targets.size();
			if (remaining <= 0) {
				break;
			}
			List<Pinner> part = ApiGetFollowers.getFollowers(p.getUsername(), size - targets.size(), account.getSslToken());
			for (Pinner pnr : part) {
				if (!history.contains(pnr.getUsername())) {
					targets.add(pnr);
				} else {
					System.out.println(pnr.getUsername() + "  was already followed");
				}
			}
			System.out.println("You have  " + targets.size() + "  targets");
			if (targets.size() > size) {
				break;
			}
		}
		return targets;

	}

	private String getSslToken() {
		PinUtils.login(driver, account);
		String sslToken = "";
		for (Cookie cookie : driver.manage().getCookies()) {
			if (cookie.getName().equals("csrftoken")) {
				sslToken = sslToken + cookie.getName() + "=" + cookie.getValue() + "; ";
			} else if (cookie.getName().equals("_pinterest_sess")) {
				sslToken = sslToken + cookie.getName() + "=" + cookie.getValue() + "; ";
			}

		}
		bCookie = driver.manage().getCookieNamed("_b");
		sslCookie = driver.manage().getCookieNamed("csrftoken");
		sessionCookie = driver.manage().getCookieNamed("_pinterest_sess");
		driver.quit();
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
				mapper.writeValue(new File(BotPaths.ROOT_DIR + "/Users/" + account.getUser() + "/acc.json"), account);
			} catch (IOException e) {
				e.printStackTrace();
			}
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

	@SuppressWarnings("unchecked")
	static void printKeys(JSONObject obj) {
		Iterator<String> iter = obj.keys();
		while (iter.hasNext()) {
			System.out.print(iter.next() + "   ");
		}
		System.out.println();
	}

	static void printJsonArray(JSONArray arr) {
		for (int i = 0; i < arr.length(); i++) {
			try {
				System.out.println(arr.get(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public List<Pinner> getUnfollowList(int followers) {
		return ApiGetFollowed.getFollowed(account.getUser(), -1, followers, account.getSslToken());
	}

	public void follow(Pinner pinner) throws ClientProtocolException, IOException, JSONException, InterruptedException,
			NoSuchAlgorithmException, KeyManagementException {
		ApiFollow.follow(pinner, sslCookie, sessionCookie);
	}

	public void unfollow(Pinner pinner) throws KeyManagementException, ClientProtocolException, NoSuchAlgorithmException, IOException,
			JSONException, InterruptedException {
		ApiUnfollow.unfollow(account.getUser(), pinner, sslCookie, sessionCookie);
	}

	public void pin() throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		ApiPin.upload(null, null, null, sslCookie, sessionCookie, bCookie);
	}
}
