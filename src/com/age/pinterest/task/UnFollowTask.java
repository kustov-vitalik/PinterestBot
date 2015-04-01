package com.age.pinterest.task;

import java.util.List;

import com.age.data.Pinner;
import com.age.data.User;
import com.age.pinterest.api.PinterestApi;
import com.age.ui.Log;

public class UnFollowTask extends Task {
	private final long interval;
	private final int minFollowers;
	private final User user;

	public UnFollowTask(User user, int minFollowers, long interval) {
		this.interval = interval;
		this.user = user;
		this.minFollowers = minFollowers;
	}

	@Override
	public void run() {
		PinterestApi api = new PinterestApi(user);
		List<Pinner> trashPinners = api.getFollowed(user.getAccount().getUser(), -1, minFollowers);
		do {
			if (this.intervalPassed(interval)) {
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
		Log.log("Unfollow task ended for " + user.getAccount().getUser());
	}

	@Override
	public TaskType getType() {
		return TaskType.UNFOLLOW;
	}

}