package com.age.pinterest.bot;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;

import com.age.pinterest.task.TaskType;

public class App {
	public static void main(String[] args) throws InterruptedException, IOException, JSONException,
			KeyManagementException, NoSuchAlgorithmException, URISyntaxException {
		System.out.println(TaskType.FOLLOW);
	}
}
