package com.age.pinterest.task;

import java.util.List;

import com.age.data.Pinner;
import com.age.help.BotPaths;
import com.age.param.FollowParam;
import com.age.pinterest.api.PinterestApi;

public class FollowTask extends Task {
	@SuppressWarnings("unused")
	private static final String FOLLOW_HISTORY_PATH = BotPaths.USER_ROOT + "/%s/followed.txt";

	private final FollowParam followParam;

	public FollowTask(FollowParam followParam) {
		super(followParam.getUser().getAccount().getUsername());
		this.followParam = followParam;
	}

	@Override
	public void run() {
		PinterestApi api = new PinterestApi(followParam.getUser());
		List<Pinner> followList = api.getFollowList(followParam.getSize());
		for (Pinner p : followList) {
			api.follow(p);
			this.sleep(followParam.getInterval());
		}
	}

	@Override
	public TaskType getType() {
		return TaskType.FOLLOW;
	}

}
