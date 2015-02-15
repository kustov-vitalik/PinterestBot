package com.age.pinterest.api;

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

public class ApiGetPinners {
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0";

	public static List<Pinner> getPinnersByKeyword(String username, int size, String keyword, String sslToken) {
		System.out.println("Collection users related to  " + keyword);
		ArrayList<Pinner> followList = new ArrayList<Pinner>();
		String url = "https://www.pinterest.com/search/boards/?q=" + keyword;
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("User-Agent", USER_AGENT);
		httpget.setHeader("X-NEW-APP", "1");
		httpget.setHeader("Referer", "http://www.pinterest.com/" + username + "/following/");
		httpget.setHeader("Accept-Language", "en-gb,en;q=0.5");
		httpget.setHeader("X-Requested-With", "XMLHttpRequest");
		httpget.setHeader("X-APP-VERSION", "0cc7472");
		httpget.setHeader("X-NEW-APP", "1");
		httpget.setHeader("Cookie", sslToken + ";");
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
				List<Pinner> pinners = ApiGetFollowers.getFollowers(usr, size - followList.size(), sslToken);
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

}
