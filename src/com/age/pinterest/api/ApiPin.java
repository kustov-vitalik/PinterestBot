package com.age.pinterest.api;

import java.io.DataOutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.age.data.Board;
import com.age.data.CookieList;
import com.age.data.Pin;
import com.age.ui.Log;

public class ApiPin {

	static void pin(Pin pin, String user, Board board, CookieList cookies) {
		String cookieList = cookies.toString();
		String image_url = ApiUpload.upload(pin.getImage(), cookies);

		String username = user;
		String boardId = board.getId();
		String boardName = board.getName();
		Log.log("Pinning with user " + user + " to board " + boardName);
		String description = pin.getDescription();
		try {
			description = URLEncoder.encode(description, "UTF-8");
			String imageUrl = URLEncoder.encode(image_url, "UTF-8");
			String pinUrl = UrlProvider.getPin(username, boardName, description, boardId, imageUrl);
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
			Log.log("Response code from pin " + con.getResponseCode());
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
