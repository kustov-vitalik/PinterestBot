package com.age.pinterest.task;

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
		String history = FileUtill.getFileContents(pathToHistory);
		List<Pinner> followList = api.getFollowList(followParam.getSize());
		for (Pinner p : followList) {
			if (!history.contains(p.getId())) {
				api.follow(p);
				FileUtill.appendToFile(pathToHistory, p.getId() + "\n");
				this.sleep(followParam.getInterval());
			} else {
				System.out.println("Sciping user with id " + p.getId());
			}
		}
	}

	@Override
	public TaskType getType() {
		return TaskType.FOLLOW;
	}

}
