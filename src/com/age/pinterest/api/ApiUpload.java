package com.age.pinterest.api;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.openqa.selenium.Cookie;

public class ApiUpload {
	public static String upload(String localImage, Cookie b, Cookie sessionCookie, Cookie sslCookie) {
		String image_url = "";
		try {
			String cookieList = b.getName() + "=" + b.getValue() + "; ";
			cookieList = cookieList + "_pinterest_pfob=disabled; ";
			cookieList = cookieList + sessionCookie.getName() + "=" + sessionCookie.getValue() + "; ";
			cookieList = cookieList + sslCookie.getName() + "=" + sslCookie.getValue() + "; ";
			String imgUrl = localImage;
			File imageFile = new File(imgUrl);

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
			String request = "https://www.pinterest.com/upload-image/?img=" + imageFile.getName();
			URL url = new URL(request);
			Path path = Paths.get(imgUrl);
			byte[] data = Files.readAllBytes(path);
			String boundary = "---------------------------14956123715492";
			String CRLF = "\r\n";
			HttpsURLConnection cox = (HttpsURLConnection) url.openConnection();
			CommonHeaders.addCommonHeaders(cox);
			cox.setRequestMethod("POST");
			cox.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			cox.setRequestProperty("Accept-Encoding", "json,deflate");
			cox.setRequestProperty("Accept", "application/json");
			cox.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			cox.setRequestProperty("Cookie", cookieList);
			cox.setRequestProperty("X-CSRFToken", sslCookie.getValue());
			cox.setRequestProperty("X-File-Name", "g.png");
			cox.setRequestProperty("Referer", "https://www.pinterest.com/");
			cox.setRequestProperty("Origin", "https://www.pinterest.com/");

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
			image_url = jsonObject.getString("image_url");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image_url;

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
