package com.age.pinterest.bot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;

import com.age.pinterest.config.PinterestAccount;
import com.age.pinterest.task.FollowByUserTask;
import com.age.pinterest.task.FollowTask;
import com.age.pinterest.task.PinTask;
import com.age.pinterest.task.Task;
import com.age.pinterest.task.UnFollowTask;

public class PinBot {
	public static String ROOT_DIR = "PinBotROOT";

	public static void addAccount(PinterestAccount acc) throws JsonGenerationException, JsonMappingException, IOException {
		System.out.println("Setting up user " + acc.getUser());
		String root = ROOT_DIR + "/Users/" + acc.getUser();
		File rootDir = new File(root);
		rootDir.mkdirs();
		File pinDir = new File(rootDir, "pins");
		pinDir.mkdir();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File(rootDir, "acc.json"), acc);
	}

	public void addFollowTask(String user, int count, long interval) throws ClientProtocolException, IOException, JSONException {
		PinterestAccount account = this.getAccount(user);
		this.startNewTask(new FollowTask(account, "", count, interval));
	}

	public void addPinTask(String user, String board, long interval) throws IOException, InterruptedException {
		PinterestAccount account = this.getAccount(user);
		this.startNewTask(new PinTask(account, board, interval));
	}

	public void addUnfollowTask(String user, int minFollower, long interval) throws IOException, JSONException, InterruptedException {
		PinterestAccount account = this.getAccount(user);
		this.startNewTask(new UnFollowTask(account, minFollower, interval));
	}

	public void addFollowByUserTaks(String user, String targetUser, long interval) {
		PinterestAccount account = this.getAccount(user);
		this.startNewTask(new FollowByUserTask(account, targetUser, interval));
	}

	public void generateAccounts(ArrayList<PinterestAccount> accounts) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		for (PinterestAccount acc : accounts) {
			String dest = PinBot.ROOT_DIR + "/" + acc.getUser();
			File f = new File(dest);
			f.mkdirs();
			dest = dest + "/" + "acc.json";

			mapper.writeValue(new File(dest), acc);
		}
	}

	public static List<String> listAccount() {
		String root = ROOT_DIR + "/Users";
		File f = new File(root);
		ArrayList<String> fileNames = new ArrayList<String>();
		for (File file : f.listFiles()) {
			fileNames.add(file.getName());
		}
		return fileNames;
	}

	private void startNewTask(Task task) {
		Thread thread = new Thread(task);
		thread.start();
	}

	private PinterestAccount getAccount(String username) {
		ObjectMapper mapper = new ObjectMapper();
		PinterestAccount account = null;
		String pathToUser = ROOT_DIR + "/Users/" + username;
		try {
			account = mapper.readValue(new File(pathToUser + "/acc.json"), PinterestAccount.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return account;
	}
}
