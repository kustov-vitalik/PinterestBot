package com.age.pinterest.api;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.Cookie;

import com.age.pinterest.config.Pin;

public class ApiPin {

	public static void upload(Pin pin, String user, String board, Cookie sslCookie, Cookie sessionCookie, Cookie b) throws NoSuchAlgorithmException,
			KeyManagementException, IOException, JSONException {
		String cookieList = b.getName() + "=" + b.getValue() + "; ";
		cookieList = cookieList + "_pinterest_pfob=disabled; ";
		cookieList = cookieList + sessionCookie.getName() + "=" + sessionCookie.getValue() + "; ";
		cookieList = cookieList + sslCookie.getName() + "=" + sslCookie.getValue() + "; ";
		String imgUrl = pin.getImage();
		File imageFile=new File(imgUrl);
		

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		File img = new File(imgUrl);
		int postDataLength = Files.readAllBytes(Paths.get(imgUrl)).length;
		String request = "https://www.pinterest.com/upload-image/?img="+imageFile.getName();
		URL url = new URL(request);
		Path path = Paths.get(imgUrl);
		byte[] data = Files.readAllBytes(path);
		String boundary = "---------------------------14956123715492";
		String CRLF = "\r\n";
		HttpsURLConnection cox = (HttpsURLConnection) url.openConnection();
		cox.setDoOutput(true);
		cox.setDoInput(true);
		cox.setInstanceFollowRedirects(true);
		cox.setRequestMethod("POST");
		cox.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		cox.setRequestProperty("Accept-Encoding", "json,deflate");
		cox.setRequestProperty("Accept-Language", "en-gb,en;q=0.5");
		cox.setRequestProperty("Accept", "application/json");
		cox.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		cox.setRequestProperty("Content-Length", Integer.toString(postDataLength));
		cox.setRequestProperty("Cookie", cookieList);
		cox.setRequestProperty("X-CSRFToken", sslCookie.getValue());
		cox.setRequestProperty("X-File-Name", "g.png");
		cox.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0");
		cox.setRequestProperty("Referer", "https://www.pinterest.com/");
		cox.setRequestProperty("Origin", "https://www.pinterest.com/");
		cox.setUseCaches(false);

		try (OutputStream output = cox.getOutputStream(); OutputStreamWriter writer = new OutputStreamWriter(output)) {
			writer.append("--" + boundary).append(CRLF);
			writer.append("Content-Disposition: form-data; name=\"img\"; filename=\"" + img.getName() + "\"").append(CRLF);
			writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(img.getName())).append(CRLF);
			writer.append(CRLF).flush();
			output.write(data);
			writer.append("--" + boundary + "--").append(CRLF).flush();
		}

		cox.connect();
		System.out.println("Response code:  " + cox.getResponseCode());
		StringWriter writer = new StringWriter();
		IOUtils.copy(cox.getInputStream(), writer, "utf-8");
		String theString = writer.toString();

		JSONObject jsonObject = new JSONObject(theString);
		String image_url = jsonObject.getString("image_url");

		String username = user;
		String boardId = "547539335885608951";
		String boardName = board;
		String description = pin.getDescription();
		description = URLEncoder.encode(description, "UTF-8");
		String imageUrl = URLEncoder.encode(image_url, "UTF-8");
		String pinUrl = "https://www.pinterest.com/source_url=%2F" + username + "%2F" + boardName + "%2F&data=%7B%22options%22%3A%7B%22board_id%22%3A%22"
				+ boardId + "%22%2C%22description%22%3A%22" + description + "%22%2C%22link%22%3A%22%22%2C%22image_url%22%3A%22" + imageUrl
				+ "%22%2C%22method%22%3A%22uploaded%22%7D%2C%22context%22%3A%7B%7D%7D&module_path=PinUploader(default_board_id%3D" + boardId
				+ ")%23Modal(module%3DPinCreate())";
		byte[] postData = pinUrl.getBytes(Charset.forName("UTF-8"));
		int len = postData.length;
		String req = "https://www.pinterest.com/resource/PinResource/create/";
		HttpsURLConnection con = (HttpsURLConnection) new URL(req).openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setInstanceFollowRedirects(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		con.setRequestProperty("Accept-Encoding", "json,deflate");
		con.setRequestProperty("Accept-Language", "en-gb,en;q=0.5");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		con.setRequestProperty("Cookie", cookieList);
		con.setRequestProperty("X-APP-VERSION", "9a6ed57");
		con.setRequestProperty("Content-Length", Integer.toString(len));
		con.setRequestProperty("X-CSRFToken", sslCookie.getValue());
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0");
		con.setRequestProperty("Referer", "https://www.pinterest.com/globalamericase/motivation/");
		con.setRequestProperty("Origin", "https://www.pinterest.com/");
		con.setUseCaches(false);
		try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
			wr.write(postData);
		}
		System.out.println("Respoinse code: " + con.getResponseCode());
		cox.disconnect();
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
