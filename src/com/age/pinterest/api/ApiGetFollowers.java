package com.age.pinterest.api;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.age.data.CookieList;
import com.age.data.Pinner;

public class ApiGetFollowers {
	private static final Logger logger = Logger.getLogger(ApiGetFollowers.class);

	static List<Pinner> getFollowers(String user, int max, CookieList cookies) {
		logger.info("Getting followers for  " + user);
		ArrayList<Pinner> result = new ArrayList<Pinner>();
		String username = user;
		String bookmark = "";
		while (!bookmark.equals("-end-")) {
			try {
				logger.info(result.size() + "  followers from " + user);
				String num = Long.toString(System.currentTimeMillis());
				String url = "";
				if (bookmark.isEmpty()) {
					url = "https://www.pinterest.com/resource/UserResource/get/?source_url=%2F"
							+ username
							+ "%2F&data=%7B%22options%22%3A%7B%22username%22%3A%22"
							+ username
							+ "%22%2C%22invite_code%22%3Anull%7D%2C%22context%22%3A%7B%7D%2C%22module%22%3A%7B%22name%22%3A%22UserProfileContent%22%2C%22options%22%3A%7B%22tab%22%3A%22followers%22%7D%7D%2C%22render_type%22%3A1%2C%22error_strategy%22%3A0%7D&module_path=App()%3EUserProfilePage(resource%3DUserResource(username%3D"
							+ username
							+ "))%3EUserInfoBar(tab%3Dfollowers%2C+spinner%3D%5Bobject+Object%5D%2C+resource%3DUserResource(username%3D"
							+ username + "%2C+invite_code%3Dnull))&_=" + num;
				} else {
					url = "https://www.pinterest.com/resource/UserFollowersResource/get/?source_url=%2F"
							+ username
							+ "%2Ffollowers%2F&data=%7B%22options%22%3A%7B%22username%22%3A%22"
							+ username
							+ "%22%2C%22bookmarks%22%3A%5B%22"
							+ bookmark
							+ "%22%5D%7D%2C%22context%22%3A%7B%7D%7D&module_path=App()%3EUserProfilePage(resource%3DUserResource(username%3D"
							+ username
							+ "))%3EUserInfoBar(tab%3Dfollowers%2C+spinner%3D%5Bobject+Object%5D%2C+resource%3DUserResource(username%3D"
							+ username + "%2C+invite_code%3Dnull))&_=" + num;
				}

				URL requestUrl = new URL(url);
				HttpURLConnection cox = (HttpURLConnection) requestUrl.openConnection();
				CommonHeaders.addCommonHeaders(cox);
				cox.setRequestMethod("GET");
				cox.setRequestProperty("Referer", "http://www.pinterest.com/");
				cox.setRequestProperty("Cookie", cookies.getSslCookie().toString());
				cox.setRequestProperty("Accept-Encoding", "json, deflate");
				cox.setUseCaches(false);

				logger.info(cox.getResponseMessage());

				InputStream instream = cox.getInputStream();
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
						logger.info("Current size is  " + result.size());
						logger.info("Max is  " + max);
						if (max > 0 && result.size() >= max) {
							logger.info("Got  prev" + result.size() + "  followers from " + user);
							return result;
						}
						JSONObject resource = jsonObject.getJSONObject("resource");
						JSONObject options = resource.getJSONObject("options");
						JSONArray bookmarks = options.getJSONArray("bookmarks");
						bookmark = bookmarks.getString(0);
					}
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		logger.info("Got last " + result.size() + "  followers from " + user);
		return result;
	}
}
