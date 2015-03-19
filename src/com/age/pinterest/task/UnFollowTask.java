package com.age.pinterest.task;

import java.util.List;

import com.age.data.Pinner;
import com.age.data.PinterestAccount;
import com.age.pinterest.api.PinterestApi;
import com.age.ui.Log;

public class UnFollowTask extends Task {
	private final long interval;
	private final PinterestAccount acc;
	private final int minFollowers;

	public UnFollowTask(PinterestAccount acc, int minFollowers, long interval) {
		this.interval = interval;
		this.acc = acc;
		this.minFollowers = minFollowers;
	}

	@Override
	public void run() {
		PinterestApi api = new PinterestApi(acc);
		List<Pinner> trashPinners = api.getUnfollowList(minFollowers);
		do {
			try {
				Pinner pinner = trashPinners.get(0);
				api.unfollow(pinner);
				trashPinners.remove(0);
			} catch (Exception e) {
				Log.log("Failed to unfollow  " + e.getMessage());
			}
		} while (!trashPinners.isEmpty() && this.intervalPassed(interval));
		Log.log("Unfollow task ended for " + acc.getUser());
	}

}