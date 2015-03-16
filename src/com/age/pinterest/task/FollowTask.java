package com.age.pinterest.task;

import java.util.List;

import org.apache.log4j.Logger;

import com.age.data.Pinner;
import com.age.data.PinterestAccount;
import com.age.help.BotPaths;
import com.age.pinterest.api.PinterestApi;

public class FollowTask extends Task {
	private static final Logger logger = Logger.getLogger(FollowTask.class);
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
		PinterestApi api = new PinterestApi(acc);
		List<Pinner> followList = api.getFollowList(size);
		while (!followList.isEmpty()) {
			if (this.intervalPassed(interval)) {
				try {
					Pinner pinner = followList.get(0);
					api.follow(pinner);

				} catch (Exception e) {
					logger.error(acc.getUser() + "  Failed to follow", e);
				}
				followList.remove(0);
			}
		}
	}

	@Override
	public Logger getLog() {
		return logger;
	}
}
