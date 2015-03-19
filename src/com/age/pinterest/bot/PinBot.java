package com.age.pinterest.bot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;

import com.age.data.Board;
import com.age.data.PinterestAccount;
import com.age.data.User;
import com.age.data.UserConfig;
import com.age.help.BotPaths;
import com.age.help.FileUtill;
import com.age.pinterest.api.PinterestApi;
import com.age.pinterest.task.FollowTask;
import com.age.pinterest.task.GenerateBasicPinsTask;
import com.age.pinterest.task.PinTask;
import com.age.pinterest.task.ScrapeTask;
import com.age.pinterest.task.Task;
import com.age.pinterest.task.UnFollowTask;
import com.age.ui.Log;

public class PinBot {

	public void addFollowTask(String user, int count, long interval) throws ClientProtocolException, IOException, JSONException {
		PinterestAccount account = this.getAccount(user);
		this.startNewTask(new FollowTask(account, count, interval));
	}

	public void addPinTask(String user, Board board, long interval) throws IOException, InterruptedException {
		PinterestAccount account = this.getAccount(user);
		this.startNewTask(new PinTask(account, board, interval));
	}

	public Task addUnfollowTask(String user, int minFollower, long interval) throws IOException, JSONException, InterruptedException {
		PinterestAccount account = this.getAccount(user);
		return this.startNewTask(new UnFollowTask(account, minFollower, interval));
	}

	public void addScrapeTask(String keyword, String tag, int count) {
		this.startNewTask(new ScrapeTask(keyword, tag, count));
	}

	public void addGenerateBasicPinsTask(String tag, String source) {
		this.startNewTask(new GenerateBasicPinsTask(tag, source));
	}

	private Task startNewTask(Task task) {
		Thread thread = new Thread(task);
		thread.start();
		return task;
	}

	private PinterestAccount getAccount(String username) {
		ObjectMapper mapper = new ObjectMapper();
		PinterestAccount account = null;
		String pathToUser = BotPaths.ROOT_DIR + "/Users/" + username;
		try {
			account = mapper.readValue(new File(pathToUser + "/acc.json"), PinterestAccount.class);
		} catch (IOException e) {
			Log.log("Failed to read account " + e.getMessage());
		}
		return account;
	}

	public static void setUpDirTree() {
		File root = new File(BotPaths.ROOT_DIR);
		root.mkdirs();
		root = new File(BotPaths.USER_ROOT);
		root.mkdirs();
		root = new File(BotPaths.PINS_ROOT);
		root.mkdirs();
		root = new File(BotPaths.IMAGES_ROOT);
		root.mkdirs();

	}

	public static void addAccount(PinterestAccount acc) throws JsonGenerationException, JsonMappingException, IOException {
		Log.log("Setting up user " + acc.getUser());
		String root = BotPaths.USER_ROOT + acc.getUser();
		File rootDir = new File(root);
		rootDir.mkdirs();
		File pinDir = new File(rootDir, "pins");
		pinDir.mkdir();
		PinterestApi api = new PinterestApi(acc);
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File(rootDir, "acc.json"), acc);
		mapper.writeValue(new File(rootDir, "config.json"), new UserConfig());
		mapper.writeValue(new File(rootDir, "user.json"), api.getManagedUser());
	}

	public static List<PinterestAccount> listAccount() {
		String root = BotPaths.USER_ROOT;
		File f = new File(root);
		ArrayList<PinterestAccount> fileNames = new ArrayList<PinterestAccount>();
		ObjectMapper mapper = new ObjectMapper();
		PinterestAccount account = null;
		for (File file : f.listFiles()) {
			File acc = new File(file, "acc.json");
			try {
				account = mapper.readValue(acc, PinterestAccount.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
			fileNames.add(account);
		}
		return fileNames;
	}

	public static void listUsers() throws IOException {
		File rootDir = new File(BotPaths.USER_ROOT);
		for (File f : rootDir.listFiles()) {
			File user = new File(f, "user.json");
			System.out.println(FileUtill.getFileListContents(user.getAbsolutePath()));
		}

	}

	public static User getUser(String user) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		File usersRoot = new File(BotPaths.USER_ROOT);
		File userFile = new File(usersRoot, user + "/user.json");
		return mapper.readValue(userFile, User.class);
	}

	public static UserConfig getUserConfig(String user) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		File usersRoot = new File(BotPaths.USER_ROOT);
		File cfgFile = new File(usersRoot, user + "/config.json");
		return mapper.readValue(cfgFile, UserConfig.class);
	}
}
