package com.age.pinterest.bot;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;

import com.age.data.PinterestAccount;
import com.age.help.AccountManager;

public class App {
	private static final String COCO = "globalamericase";
	private static final String LINDA = "linda1234willia";
	private static final String STACEY = "stacey123gray";
	private static final String COCO_BOARD = "dreamy-jewelry";
	private static final String LINDA_BOARD = "jewelry";
	private static final String STACEY_BOARD = "jewelry-that-i-would-like-to-wear";

	public static void main(String[] args) throws InterruptedException, IOException, JSONException, KeyManagementException,
			NoSuchAlgorithmException, URISyntaxException {
		PinterestAccount acc = new PinterestAccount();
		acc.setEmail("globalamericaselfdefensejohn@gmail.com");
		acc.setUser("globalamericase");
		acc.setPassword("Geni0us!");
		new AccountManager(acc);
	}
}
