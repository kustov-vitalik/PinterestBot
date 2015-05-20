package com.age.pinterest.bot;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;

import com.age.help.FileUtill;

public class App {
	public static void main(String[] args) throws InterruptedException, IOException, JSONException, KeyManagementException,
			NoSuchAlgorithmException, URISyntaxException {

		String history = "c:\\h.txt";
		String pinId = "someId";
		boolean b= FileUtill.searchFile(history, pinId);
//		String content = FileUtill.getFileContents(history);
		System.out.println(b);
		Thread.sleep(1000 * 60 * 69);
		// System.out.println(hist);
	}
}
