package com.age.pinterest.task;

import java.util.List;

import com.age.data.Pinner;
import com.age.param.UnfollowParam;
import com.age.pinterest.api.PinterestApi;
import com.age.ui.Log;

public class UnFollowTask extends Task {
	private final UnfollowParam unfollowParam;

	public UnFollowTask(UnfollowParam unfollowParam) {
		this.unfollowParam = unfollowParam;

	}

	@Override
	public void run() {
		PinterestApi api = new PinterestApi(unfollowParam.getUser());
		List<Pinner> trashPinners = api
				.getFollowed(unfollowParam.getUser().getAccount().getUsername(), -1, unfollowParam.getMinFollowers());
		do {
			if (this.intervalPassed(unfollowParam.getInterval())) {
				try {
					Pinner pinner = trashPinners.get(0);
					api.unfollow(pinner);
					trashPinners.remove(0);
					Log.log("Remaining to unfollow " + trashPinners.size());
				} catch (Exception e) {
					Log.log("Failed to unfollow  " + e.getMessage());
				}
			}
		} while (!trashPinners.isEmpty());
		Log.log("Unfollow task ended for " + unfollowParam.getUser().getAccount().getUsername());
	}

	@Override
	public TaskType getType() {
		return TaskType.UNFOLLOW;
	}

}