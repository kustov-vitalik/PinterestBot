package com.age.pinterest.task;

import java.util.Iterator;
import java.util.List;

import com.age.data.Pinner;
import com.age.help.BotPaths;
import com.age.help.FileUtill;
import com.age.param.FollowParam;
import com.age.pinterest.api.PinterestApi;

public class FollowTask extends Task {

	private static final String FOLLOW_HISTORY_PATH = BotPaths.USER_ROOT + "/%s/followed.txt";

	private final FollowParam followParam;

	public FollowTask(FollowParam followParam) {
		super(followParam.getUser().getAccount().getUsername());
		this.followParam = followParam;
	}

	@Override
	public void run() {
		PinterestApi api = new PinterestApi(followParam.getUser());
		String pathToHistory = String.format(FOLLOW_HISTORY_PATH, followParam.getUser().getAccount().getUsername());
		List<Pinner> followList = api.getFollowList(followParam.getSize());
		Iterator<Pinner> iter = followList.iterator();
		while (iter.hasNext()) {
			Pinner p = iter.next();
			if (!FileUtill.searchFile(pathToHistory ,p.getId())) {
				api.follow(p);
				FileUtill.appendToFile(pathToHistory, p.getId() + "\n");
				this.sleep(followParam.getInterval());
			} else {
				logger.log("Sciping user with id " + p.getId());
			}
			iter.remove();
			logger.log("Remaining to follow " + followList.size());
		}
		logger.log("Follow Task ended");
	}

	@Override
	public TaskType getType() {
		return TaskType.FOLLOW;
	}

}
