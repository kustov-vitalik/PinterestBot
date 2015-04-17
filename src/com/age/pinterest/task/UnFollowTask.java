package com.age.pinterest.task;

import java.util.Iterator;
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
		logger.log("Starting UNFOLLOW task for user " + unfollowParam.getUser());
		logger.log("Minimum followers the targets should have is " + unfollowParam.getMinFollowers());
		PinterestApi api = new PinterestApi(unfollowParam.getUser());
		List<Pinner> trashPinners = api
				.getFollowed(unfollowParam.getUser().getAccount().getUsername(), -1, unfollowParam.getMinFollowers());
		Iterator<Pinner> iter = trashPinners.iterator();
		while (iter.hasNext()) {
			Pinner p = iter.next();
			api.unfollow(p);
			iter.remove();
			logger.log("Remaining " + trashPinners.size());
			this.sleep(unfollowParam.getInterval());
		}
		logger.log("Unfollow task completed");
	}

	@Override
	public TaskType getType() {
		return TaskType.UNFOLLOW;
	}

}