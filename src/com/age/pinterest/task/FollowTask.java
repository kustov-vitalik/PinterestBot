package com.age.pinterest.task;

import java.util.List;

import com.age.data.Pinner;
import com.age.data.User;
import com.age.help.BotPaths;
import com.age.pinterest.api.PinterestApi;
import com.age.ui.Log;

public class FollowTask extends Task {
	public static final String PATH_TO_HISTORY_FORMAT = BotPaths.ROOT_DIR + "/Users/%s/followed.txt";

	private final int size;
	private final User user;
	private final long interval;

	public FollowTask(User user, int size, long interval) {
		this.interval = interval;
		this.user = user;
		this.size = size;
	}

	@Override
	public void run() {
		Log.log("Starting follow task for " + user.getAccount().getUser() + " will follow " + size + " users and interval is  "
				+ interval + " seconds");
		PinterestApi api = new PinterestApi(user);
		List<Pinner> followList = api.getFollowList(size);
		do {
			if (this.intervalPassed(interval)) {
				try {
					Pinner pinner = followList.get(0);
					api.follow(pinner);
				} catch (Exception e) {
					Log.log(user.getAccount().getUser() + "  failed to follow" + e.getMessage());
				}
				followList.remove(0);
				Log.log("Remaining to follow " + followList.size());
			}
		} while (!followList.isEmpty());
	}

	@Override
	public TaskType getType() {
		return TaskType.FOLLOW;
	}

}
