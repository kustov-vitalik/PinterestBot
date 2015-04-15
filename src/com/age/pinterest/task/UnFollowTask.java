package com.age.pinterest.task;

import java.util.List;

import com.age.data.Pinner;
import com.age.param.UnfollowParam;
import com.age.pinterest.api.PinterestApi;

public class UnFollowTask extends Task {
	private final UnfollowParam unfollowParam;

	public UnFollowTask(UnfollowParam unfollowParam) {
		super(unfollowParam.getUser().getAccount().getUsername());
		this.unfollowParam = unfollowParam;

	}

	@Override
	public void run() {
		PinterestApi api = new PinterestApi(unfollowParam.getUser());
		List<Pinner> trashPinners = api
				.getFollowed(unfollowParam.getUser().getAccount().getUsername(), -1, unfollowParam.getMinFollowers());
		for (Pinner p : trashPinners) {
			api.unfollow(p);
			this.sleep(unfollowParam.getInterval());
		}
	}

	@Override
	public TaskType getType() {
		return TaskType.UNFOLLOW;
	}

}