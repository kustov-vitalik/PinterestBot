package com.age.pinterest.bot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.WebDriver;

import com.age.pinterest.config.PinterestAccount;
import com.age.pinterest.task.FollowTask;
import com.age.pinterest.task.PinTask;
import com.age.pinterest.task.Task;

public class PinBot {
	private static final String ROOT_DIR_FORMAT="D:\\PinBotRoot\\%s";
	private ArrayList<Task> tasks = new ArrayList<Task>();
	private final WebDriver driver;
	private final PinterestAccount account;

	public PinBot(WebDriver driver, PinterestAccount account) {
		this.driver = driver;
		this.account = account;
		this.setUp();
	}

	public void addPinTask(String configFile) throws IOException, InterruptedException {
		String boardsLocation = "";
		String board = "";
		String city = "";
		String tag = "";
		String source = "";
		String keywords = "";
		String configurations = FileUtill.getFileContents(configFile);
		String[] cfg = configurations.split("\n");
		for (String s : cfg) {
			if (s.contains("boardsLocation")) {
				boardsLocation = s.replace("boardsLocation:", "");
			} else if (s.contains("board")) {
				board = s.replace("board:", "");
			} else if (s.contains("city")) {
				city = s.replace("city:", "");
			} else if (s.contains("keywords")) {
				keywords = s.replace("keywords:", "");
			} else if (s.contains("tag")) {
				tag = s.replace("tag:", "");
			} else if (s.contains("source")) {
				source = s.replace("source:", "");
			}
		}

		tasks.add(new PinTask(driver, boardsLocation, board, city, source, keywords, tag, 10000));
	}

	private void setUp() {
		System.out.println("Setting up user  " + account.getEmail());
		File rootDir=new File(String.format(ROOT_DIR_FORMAT, account.getUser()));
		rootDir.mkdirs();
		PinUtils.login(driver, account);
	}

	public void addFollowTask(String keyword) {

		FollowTask task = new FollowTask(driver, keyword, 5000);
		this.tasks.add(task);
	}

	public void start() {
		while (true) {
			for (Task task : tasks) {
				task.execute();
			}
		}
	}
}
