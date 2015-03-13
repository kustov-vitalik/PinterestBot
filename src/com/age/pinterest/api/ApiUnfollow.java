package com.age.pinterest.api;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

import com.age.data.CookieList;
import com.age.data.Pinner;

public class ApiUnfollow {
	private static final Logger logger = Logger.getLogger(ApiUnfollow.class);

	static void unfollow(String thisUser, Pinner target, CookieList cookies) {

		String username = target.getUsername();
		String id = target.getId();
		logger.info("unfollowing  " + username);

		String cookieList = cookies.getSslCookie() + " ";
		cookieList = cookieList + cookies.getSessionCookie();
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			String urlParam = "source_url=/"
					+ thisUser
					+ "/following/&data={\"options\":{\"user_id\":\""
					+ id
					+ "\"},\"context\":{}}&module_path=App()>UserProfilePage(resource=UserResource(username="
					+ thisUser
					+ "))>UserProfileContent(resource=UserResource(username="
					+ thisUser
					+ "))>Grid(resource=UserFollowingResource(username="
					+ thisUser
					+ "))>GridItems(resource=UserFollowingResource(username="
					+ thisUser
					+ "))>User(resource=UserResource(username="
					+ username
					+ "))>UserFollowButton(followed=true, class_name=gridItem, unfollow_text=Unfollow, follow_ga_category=user_follow, unfollow_ga_category=user_unfollow, disabled=false, is_me=false, follow_class=default, log_element_type=62, text=Unfollow, user_id="
					+ id + ", follow_text=Follow, color=dim)";
			byte[] postData = urlParam.getBytes(Charset.forName("UTF-8"));
			int postDataLength = postData.length;
			String request = "https://www.pinterest.com/resource/UserFollowResource/delete/";
			URL url = new URL(request);
			HttpURLConnection cox = (HttpURLConnection) url.openConnection();
			CommonHeaders.addCommonHeaders(cox);
			cox.setRequestMethod("POST");
			cox.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			cox.setRequestProperty("Accept-Encoding", "gzip, deflate");
			cox.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
			cox.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			cox.setRequestProperty("Cookie", cookieList);
			cox.setRequestProperty("X-CSRFToken", cookies.getSslCookie().getValue());
			cox.setRequestProperty("Referer", "https://www.pinterest.com/" + thisUser + "/following/");

			try (DataOutputStream wr = new DataOutputStream(cox.getOutputStream())) {
				wr.write(postData);
			}
			cox.connect();
			logger.info("Response code:  " + cox.getResponseCode());

			cox.getInputStream();
			cox.disconnect();
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
