package com.age.pinterest.task;

import java.util.List;

import com.age.data.Pinner;
import com.age.data.PinterestAccount;
import com.age.help.BotPaths;
import com.age.pinterest.api.PinterestApi;
import com.age.ui.Log;

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
		Log.log("Starting follow task for " + acc.getUser() + " will follow " + size + " users and interval is  " + interval
				+ " seconds");
		PinterestApi api = new PinterestApi(acc);
		List<Pinner> followList = api.getFollowList(size);
		do {
			try {
				Pinner pinner = followList.get(0);
				api.follow(pinner);
			} catch (Exception e) {
				Log.log(acc.getUser() + "  Failed to follow" + e.getMessage());
			}
			followList.remove(0);
		} while (!followList.isEmpty() && this.intervalPassed(interval));
	}

}
