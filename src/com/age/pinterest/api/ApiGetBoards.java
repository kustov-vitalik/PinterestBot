package com.age.pinterest.api;

import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.age.data.Board;
import com.age.ui.Log;

public class ApiGetBoards {
	static List<Board> getBoards(String username) {
		List<Board> boards = new ArrayList<Board>();
		Log.log("Getting boards for user " + username);
		try {
			URL requestUrl = new URL(UrlProvider.getBoards(username));
			HttpURLConnection cox = (HttpURLConnection) requestUrl.openConnection();
			CommonHeaders.addCommonHeaders(cox);
			cox.setRequestMethod("GET");
			cox.setRequestProperty("Accept-Encoding", "json, deflate");
			StringWriter writer = new StringWriter();

			IOUtils.copy(cox.getInputStream(), writer, "utf-8");
			cox.disconnect();
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
				boards.add(new Board(aBoard.getString("name"), aBoard.getString("id")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return boards;
	}
}
