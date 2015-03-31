package com.age.pinterest.api;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import com.age.data.CookieList;
import com.age.data.Pinner;
import com.age.ui.Log;

public class ApiFollow {

	static void follow(Pinner target, CookieList cookies) {

		String username = target.getUsername();
		String id = target.getId();
		Log.log("Following  " + username);
		try {
			String cookieList = cookies.getSslCookie() + " ";
			cookieList = cookieList + cookies.getSessionCookie();
			String urlParameters = UrlProvider.getFollow(username, id);
			byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
			int postDataLength = postData.length;
			String request = "https://www.pinterest.com/resource/UserFollowResource/create/";
			URL url = new URL(request);
			HttpURLConnection cox = (HttpURLConnection) url.openConnection();
			CommonHeaders.addCommonHeaders(cox);
			cox.setRequestMethod("POST");
			cox.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			cox.setRequestProperty("Accept-Encoding", "gzip, deflate");
			cox.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
			cox.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			cox.setRequestProperty("Cookie", cookieList);
			cox.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			cox.setRequestProperty("X-CSRFToken", cookies.getSslCookie().getValue());
			cox.setRequestProperty("Referer", "https://www.pinterest.com/" + username + "/");

			try (DataOutputStream wr = new DataOutputStream(cox.getOutputStream())) {
				wr.write(postData);
			}
			cox.connect();
			Log.log("Response code from follow  " + cox.getResponseCode());

			cox.getInputStream();
			cox.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
