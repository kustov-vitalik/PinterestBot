package com.age.pinterest.api;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.json.JSONException;

public class ApiLogin {
	private static final Logger logger =  Logger.getLogger(ApiLogin.class);
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0";

	public static void login() throws IllegalStateException, IOException, JSONException, NoSuchAlgorithmException, KeyManagementException {
		String loginUrl = "https://www.pinterest.com/login/";
		HttpGet httpget = new HttpGet(loginUrl);
		httpget.setHeader("User-Agent", USER_AGENT);
		httpget.setHeader("Referer", "http://www.pinterest.com");
		httpget.setHeader("Accept-Encoding", "gzip");
		httpget.setHeader("Host", "www.pinterest.com");
		httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = httpclient.execute(httpget);
		HeaderElement sslToken = null;
		for (Header h : response.getAllHeaders()) {
			if (h.getName().equals("Set-Cookie")) {
				for (HeaderElement el : h.getElements()) {
					if (el.getName().equals("csrftoken")) {
						sslToken = el;
					}
				}
			}
		}
		logger.info(sslToken.getName());
		logger.info(sslToken.getValue());

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		
		String urlParam = "source_url=%2flogin%2f&data=%7b%22options%22%3a%7b%22username_or_email%22%3a%22globalamericaselfdefensejohn%40gmail.com%22%2c%22password%22%3a%22Geni0us!%22%7d%2c%22context%22%3a%7b%7d%7d&module_path=App()%3eLoginPage()%3eLogin()%3eButton(class_name%3dprimary%2c+text%3dLog+in%2c+type%3dsubmit%2c+size%3dlarge)";
		byte[] postData = urlParam.getBytes(Charset.forName("UTF-8"));
		int postDataLength = postData.length;
		String request = "https://www.pinterest.com/resource/UserSessionResource/create/";
		URL url = new URL(request);
		HttpURLConnection cox = (HttpURLConnection) url.openConnection();
		cox.setDoOutput(true);
		cox.setDoInput(true);
		cox.setInstanceFollowRedirects(false);
		cox.setRequestMethod("POST");
		cox.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		cox.setRequestProperty("Accept-Encoding", "gzip, deflate");
		cox.setRequestProperty("X-NEW-APP", "1");
		cox.setRequestProperty("Accept-Language", "en-gb,en;q=0.5");
		cox.setRequestProperty("X-APP-VERSION", "8718db9");
		cox.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		cox.setRequestProperty("Content-Length", Integer.toString(postDataLength));
		cox.setRequestProperty("Cookie", sslToken.getName() + "=" + sslToken.getValue() + "; ");
		cox.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		cox.setRequestProperty("X-CSRFToken", sslToken.getValue());
		cox.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36");
		cox.setRequestProperty("Referer", "https://www.pinterest.com/login/");
		cox.setUseCaches(false);
		try (DataOutputStream wr = new DataOutputStream(cox.getOutputStream())) {
			wr.write(postData);
		}
		cox.connect();
		logger.info("Response code:  " + cox.getResponseCode());

		cox.getInputStream();
		for (String key : cox.getHeaderFields().keySet()) {
			logger.info(key);
			if (key != null && key.equals("Set-Cookie")) {
				logger.info(cox.getHeaderField(key));
			}
		}
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
