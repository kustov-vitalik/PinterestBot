package com.age.pinterest.api;

import java.net.HttpURLConnection;

import com.age.data.Cookies;

public class CommonHeaders {
	private static final String USER_AGEND = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36";

	static void addCommonHeaders(HttpURLConnection cox, Cookies cookies) {
		addCommonHeaders(cox);
		cox.setRequestProperty("Cookie", cookies.toString());
//		cox.setRequestProperty("X-CSRFToken", cookies.getSslCookie().getValue());
	}

	static void addCommonHeaders(HttpURLConnection cox) {
		cox.setDoOutput(true);
		cox.setDoInput(true);
		cox.setInstanceFollowRedirects(false);
		cox.setRequestProperty("User-Agent", USER_AGEND);
		cox.setRequestProperty("X-NEW-APP", "1");
		cox.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		cox.setRequestProperty("Accept-Encoding", "json, deflate");
		cox.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		cox.setRequestProperty("Accept-Language", "en-gb,en;q=0.5");
		cox.setRequestProperty("X-APP-VERSION", "8718db9");
//		cox.setRequestProperty("Host", "www.pinterest.com");
		cox.setUseCaches(false);
	}

}
