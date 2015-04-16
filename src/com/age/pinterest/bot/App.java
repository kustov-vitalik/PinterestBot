package com.age.pinterest.bot;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.json.JSONException;

import com.age.data.Board;
import com.age.data.Pin;
import com.age.data.User;
import com.age.pinterest.api.PinterestApi;

public class App {
	public static void main(String[] args) throws InterruptedException, IOException, JSONException, KeyManagementException,
			NoSuchAlgorithmException, URISyntaxException {
		User user = PinBot.getUser("globalamericase");
		PinterestApi api = new PinterestApi(user);
		List<String> pinIds = api.searchPins("dress", 10000);
		Board board = null;
		for (Board b : user.getBoards()) {
			if (b.getId().equals("547539335885627233")) {
				board = b;
				break;
			}
		}
		for (String id : pinIds) {
			Pin p = api.getPinInfo(id);
			String descr = URLEncoder.encode(p.getDescription(), "utf-8");
			String newId = api.repin(board, id, "");
			api.editPin(board, newId, descr, "http://inspirationaldresses.com/");
			Thread.sleep(1000 * 60 * 90);
		}
	}
}
