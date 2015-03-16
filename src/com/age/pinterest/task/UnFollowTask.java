package com.age.pinterest.task;

import java.util.List;

import org.apache.log4j.Logger;

import com.age.data.Pinner;
import com.age.data.PinterestAccount;
import com.age.pinterest.api.PinterestApi;

public class UnFollowTask extends Task {
	private static final Logger logger = Logger.getLogger(UnFollowTask.class);
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
		while (!trashPinners.isEmpty()) {
			if (this.intervalPassed(interval)) {
				try {
					Pinner pinner = trashPinners.get(0);
					api.unfollow(pinner);
					trashPinners.remove(0);
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		}
	}

	@Override
	public Logger getLog() {
		return logger;
	}

}