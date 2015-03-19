package com.age.pinterest.api;

import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.age.data.CookieList;
import com.age.data.Pinner;
import com.age.ui.Log;

public class ApiGetFollowed {

	static List<Pinner> getFollowed(String user, int maxListSize, int minFollowers, CookieList cookies) {
		List<Pinner> resultList = new ArrayList<Pinner>();
		String bookmark = "";
		Log.log("Getting unfollow list for  " + user);
		while (!bookmark.equals("-end-")) {
			if (resultList.size() % 10 == 0 && !resultList.isEmpty()) {
				Log.log("unfollow list size is " + resultList.size());
			}
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
					url = "https://www.pinterest.com/resource/UserFollowingResource/get/?source_url=%2F" + user
							+ "%2Ffollowing%2F&data=%7B%22options%22%3A%7B%22username%22%3A%22" + user + "%22%2C%22bookmarks%22%3A%5B%22"
							+ bookmark + "%3D%3D%22%5D%7D%2C%22context%22%3A%7B%7D%7D&module_path=App(module%3D%5Bobject+Object%5D)&_="
							+ num;
				}
				URL requestUrl = new URL(url);
				HttpURLConnection cox = (HttpURLConnection) requestUrl.openConnection();
				CommonHeaders.addCommonHeaders(cox);
				cox.setRequestMethod("GET");
				cox.setRequestProperty("Referer", "http://www.pinterest.com/" + user + "/following/");
				cox.setRequestProperty("Cookie", cookies.getSslCookie().toString());
				cox.setRequestProperty("Accept-Encoding", "json, deflate");

				StringWriter writer = new StringWriter();

				IOUtils.copy(cox.getInputStream(), writer, "utf-8");
				cox.disconnect();
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
						int userFollowers = Integer.parseInt(root.getString("follower_count"));
						if (minFollowers < 0 || userFollowers < minFollowers) {
							Pinner pinner = new Pinner();
							pinner.setFollowers(userFollowers);
							pinner.setFullName(root.getString("full_name"));
							pinner.setId(root.getString("id"));
							pinner.setUsername(root.getString("username"));
							resultList.add(pinner);
						}
					} catch (Exception e) {
						Log.log(e.getMessage());
					}
				}
			} catch (Exception e) {
				Log.log(e.getMessage());
			}

		}
		return resultList;
	}
}
