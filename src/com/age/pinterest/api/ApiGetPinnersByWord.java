package com.age.pinterest.api;

import java.io.InputStream;
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

public class ApiGetPinnersByWord {

	static List<Pinner> getPinnersByKeyword(String username, int size, String keyword, CookieList cookies) {
		Log.log("Collection users related to  " + keyword);
		ArrayList<Pinner> followList = new ArrayList<Pinner>();
		String url = "https://www.pinterest.com/search/boards/?q=" + keyword;

		try {
			URL requestUrl = new URL(url);
			HttpURLConnection cox = (HttpURLConnection) requestUrl.openConnection();
			CommonHeaders.addCommonHeaders(cox);
			cox.setRequestMethod("GET");
			cox.setRequestProperty("Referer", "http://www.pinterest.com/" + username + "/following/");
			cox.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			cox.setRequestProperty("Cookie", cookies.getSslCookie().toString());
			cox.setRequestProperty("Accept-Encoding", "json, deflate");
			Log.log(cox.getResponseMessage());

			InputStream instream = cox.getInputStream();
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
				if (followList.size() > size) {
					break;
				}
				JSONObject user = results.getJSONObject(i);
				JSONObject owner = user.getJSONObject("owner");
				String usr = owner.getString("username");
				List<Pinner> pinners = ApiGetFollowers.getFollowers(usr, size - followList.size(), cookies);
				for (Pinner p : pinners) {
					followList.add(p);
				}
				Log.log("Follow list is:  " + followList.size());
			}
			return followList;
		} catch (Exception e) {
			Log.log("Account manager failed to get follow list. It will probably return empty list.  " + e.getMessage());
		}
		return new ArrayList<Pinner>();
	}

}
