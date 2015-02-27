package com.age.pinterest.api;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;

public class ApiLogin {
	private static final Logger logger = Logger.getLogger(ApiLogin.class);

	public static void login() throws IllegalStateException, IOException, JSONException, NoSuchAlgorithmException, KeyManagementException {
		String url = "https://www.pinterest.com/login/";
		URL requestUrl = new URL(url);
		HttpURLConnection cox = (HttpURLConnection) requestUrl.openConnection();
		cox.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		cox.setRequestProperty("Accept-Encoding", "gzip");
		cox.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0");
		logger.info(cox.getResponseMessage());
		String headerName = null;
		String sslHeader = "";
		String sessionHeader = "";
		for (int i = 1; (headerName = cox.getHeaderFieldKey(i)) != null; i++) {
			if (headerName.equals("Set-Cookie")) {
				String cookie = cox.getHeaderField(i);
				cookie = cookie.substring(0, cookie.indexOf(";"));
				if (cookie.contains("csrftoken")) {
					sslHeader = cookie;
				} else if (cookie.contains("_pinterest_sess")) {
					sessionHeader = cookie;
				}
			}
		}
		cox.disconnect();

		String cookieList = sslHeader + "; " + sessionHeader;
		String sslValue = sslHeader.substring(sslHeader.indexOf("=") + 1);
		System.out.println(sslValue);
		System.out.println(cookieList);
		String urlParam = "source_url=%2flogin%2f&data=%7b%22options%22%3a%7b%22username_or_email%22%3a%22globalamericaselfdefensejohn%40gmail.com%22%2c%22password%22%3a%22Geni0us!%22%7d%2c%22context%22%3a%7b%7d%7d&module_path=App()%3eLoginPage()%3eLogin()%3eButton(class_name%3dprimary%2c+text%3dLog+in%2c+type%3dsubmit%2c+size%3dlarge)";
		url = "https://www.pinterest.com/resource/UserSessionResource/create/";
		byte[] postData = urlParam.getBytes(Charset.forName("UTF-8"));
		int postDataLength = postData.length;
		requestUrl = new URL(url);
		cox = (HttpURLConnection) requestUrl.openConnection();
		CommonHeaders.addCommonHeaders(cox);
		cox.setRequestMethod("POST");
		cox.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		cox.setRequestProperty("Accept-Encoding", "text, deflate");
		cox.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		cox.setRequestProperty("Content-Length", Integer.toString(postDataLength));
		cox.setRequestProperty("Cookie", cookieList);
		cox.setRequestProperty("X-CSRFToken", sslValue);
		cox.setRequestProperty("Referer", "https://www.pinterest.com/login");
		try (DataOutputStream wr = new DataOutputStream(cox.getOutputStream())) {
			wr.write(postData);
		}

		for (int i = 1; (headerName = cox.getHeaderFieldKey(i)) != null; i++) {
			if (headerName.equals("Set-Cookie")) {
				System.out.println(cox.getHeaderField(i));
			}
		}
		cox.connect();
		logger.info("Response code:  " + cox.getResponseCode());
		StringWriter writer = new StringWriter();

		IOUtils.copy(cox.getInputStream(), writer, "utf-8");
		String theString = writer.toString();
		System.out.println(theString);
		cox.disconnect();

	}

}
