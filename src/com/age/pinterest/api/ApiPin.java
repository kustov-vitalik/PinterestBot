package com.age.pinterest.api;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONException;
import org.openqa.selenium.Cookie;

import com.age.pinterest.config.Pin;

public class ApiPin {

	public static void upload(Pin pin, String user, String board, Cookie sslCookie, Cookie sessionCookie, Cookie b)
			throws NoSuchAlgorithmException, KeyManagementException, IOException, JSONException {
		String cookieList = b.getName() + "=" + b.getValue() + "; ";
		cookieList = cookieList + "_pinterest_pfob=disabled; ";
		cookieList = cookieList + sessionCookie.getName() + "=" + sessionCookie.getValue() + "; ";
		cookieList = cookieList + sslCookie.getName() + "=" + sslCookie.getValue() + "; ";

		String image_url = ApiUpload.upload(pin.getImage(), b, sessionCookie, sslCookie);

		String username = user;
		String boardId = "547539335885608951";
		String boardName = board;
		String description = pin.getDescription();
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
		con.setRequestProperty("X-CSRFToken", sslCookie.getValue());
		con.setRequestProperty("Referer", "https://www.pinterest.com/globalamericase/motivation/");
		con.setRequestProperty("Origin", "https://www.pinterest.com/");

		try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
			wr.write(postData);
		}
		System.out.println("Respoinse code: " + con.getResponseCode());
		con.disconnect();

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
