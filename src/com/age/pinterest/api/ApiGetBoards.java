package com.age.pinterest.api;

import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiGetBoards {
	public static void getBoards() throws IOException, JSONException {
		String username="globalamericase";
		String url = "https://www.pinterest.com/resource/UserResource/get/?source_url=%2Fglobalamericase%2F&data=%7B%22options%22%3A%7B%22username%22%3A%22globalamericase%22%2C%22invite_code%22%3Anull%7D%2C%22context%22%3A%7B%7D%2C%22module%22%3A%7B%22name%22%3A%22UserProfileContent%22%2C%22options%22%3A%7B%22tab%22%3A%22boards%22%7D%7D%2C%22render_type%22%3A1%2C%22error_strategy%22%3A0%7D&_=1426178220430";
		URL requestUrl = new URL(url);
		HttpURLConnection cox = (HttpURLConnection) requestUrl.openConnection();
		CommonHeaders.addCommonHeaders(cox);
		cox.setRequestMethod("GET");
		cox.setRequestProperty("Accept-Encoding", "json, deflate");
		StringWriter writer = new StringWriter();

		IOUtils.copy(cox.getInputStream(), writer, "utf-8");
		cox.disconnect();
		String theString = writer.toString();
		JSONObject jsonObject = new JSONObject(theString);
	
	}
}
