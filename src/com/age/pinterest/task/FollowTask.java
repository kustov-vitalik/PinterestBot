package com.age.pinterest.task;

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.age.data.Pinner;
import com.age.help.BotPaths;
import com.age.help.PinUtils;
import com.age.pinterest.api.AccountManager;
import com.age.pinterest.config.PinterestAccount;

public class FollowTask extends Task {
	public static final String PATH_TO_HISTORY_FORMAT = BotPaths.ROOT_DIR + "/Users/%s/followed.txt";

	private final int size;
	private final PinterestAccount acc;
	private final long interval;

	public FollowTask(PinterestAccount acc, int size, long interval) {
		this.interval = interval;
		this.acc = acc;
		this.size = size;
	}

	@Override
	public void run() {
		WebDriver driver = PinUtils.getChrome();
		AccountManager manager = new AccountManager(acc, driver);
		List<Pinner> followList = manager.getFollowList(size);
		while (!followList.isEmpty()) {
			if (this.intervalPassed(interval)) {
				try {
					Pinner pinner = followList.get(0);
					manager.follow(pinner);

				} catch (Exception e) {
					System.out.println(acc.getUser() + "  Failed to follow");
					e.printStackTrace();
				}
				followList.remove(0);
			}
		}
		driver.quit();
	}
}
