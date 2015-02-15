package com.age.pinterest.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
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

public class ApiPin {

	public static void upload(Cookie sslCookie, Cookie sessionCookie, Cookie b) throws NoSuchAlgorithmException, KeyManagementException, IOException,
			JSONException {
		String cookieList = b.getName() + "=" + b.getValue() + "; ";
		cookieList = cookieList + "_pinterest_pfob=disabled; ";
		cookieList = cookieList + sessionCookie.getName() + "=" + sessionCookie.getValue() + "; ";
		cookieList = cookieList + sslCookie.getName() + "=" + sslCookie.getValue() + "; ";
		String imgUrl = "C:/Users/Borio/Desktop/g.png";

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		// byte[] postData = ArrayUtils.addAll(extraString.getBytes(),
		// Files.readAllBytes(Paths.get(imgUrl)));
		File img = new File(imgUrl);
		int postDataLength = Files.readAllBytes(Paths.get(imgUrl)).length;
		String request = "https://www.pinterest.com/upload-image/?img=g.png";
		URL url = new URL(request);
		// HttpClient httpclient = HttpClients.createDefault();
		// HttpPost httppost = new HttpPost(request);
		// httppost.setHeader("Content-Type",
		// "multipart/form-data; boundary=----------------------------8d2175e05d11be8");
		// httppost.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		// httppost.setHeader("X-NEW-APP", "1");
		// httppost.setHeader("Accept-Language", "en-gb,en;q=0.8");
		// httppost.setHeader("X-APP-VERSION", "8718db9");
		// httppost.setHeader("Accept", "*/*");
		// httppost.setHeader("Cookie", cookieList);
		// httppost.setHeader("X-Requested-With", "XMLHttpRequest");
		// httppost.setHeader("X-CSRFToken", sslCookie.getValue());
		// httppost.setHeader("X-File-Name", "1.png");
		// httppost.setHeader("User-Agent",
		// "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36");
		// httppost.setHeader("Referer", "https://www.pinterest.com/");
		//
		// MultipartEntity reqEntity = new MultipartEntity();
		// // reqEntity.addPart("someParam", "someValue");
		// reqEntity.addPart("Content-Disposition: form-data; name=\"img\"; filename=\"1.png\"\nContent-Type: image/png",
		// new FileBody(new File(imgUrl)));
		// httppost.setEntity(reqEntity);
		//
		// HttpResponse response = httpclient.execute(httppost);
		// System.out.println(response.getStatusLine());

		// String boundary = Long.toHexString(System.currentTimeMillis());
		Path path = Paths.get(imgUrl);
		byte[] data = Files.readAllBytes(path);
		// String boundary = "----------------------------" +
		// Long.toHexString(System.currentTimeMillis());
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

		System.out.println(theString);
		// JSONObject jsonObject = new JSONObject(theString);
		// System.out.println(jsonObject);
		cox.disconnect();

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
