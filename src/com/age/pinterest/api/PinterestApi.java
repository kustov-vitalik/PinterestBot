package com.age.pinterest.api;

import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.age.data.Board;
import com.age.data.Cookie;
import com.age.data.Cookies;
import com.age.data.Pin;
import com.age.data.Pinner;
import com.age.data.PinterestAccount;
import com.age.data.User;
import com.age.pinterest.bot.PinBot;
import com.age.ui.Log;

public class PinterestApi {
	private static final int WAVE_FOLLOW_USERS_NUM = 100;
	private static final String CRLF = "";
	private User user;
	private final Proxy proxy = Proxy.NO_PROXY;

	public PinterestApi(PinterestAccount account) {
		Validate.notNull(account);
		this.user=PinBot.getUser(account.getUser());
		this.user = setUpUser(account);

	}

	public PinterestApi(User user) {
		Validate.notNull(user);
		this.user = user;
	}

	public void follow(Pinner target) {
		Log.log("Following  " + target.getUsername());
		try {
			String urlParameters = UrlProvider.getFollow(target.getUsername(), target.getId());
			byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
			int postDataLength = postData.length;
			String request = "https://www.pinterest.com/resource/UserFollowResource/create/";
			URL url = new URL(request);
			HttpURLConnection con = (HttpURLConnection) url.openConnection(proxy);
			CommonHeaders.addCommonHeaders(con, user.getCookies());
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			con.setRequestProperty("Content-Length", Integer.toString(postDataLength));

			try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				wr.write(postData);
			}
			con.connect();
			Log.log("Response code from follow  " + con.getResponseCode());
			con.disconnect();
		} catch (Exception e) {
			Log.log("Failed when following " + e.getMessage());
		}

	}

	public List<Board> getBoards() {
		List<Board> boards = new ArrayList<Board>();
		Log.log("Getting boards for user " + user.getAccount().getUser());
		try {
			URL requestUrl = new URL(UrlProvider.getBoards(user.getAccount().getUser()));
			HttpURLConnection con = (HttpURLConnection) requestUrl.openConnection(proxy);
			CommonHeaders.addCommonHeaders(con, user.getCookies());
			con.setRequestMethod("GET");
			StringWriter writer = new StringWriter();
			IOUtils.copy(con.getInputStream(), writer, "utf-8");
			Log.log("Response code from get boards is " + con.getResponseCode());
			con.disconnect();
			String theString = writer.toString();
			JSONObject jsonObject = new JSONObject(theString);
			JSONObject module = jsonObject.getJSONObject("module");
			JSONObject tree = module.getJSONObject("tree");
			JSONArray children1 = tree.getJSONArray("children");
			JSONObject children2 = children1.getJSONObject(0);
			JSONArray children3 = children2.getJSONArray("children");
			JSONObject children4 = children3.getJSONObject(0);
			JSONArray children5 = children4.getJSONArray("children");
			JSONObject childrenObj = children5.getJSONObject(0);
			JSONArray data = childrenObj.getJSONArray("data");
			for (int i = 0; i < data.length(); i++) {
				JSONObject aBoard = data.getJSONObject(i);
				try {
					boards.add(new Board(aBoard.getString("name"), aBoard.getString("id")));
				} catch (Exception e) {
					System.out.println("Some stupid bug in pinterest...");
				}
			}
		} catch (Exception e) {
			Log.log("Failed when getting boards " + e.getMessage());
		}
		return boards;
	}

	public List<Pinner> getFollowed(String target, int maxListSize, int minFollowers) {
		List<Pinner> resultList = new ArrayList<Pinner>();
		String bookmark = "";
		Log.log("Getting unfollow list for  " + target);
		while (!bookmark.equals("-end-")) {
			// TODO make it use timer
			if (resultList.size() % 10 == 0 && !resultList.isEmpty()) {
				Log.log("unfollow list size is " + resultList.size());
			}
			try {
				String url = "";
				if (bookmark.isEmpty()) {
					url = UrlProvider.getFollowedPre(target);
				} else {
					url = UrlProvider.getFollowedPost(target, bookmark);
				}
				URL requestUrl = new URL(url);
				HttpURLConnection con = (HttpURLConnection) requestUrl.openConnection(proxy);
				CommonHeaders.addCommonHeaders(con, user.getCookies());
				con.setRequestMethod("GET");

				StringWriter writer = new StringWriter();
				IOUtils.copy(con.getInputStream(), writer, "utf-8");
				con.disconnect();
				String theString = writer.toString();
				JSONObject jsonObject = new JSONObject(theString);
				JSONObject resource = jsonObject.getJSONObject("resource");
				JSONObject options = resource.getJSONObject("options");
				JSONArray markArr = options.getJSONArray("bookmarks");
				bookmark = markArr.getString(0);
				JSONObject resp = jsonObject.getJSONObject("resource_response");
				JSONArray arr = resp.getJSONArray("data");
				for (int i = 1; i < arr.length(); i++) {
					if (maxListSize > 0 && resultList.size() > maxListSize) {
						return resultList;
					}
					JSONObject root = arr.getJSONObject(i);
					int userFollowers = Integer.parseInt(root.getString("follower_count"));
					if (minFollowers < 0 || userFollowers < minFollowers) {
						Pinner pinner = new Pinner();
						pinner.setFollowers(userFollowers);
						pinner.setFullName(root.getString("full_name"));
						pinner.setId(root.getString("id"));
						pinner.setUsername(root.getString("username"));
						resultList.add(pinner);
					}
				}
			} catch (Exception e) {
				Log.log("Failed when getting followed " + e.getMessage());
			}
		}
		return resultList;
	}

	public List<Pinner> getFollowers(String target, int max) {
		Log.log("Getting followers from  " + target);
		ArrayList<Pinner> result = new ArrayList<Pinner>();
		if (max <= 0) {
			return result;
		}
		String bookmark = "";
		while (!bookmark.equals("-end-")) {
			try {
				String url = "";
				if (bookmark.isEmpty()) {
					url = UrlProvider.getFollowersPre(target);
				} else {
					url = UrlProvider.getFollowersPost(target, bookmark);
				}

				URL requestUrl = new URL(url);
				HttpURLConnection con = (HttpURLConnection) requestUrl.openConnection(proxy);
				CommonHeaders.addCommonHeaders(con, user.getCookies());
				con.setRequestMethod("GET");
				con.setUseCaches(false);

				StringWriter writer = new StringWriter();
				IOUtils.copy(con.getInputStream(), writer, "utf-8");
				String theString = writer.toString();
				JSONObject jsonObject = new JSONObject(theString);
				try {
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
							String username = obj.getString("username");
							String fullName = obj.getString("full_name");
							int followers = Integer.parseInt(obj.getString("follower_count"));
							int pinCount = Integer.parseInt(obj.getString("pin_count"));
							String id = obj.getString("id");
							Pinner pinner = new Pinner(username, followers, id, pinCount, fullName);
							if (!result.contains(pinner)) {
								result.add(pinner);
							}
							if (max >= 0 && result.size() >= max) {
								Log.log("Got " + result.size() + "  followers from " + target);
								return result;
							}
							JSONObject resource = jsonObject.getJSONObject("resource");
							JSONObject options = resource.getJSONObject("options");
							JSONArray bookmarks = options.getJSONArray("bookmarks");
							bookmark = bookmarks.getString(0);
						}
					}
				} catch (JSONException jsonE) {
					Log.log("No followers from  " + target);
					break;
				}
			} catch (Exception e) {
				Log.log("Error while getting followers  " + e.getMessage());
			}
		}
		Log.log("Got " + result.size() + "  followers from " + target);
		return result;
	}

	public List<Pinner> getPinnersByKeyword(String keyword, int max) {
		Log.log("Collection users related to  " + keyword);
		ArrayList<Pinner> followList = new ArrayList<Pinner>();
		String url = "https://www.pinterest.com/search/boards/?q=" + keyword;
		try {
			URL requestUrl = new URL(url);
			HttpURLConnection con = (HttpURLConnection) requestUrl.openConnection(proxy);
			CommonHeaders.addCommonHeaders(con, user.getCookies());
			con.setRequestMethod("GET");
			Log.log("Response code from Get pinners by keyword is " + con.getResponseMessage());

			InputStream instream = con.getInputStream();
			StringWriter writer = new StringWriter();

			IOUtils.copy(instream, writer, "utf-8");
			String theString = writer.toString();
			JSONObject jsonObject = new JSONObject(theString);
			JSONObject mod = jsonObject.getJSONObject("module");
			JSONObject tree = mod.getJSONObject("tree");
			JSONObject data = tree.getJSONObject("data");
			JSONArray results = data.getJSONArray("results");
			Log.log("Found " + results.length() + "  users");
			for (int i = 1; i < results.length(); i++) {
				if (followList.size() > max) {
					break;
				}
				JSONObject user = results.getJSONObject(i);
				JSONObject owner = user.getJSONObject("owner");
				String usr = owner.getString("username");
				List<Pinner> pinners = this.getFollowers(usr, max - followList.size());
				for (Pinner p : pinners) {
					followList.add(p);
				}
				Log.log("Follow list is:  " + followList.size());
			}
		} catch (Exception e) {
			Log.log("Account manager failed to get follow list. It will probably return empty list.  " + e.getMessage());
		}
		return followList;
	}

	public void pin(Pin pin, Board board) {
		String pinId = "";
		String image_url = this.upload(pin.getImage());
		String username = user.getAccount().getUser();
		String boardId = board.getId();
		String boardName = board.getName();
		Log.log("Pinning with user " + username + " to board " + boardName);
		String description = pin.getDescription();
		try {
			description = URLEncoder.encode(description, "UTF-8");
			String imageUrl = URLEncoder.encode(image_url, "UTF-8");
			String pinUrl = UrlProvider.getPin(username, boardName, description, boardId, imageUrl);
			byte[] postData = pinUrl.getBytes(Charset.forName("UTF-8"));
			int len = postData.length;
			String req = "https://www.pinterest.com/resource/PinResource/create/";
			HttpsURLConnection con = (HttpsURLConnection) new URL(req).openConnection(proxy);
			CommonHeaders.addCommonHeaders(con, user.getCookies());
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			con.setRequestProperty("Content-Length", Integer.toString(len));

			try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				wr.write(postData);
			}
			StringWriter writer = new StringWriter();
			IOUtils.copy(con.getInputStream(), writer, "utf-8");
			String theString = writer.toString();
			JSONObject root = new JSONObject(theString);
			JSONObject res = root.getJSONObject("resource_response");
			JSONObject data = res.getJSONObject("data");
			pinId = data.getString("id");
			Log.log("Response code from pin " + con.getResponseCode());
			con.disconnect();
		} catch (Exception e) {
			Log.log("Failed when pinning " + e.getMessage());
		}
		this.editPin(board, pinId, pin.getDescription(), pin.getSource());
	}

	public void unfollow(Pinner target) {
		try {
			String urlParam = UrlProvider.getUnfollow(user.getAccount().getUser(), target.getUsername(), target.getId());
			byte[] postData = urlParam.getBytes(Charset.forName("UTF-8"));
			int postDataLength = postData.length;
			String request = "https://www.pinterest.com/resource/UserFollowResource/delete/";
			URL url = new URL(request);
			HttpURLConnection con = (HttpURLConnection) url.openConnection(proxy);
			CommonHeaders.addCommonHeaders(con, user.getCookies());
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			con.setRequestProperty("Content-Length", Integer.toString(postDataLength));

			try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				wr.write(postData);
			}
			con.connect();
			Log.log(user.getAccount().getUser() + "  unfollowed  " + target.getUsername());
			con.disconnect();
		} catch (Exception e) {
			Log.log("Failed when unfollowing " + e.getMessage());
		}

	}

	public void editPin(Board board, String pinId, String description, String link) {
		String urlParams = UrlProvider.getEditPin(user.getAccount().getUser(), board.getName(), board.getId(), description, link, pinId);
		try {
			byte[] postData = urlParams.getBytes(Charset.forName("UTF-8"));
			int len = postData.length;
			String req = "https://www.pinterest.com/resource/PinResource/update/";
			HttpsURLConnection con = (HttpsURLConnection) new URL(req).openConnection(proxy);
			CommonHeaders.addCommonHeaders(con, user.getCookies());

			if (!con.usingProxy()) {
				System.out.println("No proxy On pin edit!!");
			}
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			con.setRequestProperty("Content-Length", Integer.toString(len));

			try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				wr.write(postData);
			}
			Log.log("Response code from edit " + con.getResponseCode());

			con.disconnect();
		} catch (Exception e) {
			Log.log("Failed on editing pin " + e.getMessage());
		}

	}

	public User getManagedUser() {
		return this.user;
	}

	public List<Pinner> getFollowList(int minListSize) {
		Log.log("Will get " + minListSize);
		List<Pinner> userFollowers = this.getFollowers(user.getAccount().getUser(), WAVE_FOLLOW_USERS_NUM);
		while (userFollowers.size() <= WAVE_FOLLOW_USERS_NUM) {
			List<Pinner> extraPinners = this.getPinnersByKeyword("fashion", WAVE_FOLLOW_USERS_NUM);
			userFollowers.addAll(extraPinners);
		}
		ArrayList<Pinner> targets = new ArrayList<Pinner>();
		for (Pinner p : userFollowers) {
			int remaining = minListSize - targets.size();
			if (remaining <= 0 || targets.size() > minListSize) {
				break;
			}
			targets.addAll(this.getFollowers(p.getUsername(), remaining));
		}
		return targets;
	}

	private User setUpUser(PinterestAccount acc) {
		this.user=PinBot.getUser(acc.getUser());
		user.setAccount(acc);
		user.setCookies(this.login());
		user.setBoards(this.getBoards());
		return user;
	}

	private Cookies login() {
		String url = "https://www.pinterest.com/login/";
		Log.log("Logging in with user " + user.getAccount().getEmail());
		Log.log("Password is  " + user.getAccount().getPassword());
		try {
			URL requestUrl = new URL(url);
			HttpURLConnection con = (HttpURLConnection) requestUrl.openConnection(proxy);
			String headerName = null;
			String sslHeader = "";
			String sessionHeader = "";
			for (int i = 1; (headerName = con.getHeaderFieldKey(i)) != null; i++) {
				if (headerName.equals("Set-Cookie")) {
					String cookie = con.getHeaderField(i);
					cookie = cookie.substring(0, cookie.indexOf(";"));
					if (cookie.contains("csrftoken")) {
						sslHeader = cookie;
					} else if (cookie.contains("_pinterest_sess")) {
						sessionHeader = cookie;
					}
				}
			}
			con.disconnect();

			String cookieList = sslHeader + "; " + sessionHeader;
			String sslValue = sslHeader.substring(sslHeader.indexOf("=") + 1);
			String urlParam = UrlProvider.getLogin(user.getAccount().getEmail(), user.getAccount().getPassword());
			url = "https://www.pinterest.com/resource/UserSessionResource/create/";
			byte[] postData = urlParam.getBytes(Charset.forName("UTF-8"));
			int postDataLength = postData.length;
			requestUrl = new URL(url);
			con = (HttpURLConnection) requestUrl.openConnection(proxy);
			CommonHeaders.addCommonHeaders(con);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			con.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			con.setRequestProperty("Cookie", cookieList);
			con.setRequestProperty("X-CSRFToken", sslValue);
			con.setRequestProperty("Referer", "https://www.pinterest.com/login");
			try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				wr.write(postData);
			}

			String sslTokenStr = "";
			String bTokenStr = "";
			String sessionTokenStr = "";
			for (int i = 1; (headerName = con.getHeaderFieldKey(i)) != null; i++) {
				if (headerName.equals("Set-Cookie")) {
					if (headerName.equals("Set-Cookie")) {
						String cookie = con.getHeaderField(i);
						cookie = cookie.substring(0, cookie.indexOf(";"));
						if (cookie.contains("csrftoken")) {
							sslTokenStr = cookie;
						} else if (cookie.contains("_pinterest_sess")) {
							sessionTokenStr = cookie;
						} else if (cookie.contains("_b=")) {
							bTokenStr = cookie;
						}

					}
				}
			}
			con.connect();
			Log.log("Response code from login is   " + con.getResponseCode());
			StringWriter writer = new StringWriter();

			IOUtils.copy(con.getInputStream(), writer, "utf-8");
			con.disconnect();

			Cookie sessionCookie = getCookieFromString(sessionTokenStr);
			Cookie sslCookie = getCookieFromString(sslTokenStr);
			Cookie bCookie = getCookieFromString(bTokenStr);
			Cookies cookieListResult = new Cookies();
			cookieListResult.setSessionCookie(sessionCookie);
			cookieListResult.setSslCookie(sslCookie);
			cookieListResult.setBCookie(bCookie);
			return cookieListResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.log("FAILED TO LOGIN");
		return null;
	}

	private static Cookie getCookieFromString(String cookieStr) {
		String value = cookieStr.substring(cookieStr.indexOf("=") + 1);
		String name = cookieStr.substring(0, cookieStr.indexOf("="));
		return new Cookie(name, value);
	}

	private String upload(String pathToImage) {
		Log.log("Uploading image");
		String image_url = "";
		try {
			File imageFile = new File(pathToImage);

			File img = new File(pathToImage);
			int postDataLength = Files.readAllBytes(Paths.get(pathToImage)).length;
			String request = "https://www.pinterest.com/upload-image/?img=" + imageFile.getName();
			URL url = new URL(request);
			Path path = Paths.get(pathToImage);
			byte[] data = Files.readAllBytes(path);
			String boundary = "---------------------------14956123715492";
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection(proxy);
			CommonHeaders.addCommonHeaders(con, user.getCookies());
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			con.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			con.setRequestProperty("X-File-Name", imageFile.getName());

			try (OutputStream output = con.getOutputStream(); OutputStreamWriter writer = new OutputStreamWriter(output)) {
				writer.append("--" + boundary).append(CRLF);
				writer.append("Content-Disposition: form-data; name=\"img\"; filename=\"" + img.getName() + "\"").append(CRLF);
				writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(img.getName())).append(CRLF);
				writer.append(CRLF).flush();
				output.write(data);
				writer.append("--" + boundary + "--").append(CRLF).flush();
			}
			con.connect();
			Log.log("Response code form upload  " + con.getResponseCode());
			StringWriter writer = new StringWriter();
			IOUtils.copy(con.getInputStream(), writer, "utf-8");
			String theString = writer.toString();
			JSONObject jsonObject = new JSONObject(theString);
			image_url = jsonObject.getString("image_url");
		} catch (Exception e) {
			Log.log("Failed on upload  " + e.getMessage());
		}
		return image_url;
	}
}
