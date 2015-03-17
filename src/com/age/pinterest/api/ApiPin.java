package com.age.pinterest.api;

import java.io.DataOutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

import com.age.data.Board;
import com.age.data.CookieList;
import com.age.data.Pin;
import com.age.ui.LogFrame;

public class ApiPin {

	static void pin(Pin pin, String user, Board board, CookieList cookies) {
		String cookieList = cookies.toString();
		String image_url = ApiUpload.upload(pin.getImage(), cookies);

		String username = user;
		String boardId = board.getId();
		String boardName = board.getName();

		String description = pin.getDescription();
		try {
			description = URLEncoder.encode(description, "UTF-8");
			String imageUrl = URLEncoder.encode(image_url, "UTF-8");
			String pinUrl = "https://www.pinterest.com/source_url=%2F" + username + "%2F" + boardName
					+ "%2F&data=%7B%22options%22%3A%7B%22board_id%22%3A%22" + boardId + "%22%2C%22description%22%3A%22" + description
					+ "%22%2C%22link%22%3A%22%22%2C%22image_url%22%3A%22" + imageUrl
					+ "%22%2C%22method%22%3A%22uploaded%22%7D%2C%22context%22%3A%7B%7D%7D&module_path=PinUploader(default_board_id%3D"
					+ boardId + ")%23Modal(module%3DPinCreate())";
			byte[] postData = pinUrl.getBytes(Charset.forName("UTF-8"));
			int len = postData.length;
			String req = "https://www.pinterest.com/resource/PinResource/create/";
			HttpsURLConnection con = (HttpsURLConnection) new URL(req).openConnection();
			CommonHeaders.addCommonHeaders(con);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			con.setRequestProperty("Accept-Encoding", "json,deflate");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Cookie", cookieList);
			con.setRequestProperty("Content-Length", Integer.toString(len));
			con.setRequestProperty("X-CSRFToken", cookies.getSslCookie().getValue());
			con.setRequestProperty("Referer", "https://www.pinterest.com/globalamericase/motivation/");
			con.setRequestProperty("Origin", "https://www.pinterest.com/");

			try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				wr.write(postData);
			}
			LogFrame.log("Respoinse code: " + con.getResponseCode());
			con.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] certs, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] certs, String authType) {
		}
	} };
}
