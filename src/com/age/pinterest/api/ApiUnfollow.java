package com.age.pinterest.api;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.openqa.selenium.Cookie;

import com.age.data.Pinner;

public class ApiUnfollow {

	public static void unfollow(String thisUser, Pinner target, Cookie sslCookie, Cookie sessionCookie) throws ClientProtocolException, IOException,
			JSONException, InterruptedException, NoSuchAlgorithmException, KeyManagementException {

		String username = target.getUsername();
		String id = target.getId();
		System.out.println("unfollowing  " + username);

		String cookieList = sslCookie.getName() + "=" + sslCookie.getValue() + "; ";
		cookieList = cookieList + sessionCookie.getName() + "=" + sessionCookie.getValue() + "; ";
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
		cox.setRequestProperty("Cookie", cookieList);
		cox.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		cox.setRequestProperty("X-CSRFToken", sslCookie.getValue());
		cox.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36");
		cox.setRequestProperty("Referer", "https://www.pinterest.com/" + thisUser + "/following/");
		cox.setUseCaches(false);
		try (DataOutputStream wr = new DataOutputStream(cox.getOutputStream())) {
			wr.write(postData);
		}
		cox.connect();
		System.out.println("Response code:  " + cox.getResponseCode());

		cox.getInputStream();
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
