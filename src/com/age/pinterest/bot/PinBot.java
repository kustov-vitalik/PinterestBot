package com.age.pinterest.bot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openqa.selenium.WebDriver;

import com.age.help.PinGenerator;
import com.age.help.PinUtils;
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
	private ArrayList<Task> tasks = new ArrayList<Task>();

	public PinBot(WebDriver driver, String user) {
		this.driver = driver;
		this.user = user;
		this.setUp();
	}

	private void setUp() {
		try {
			System.out.println("Setting up user");
			this.userRoot = ROOT_DIR + "/" + user;
			File rootDir = new File(userRoot);
			rootDir.mkdirs();
			File pinDir = new File(rootDir, "pins");
			pinDir.mkdir();
			ObjectMapper mapper = new ObjectMapper();
			PinterestAccount account = mapper.readValue(new File(userRoot + "/" + "acc.json"), PinterestAccount.class);
			PinUtils.login(driver, account);
		} catch (Exception e) {
			System.out.println("Failed to set up  " + e.getMessage());
		}
	}

	public void addFollowTask(String keyword, long interval) {
		this.tasks.add(new FollowTask(driver, keyword, interval));
	}

	public void addPinTask(String board, long interval) throws IOException, InterruptedException {
		tasks.add(new PinTask(driver, board, user, interval));
	}

	public void addUnfollowTask(long interval) {
		tasks.add(new UnFollowTask(driver, user, interval));
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

}
