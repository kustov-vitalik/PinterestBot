package com.age.pinterest.api;

import java.io.DataOutputStream;
import java.io.File;
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
import java.util.Iterator;
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
import com.age.help.FileLogger;

public class PinterestApi {
	private static final int WAVE_FOLLOW_USERS_NUM = 20;
	private static final String CRLF = "\r\n";
	private User user;
	private final Proxy proxy = Proxy.NO_PROXY;
	private final FileLogger logger;

	public PinterestApi(PinterestAccount account) {
		Validate.notNull(account);
		logger = new FileLogger(account.getUsername());
		this.user = setUpUser(account);
	}

	public PinterestApi(User user) {
		Validate.notNull(user);
		logger = new FileLogger(user.getAccount().getUsername());
		this.user = user;
	}

	public void follow(Pinner target) {
		logger.log("Following  " + target.getUsername());
		try {
			String urlParameters = UrlProvider.getFollow(target.getUsername(), target.getId());
			byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
			int postDataLength = postData.length;
			String request = "https://www.pinterest.com/resource/UserFollowResource/create/";
			URL url = new URL(request);
			HttpURLConnection con = (HttpURLConnection) url.openConnection(proxy);
			CommonHeaders.addCommonHeaders(con);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			con.setRequestProperty("Accept-Encoding", "json, deflate");
			con.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
			con.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			con.setRequestProperty("Cookie", user.getCookies().toString());
			con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			con.setRequestProperty("X-CSRFToken", user.getCookies().getSslCookie().getValue());
			con.setRequestProperty("Referer", "https://www.pinterest.com/" + user.getAccount().getUsername() + "/");
			try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				wr.write(postData);
			}
			con.connect();
			logger.log("Response code from follow  " + con.getResponseCode());
			con.disconnect();
		} catch (Exception e) {
			logger.log("Failed when following " + e.getMessage());
		}

	}

	public List<Board> getBoards(User targetUser) {
		List<Board> boards = new ArrayList<Board>();
		logger.log("Getting boards for user " + targetUser.getAccount().getUsername());
		try {
			URL requestUrl = new URL(UrlProvider.getBoards(targetUser.getAccount().getUsername()));
			HttpsURLConnection con = (HttpsURLConnection) requestUrl.openConnection(proxy);
			CommonHeaders.addCommonHeaders(con, targetUser.getCookies());
			con.setRequestMethod("GET");
			con.connect();
			logger.log("Response code from get boards is " + con.getResponseCode());
			JSONObject jsonObject = this.getJsonResponse(con);
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
			logger.log("Failed when getting boards " + e.getMessage());
		}
		return boards;
	}

	public List<Pinner> getFollowed(String target, int maxListSize, int minFollowers) {
		List<Pinner> resultList = new ArrayList<Pinner>();
		String bookmark = "";
		logger.log("Getting unfollow list for  " + target);
		while (!bookmark.equals("-end-")) {
			// TODO make it use timer
			if (resultList.size() % 10 == 0 && !resultList.isEmpty()) {
				logger.log("unfollow list size is " + resultList.size());
			}
			try {
				String url = "";
				if (bookmark.isEmpty()) {
					url = UrlProvider.getFollowedPre(target);
				} else {
					url = UrlProvider.getFollowedPost(target, bookmark);
				}
				URL requestUrl = new URL(url);
				HttpsURLConnection con = (HttpsURLConnection) requestUrl.openConnection(proxy);
				CommonHeaders.addCommonHeaders(con, user.getCookies());
				con.setRequestMethod("GET");
				JSONObject jsonObject = this.getJsonResponse(con);
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
				logger.log("Failed when getting followed " + e.getMessage());
			}
		}
		return resultList;
	}

	public List<Pinner> getFollowers(String target, int max) {
		logger.log("Getting followers from  " + target);
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
				HttpsURLConnection con = (HttpsURLConnection) requestUrl.openConnection(proxy);
				CommonHeaders.addCommonHeaders(con, user.getCookies());
				con.setRequestMethod("GET");
				JSONObject jsonObject = this.getJsonResponse(con);
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
								logger.log("Got " + result.size() + "  followers from " + target);
								return result;
							}
							JSONObject resource = jsonObject.getJSONObject("resource");
							JSONObject options = resource.getJSONObject("options");
							JSONArray bookmarks = options.getJSONArray("bookmarks");
							bookmark = bookmarks.getString(0);
						}
					}
				} catch (JSONException jsonE) {
					logger.log("No followers from  " + target);
					break;
				}
			} catch (Exception e) {
				logger.log("Error while getting followers  " + e.getMessage());
			}
		}
		logger.log("Got " + result.size() + "  followers from " + target);
		return result;
	}

	public List<Pinner> getPinnersByKeyword(String keyword, int max) {
		logger.log("Collection users related to  " + keyword);
		ArrayList<Pinner> followList = new ArrayList<Pinner>();
		String url = "https://www.pinterest.com/search/boards/?q=" + keyword;
		try {
			URL requestUrl = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) requestUrl.openConnection(proxy);
			CommonHeaders.addCommonHeaders(con, user.getCookies());
			con.setRequestMethod("GET");
			JSONObject jsonObject = this.getJsonResponse(con);
			JSONObject mod = jsonObject.getJSONObject("module");
			JSONObject tree = mod.getJSONObject("tree");
			JSONObject data = tree.getJSONObject("data");
			JSONArray results = data.getJSONArray("results");
			logger.log("Found " + results.length() + "  users");
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
				logger.log("Follow list is:  " + followList.size());
			}
		} catch (Exception e) {
			logger.log("Account manager failed to get follow list. It will probably return empty list.  " + e.getMessage());
		}
		return followList;
	}

	public void pin(Pin pin, Board board) {
		String pinId = "";
		String image_url = this.upload(pin.getImage());
		String username = user.getAccount().getUsername();
		String boardId = board.getId();
		String boardName = board.getName();
		logger.log("Pinning with user " + username + " to board " + boardName);
		String description = pin.getDescription();
		try {
			description = URLEncoder.encode(description, "UTF-8");
			String imageUrl = URLEncoder.encode(image_url, "UTF-8");
			String pinUrl = UrlProvider.getPin(username, boardName, description, boardId, imageUrl);
			byte[] postData = pinUrl.getBytes(Charset.forName("UTF-8"));
			int len = postData.length;
			String req = "https://www.pinterest.com/resource/PinResource/create/";
			HttpURLConnection con = (HttpURLConnection) new URL(req).openConnection(proxy);
			CommonHeaders.addCommonHeaders(con, user.getCookies());
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			con.setRequestProperty("Content-Length", Integer.toString(len));
			con.setRequestProperty("Referer", "https://www.pinterest.com/" + username + "/" + boardName + "/");
			con.setRequestProperty("Origin", "https://www.pinterest.com/");

			try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				wr.write(postData);
			}
			JSONObject root = this.getJsonResponse(con);
			JSONObject res = root.getJSONObject("resource_response");
			JSONObject data = res.getJSONObject("data");
			pinId = data.getString("id");
			logger.log("Response code from pin " + con.getResponseCode());
		} catch (Exception e) {
			logger.log("Failed when pinning " + e.getMessage());
			e.printStackTrace();
		}
		this.editPin(board, pinId, pin.getDescription(), pin.getSource());
	}

	public void unfollow(Pinner target) {
		try {
			String urlParam = UrlProvider.getUnfollow(user.getAccount().getUsername(), target.getUsername(), target.getId());
			byte[] postData = urlParam.getBytes(Charset.forName("UTF-8"));
			int postDataLength = postData.length;
			String request = "https://www.pinterest.com/resource/UserFollowResource/delete/";
			URL url = new URL(request);
			HttpURLConnection con = (HttpURLConnection) url.openConnection(proxy);
			CommonHeaders.addCommonHeaders(con);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			con.setRequestProperty("Accept-Encoding", "gzip, deflate");
			con.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
			con.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			con.setRequestProperty("Cookie", user.getCookies().toString());
			con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			con.setRequestProperty("X-CSRFToken", user.getCookies().getSslCookie().getValue());
			con.setRequestProperty("Referer", "https://www.pinterest.com/" + user.getAccount().getUsername() + "/following/");

			try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				wr.write(postData);
			}
			con.connect();
			logger.log(user.getAccount().getUsername() + "  unfollowed  " + target.getUsername());
			System.out.println("Response code from unfollow " + con.getResponseCode());
			con.disconnect();
		} catch (Exception e) {
			logger.log("Failed when unfollowing " + e.getMessage());
			e.printStackTrace();
		}

	}

	public String repin(Board board, String pinId, String description) {
		Cookies cookies = user.getCookies();
		String id = null;
		try {
			String boardId = board.getId();
			String urlParams = "source_url=%2F&data=%7B%22options%22%3A%7B%22pin_id%22%3A%22"
					+ pinId
					+ "%22%2C%22description%22%3A%22"
					+ description
					+ "%22%2C%22link%22%3A%22%22%2C%22is_video%22%3Afalse%2C%22board_id%22%3A%22"
					+ boardId
					+ "%22%7D%2C%22context%22%3A%7B%7D%7D&module_path=Modal()%3EPinCreate3(resource%3DPinResource(id%3D"
					+ pinId
					+ "%2C+allow_stale%3Dtrue))%3EBoardPicker(resource%3DBoardPickerBoardsResource(filter%3Dall%2C+allow_stale%3Dtrue))%3ESelectList(view_type%3DpinCreate3%2C+selected_section_index%3Dundefined%2C+selected_item_index%3Dundefined%2C+highlight_matched_text%3Dtrue%2C+suppress_hover_events%3Dundefined%2C+item_module%3D%5Bobject+Object%5D)";
			byte[] postData = urlParams.getBytes(Charset.forName("UTF-8"));
			int len = postData.length;
			String url = "https://www.pinterest.com/resource/RepinResource/create/";
			HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection(proxy);
			CommonHeaders.addCommonHeaders(con);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			con.setRequestProperty("Accept-Encoding", "json,deflate");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Content-Length", Integer.toString(len));
			con.setRequestProperty("Cookie", cookies.toString());
			con.setRequestProperty("X-CSRFToken", cookies.getSslCookie().getValue());
			con.setRequestProperty("Referer", "https://www.pinterest.com/");
			con.setRequestProperty("Origin", "https://www.pinterest.com/");
			try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				wr.write(postData);
			}
			con.connect();
			System.out.println("Response code from repin  " + con.getResponseCode());
			JSONObject root = this.getJsonResponse(con);
			JSONObject response = root.getJSONObject("resource_response");
			JSONObject data = response.getJSONObject("data");
			id = data.getString("id");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;

	}

	public Pin getPinInfo(String pinId) {
		Pin pin = new Pin();
		try {
			String url = "https://www.pinterest.com/pin/" + pinId + "/";
			HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
			CommonHeaders.addCommonHeaders(con);
			con.setRequestMethod("GET");
			con.setRequestProperty("Cookie", user.getCookies().toString());
			JSONObject root = this.getJsonResponse(con);
			JSONObject module = root.getJSONObject("module");
			JSONObject tree = module.getJSONObject("tree");
			JSONObject data = tree.getJSONObject("data");

			String description = data.getString("description");
			pin.setDescription(description);

			String link = data.getString("link");
			pin.setSource(link);
			pin.setImage("");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return pin;
	}

	public void editPin(Board board, String pinId, String description, String link) {
		Pin pin = this.getPinInfo(pinId);
		if (description.isEmpty()) {
			description = pin.getDescription();
		}
		if (link == null) {
			link = pin.getSource();
		}

		Cookies cookies = user.getCookies();
		String urlParams = UrlProvider.getEditPin(user.getAccount().getUsername(), board.getName(), board.getId(), description,
				link, pinId);
		try {
			byte[] postData = urlParams.getBytes(Charset.forName("UTF-8"));
			int len = postData.length;
			String req = "https://www.pinterest.com/resource/PinResource/update/";
			HttpsURLConnection con = (HttpsURLConnection) new URL(req).openConnection();
			CommonHeaders.addCommonHeaders(con);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			con.setRequestProperty("Accept-Encoding", "json,deflate");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Cookie", cookies.toString());
			con.setRequestProperty("Content-Length", Integer.toString(len));
			con.setRequestProperty("X-CSRFToken", cookies.getSslCookie().getValue());
			con.setRequestProperty("Referer", "https://www.pinterest.com/");
			con.setRequestProperty("Origin", "https://www.pinterest.com/");

			try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				wr.write(postData);
			}
			con.connect();
			this.getJsonResponse(con);
			logger.log("Response code from edit " + con.getResponseCode());
		} catch (Exception e) {
			logger.log("Failed on editing pin " + e.getMessage());
			e.printStackTrace();
		}

	}

	public User getManagedUser() {
		return this.user;
	}

	public List<Pinner> getFollowList(int minListSize) {
		logger.log("Will get " + minListSize);
		List<Pinner> userFollowers = this.getFollowers(user.getAccount().getUsername(), WAVE_FOLLOW_USERS_NUM);
		while (userFollowers.size() < WAVE_FOLLOW_USERS_NUM) {
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

	public List<String> searchPins(String keyword, int repinCount) {
		List<String> pinIds = new ArrayList<String>();
		String bookmark = "";
		int pinCount = 0;
		while (true) {
			try {
				if (bookmark.isEmpty()) {
					String url = "https://www.pinterest.com/search/pins/?q=" + keyword;
					HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection(proxy);
					con.setRequestMethod("GET");
					CommonHeaders.addCommonHeaders(con);
					con.setRequestProperty("Cookie", user.getCookies().toString());
					JSONObject root = this.getJsonResponse(con);
					JSONArray dataCache = root.getJSONArray("resource_data_cache");
					JSONObject guides = dataCache.getJSONObject(0);
					JSONObject resource = guides.getJSONObject("resource");
					JSONObject options = resource.getJSONObject("options");
					JSONArray bookmarks = options.getJSONArray("bookmarks");
					bookmark = bookmarks.getString(0);
					JSONObject module = root.getJSONObject("module");
					JSONObject tree = module.getJSONObject("tree");
					JSONObject data = tree.getJSONObject("data");
					JSONArray results = data.getJSONArray("results");
					for (int i = 0; i < results.length(); i++) {
						JSONObject pin = results.getJSONObject(i);
						pinCount++;
						int repins = Integer.parseInt(pin.getString("repin_count"));
						if (repins > repinCount) {
							pinIds.add(pin.getString("id"));
						}
					}
				} else {
					String url = UrlProvider.getPinSearch(keyword, bookmark);
					HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection(proxy);
					con.setRequestMethod("GET");
					CommonHeaders.addCommonHeaders(con);
					con.setRequestProperty("Cookie", user.getCookies().toString());
					JSONObject root = this.getJsonResponse(con);
					System.out.println("Response from search " + con.getResponseCode());
					JSONObject response = root.getJSONObject("resource_response");
					JSONArray data = response.getJSONArray("data");
					for (int i = 0; i < data.length(); i++) {
						JSONObject pin = data.getJSONObject(i);
						pinCount++;
						int repins = Integer.parseInt(pin.getString("repin_count"));
						if (repins > repinCount) {
							pinIds.add(pin.getString("id"));
						}
					}
					JSONObject resource = root.getJSONObject("resource");
					JSONObject options = resource.getJSONObject("options");
					JSONArray bookmarks = options.getJSONArray("bookmarks");
					bookmark = bookmarks.getString(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		System.out.println("Processed " + pinCount);
		System.out.println("Found " + pinIds.size() + " pins with more than  " + repinCount + " repins");
		return pinIds;

	}

	private User setUpUser(PinterestAccount acc) {
		User tmpUser = new User();
		tmpUser.setAccount(acc);
		Cookies cookies = this.login(acc);
		tmpUser.setCookies(cookies);
		List<Board> boards = this.getBoards(tmpUser);
		tmpUser.setBoards(boards);
		return tmpUser;
	}

	private Cookies login(PinterestAccount account) {
		String url = "https://www.pinterest.com/login/";
		logger.log("loggerging in with user " + account.getEmail());
		logger.log("Password is  " + account.getPassword());
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
			String urlParam = UrlProvider.getLogin(account.getEmail(), account.getPassword());
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
			logger.log("Response code from login is   " + con.getResponseCode());
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
		logger.log("FAILED TO LOGIN");
		return null;
	}

	private static Cookie getCookieFromString(String cookieStr) {
		String value = cookieStr.substring(cookieStr.indexOf("=") + 1);
		String name = cookieStr.substring(0, cookieStr.indexOf("="));
		return new Cookie(name, value);
	}

	private String upload(String pathToImage) {
		logger.log("Uploading image");
		String image_url = "";
		Cookies cookies = user.getCookies();
		try {
			File imageFile = new File(pathToImage);

			File img = new File(pathToImage);
			int postDataLength = Files.readAllBytes(Paths.get(pathToImage)).length;
			String request = "https://www.pinterest.com/upload-image/?img=" + imageFile.getName();
			URL url = new URL(request);
			Path path = Paths.get(pathToImage);
			byte[] data = Files.readAllBytes(path);
			String boundary = "---------------------------14956123715492";
			HttpURLConnection con = (HttpURLConnection) url.openConnection(proxy);
			CommonHeaders.addCommonHeaders(con);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			con.setRequestProperty("Accept-Encoding", "json,deflate");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			con.setRequestProperty("Cookie", cookies.toString());
			con.setRequestProperty("X-CSRFToken", cookies.getSslCookie().getValue());
			con.setRequestProperty("X-File-Name", "g.png");
			con.setRequestProperty("Referer", "https://www.pinterest.com/");
			con.setRequestProperty("Origin", "https://www.pinterest.com/");

			try (OutputStream output = con.getOutputStream(); OutputStreamWriter writer = new OutputStreamWriter(output)) {
				writer.append("--" + boundary).append(CRLF);
				writer.append("Content-Disposition: form-data; name=\"img\"; filename=\"" + img.getName() + "\"").append(CRLF);
				writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(img.getName())).append(CRLF);
				writer.append(CRLF).flush();
				output.write(data);
				writer.append("--" + boundary + "--").append(CRLF).flush();
			}
			con.connect();
			logger.log("Response code form upload  " + con.getResponseCode());
			JSONObject jsonObject = this.getJsonResponse(con);
			image_url = jsonObject.getString("image_url");
		} catch (Exception e) {
			logger.log("Failed on upload  " + e.getMessage());
			e.printStackTrace();
		}
		return image_url;
	}

	@SuppressWarnings({ "unused", "rawtypes" })
	private void printObject(JSONObject obj) {
		Iterator iter = obj.keys();
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
	}

	private JSONObject getJsonResponse(HttpsURLConnection con) {
		Validate.notNull(con);
		JSONObject json = null;
		String theString = "error";
		try {
			StringWriter writer = new StringWriter();
			IOUtils.copy(con.getInputStream(), writer, "utf-8");
			theString = writer.toString();
			json = new JSONObject(theString);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.disconnect();
		}
		return json;
	}

	private JSONObject getJsonResponse(HttpURLConnection con) {
		Validate.notNull(con);
		JSONObject json = null;
		String theString = "error";
		try {
			StringWriter writer = new StringWriter();
			IOUtils.copy(con.getInputStream(), writer, "utf-8");
			theString = writer.toString();
			json = new JSONObject(theString);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.disconnect();
		}
		return json;
	}

	@SuppressWarnings("unused")
	private String getTxtResponse(HttpURLConnection con) {
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(con.getInputStream(), writer, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	@SuppressWarnings("unused")
	private void printArray(JSONArray arr) {
		try {
			for (int i = 0; i < arr.length(); i++) {
				System.out.println(arr.get(i).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
