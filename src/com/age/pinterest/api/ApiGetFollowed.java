package com.age.pinterest.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import com.age.data.Pinner;

public class ApiGetFollowed {
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0";

	public static List<Pinner> getFollowed(String user, int maxListSize, int minFollowers, String sslToken) {
		List<Pinner> resultList = new ArrayList<Pinner>();
		String bookmark = "";
		while (!bookmark.equals("-end-")) {
			System.out.println("Target list:  " + resultList.size());
			CloseableHttpClient httpclient = HttpClients.createDefault();
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
							+ bookmark + "%3D%3D%22%5D%7D%2C%22context%22%3A%7B%7D%7D&module_path=App(module%3D%5Bobject+Object%5D)&_=" + num;
				}
				HttpGet httpget = new HttpGet(url);
				httpget.setHeader("User-Agent", USER_AGENT);
				httpget.setHeader("X-NEW-APP", "1");
				httpget.setHeader("Referer", "http://www.pinterest.com/" + user + "/following/");
				httpget.setHeader("Accept-Language", "en-gb,en;q=0.5");
				httpget.setHeader("X-Requested-With", "XMLHttpRequest");
				httpget.setHeader("X-APP-VERSION", "0cc7472");
				httpget.setHeader("X-NEW-APP", "1");
				httpget.setHeader("Cookie", sslToken + ";");
				httpget.setHeader("Accept-Encoding", "gzip, deflate");
				httpget.setHeader("Host", "www.pinterest.com");

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
			} finally {
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return resultList;
	}
}
