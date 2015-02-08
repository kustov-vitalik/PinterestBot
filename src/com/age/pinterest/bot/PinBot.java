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
import org.openqa.selenium.WebDriver;

import com.age.data.Pinner;
import com.age.help.FileUtill;
import com.age.pinterest.api.Manager;
import com.age.pinterest.config.PinterestAccount;
import com.age.pinterest.task.FollowTask;
import com.age.pinterest.task.PinTask;
import com.age.pinterest.task.Task;
import com.age.pinterest.task.UnFollowTask;

public class PinBot {
	public static String ROOT_DIR = "D:/PinBot";

	private String userRoot;
	private final WebDriver driver;
	private final String user;
	private Manager manager;
	private ArrayList<Task> tasks = new ArrayList<Task>();

	public PinBot(WebDriver driver, String user) {
		this.driver = driver;
		this.user = user;
		this.setUp();
	}

	private void setUp() {
		try {
			System.out.println("Setting up user  " + user);
			this.userRoot = ROOT_DIR + "/Users/" + user;
			File rootDir = new File(userRoot);
			rootDir.mkdirs();
			File pinDir = new File(rootDir, "pins");
			pinDir.mkdir();
			System.out.println(userRoot);
			ObjectMapper mapper = new ObjectMapper();
			PinterestAccount account = mapper.readValue(new File(userRoot + "/" + "acc.json"), PinterestAccount.class);
			manager = new Manager(account, driver);
		} catch (Exception e) {
			System.out.println("Failed to set up  " + e.getMessage());
		}
	}

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

	public void addFollowTask(long interval, String keyword, int count) throws ClientProtocolException, IOException, JSONException {
		this.tasks.add(new FollowTask(driver, interval, user, getTargets(keyword, count)));
	}

	public void addPinTask(String board, long interval) throws IOException, InterruptedException {
		tasks.add(new PinTask(driver, board, user, interval));
	}

	public void addUnfollowTask(long interval, int minFollower) throws IOException, JSONException, InterruptedException {
		tasks.add(new UnFollowTask(driver, interval, getTrash(minFollower)));
	}

	public void buildHistory() throws ClientProtocolException, IOException, JSONException {
		manager.buildFollowHistory();
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

	public void start() {

		while (true) {
			for (Task task : tasks) {
				task.execute();
			}
		}
	}

	private ArrayList<String> getTrash(int followers) throws IOException, JSONException, InterruptedException {
		ArrayList<String> list = new ArrayList<String>();

		List<Pinner> pinners = manager.getUnfollowList(followers);
		for (Pinner p : pinners) {
			list.add(String.format("https://www.pinterest.com/%s/", p.getUsername()));
		}
		return list;

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

	private ArrayList<String> getTargets(String keyword, int count) throws ClientProtocolException, IOException, JSONException {
		ArrayList<String> list = new ArrayList<String>();
		List<Pinner> pinners = manager.getFollowList(count, keyword);
		for (Pinner p : pinners) {
			list.add(String.format("https://www.pinterest.com/%s/", p.getUsername()));
		}
		return list;
	}

}
