package com.age.pinterest.bot;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.PropertyConfigurator;
import org.json.JSONException;

import com.age.help.DescriptionGenerator;

public class App {
	private static final String COCO = "globalamericase";
	private static final String LINDA = "linda1234willia";
	private static final String STACEY = "stacey123gray";
	private static final String COCO_BOARD = "dreamy-jewelry";
	private static final String LINDA_BOARD = "jewelry";
	private static final String STACEY_BOARD = "jewelry-that-i-would-like-to-wear";

	public static void main(String[] args) throws InterruptedException, IOException, JSONException, KeyManagementException,
			NoSuchAlgorithmException, URISyntaxException {
		String log4jConfPath = "prop/log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		DescriptionGenerator gen=new DescriptionGenerator();
		gen.getQuotes("honey");
	}
}
