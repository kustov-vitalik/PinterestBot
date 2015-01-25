package com.age.pinterest.bot;

import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.WebDriver;

import com.age.pinterest.task.PinTask;
import com.age.pinterest.task.Task;

public class PinBot {
	private static final String BOARDS_URL_FORMAT = "http://www.pinterest.com/%s/%s";
	private static final String LOGIN_URL = "https://pinterest.com/login/";
	private ArrayList<Task> tasks = new ArrayList<Task>();
	private final WebDriver driver;

	public PinBot(WebDriver driver, String email, String password) {
		this.driver = driver;
		PinUtils.login(driver, email, password);
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

	public void start() {
		while (true) {
			for (Task task : tasks) {
				task.execute();
			}
		}
	}
}
