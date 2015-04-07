package com.age.pinterest.task;

import java.util.List;

import com.age.data.Pinner;
import com.age.help.BotPaths;
import com.age.param.FollowParam;
import com.age.pinterest.api.PinterestApi;
import com.age.ui.Log;

public class FollowTask extends Task {
	public static final String PATH_TO_HISTORY_FORMAT = BotPaths.ROOT_DIR + "/Users/%s/followed.txt";

	private final FollowParam follow;

	public FollowTask(FollowParam followData) {
		this.follow = followData;
	}

	@Override
	public void run() {
		Log.log("Starting follow task for " + follow.getUser().getAccount().getUsername() + " will follow " + follow.getSize()
				+ " users and interval is  " + follow.getInterval() + " seconds");
		PinterestApi api = new PinterestApi(follow.getUser());
		List<Pinner> followList = api.getFollowList(follow.getSize());
		do {
			if (this.intervalPassed(follow.getInterval())) {
				try {
					Pinner pinner = followList.get(0);
					api.follow(pinner);
				} catch (Exception e) {
					Log.log(follow.getUser().getAccount().getUsername() + "  failed to follow" + e.getMessage());
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
