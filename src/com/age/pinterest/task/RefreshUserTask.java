package com.age.pinterest.task;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import com.age.data.User;
import com.age.help.BotPaths;
import com.age.pinterest.api.PinterestApi;

public class RefreshUserTask extends Task {
	private final User user;

	public RefreshUserTask(User user) {
		this.user = user;
	}

	@Override
	public void run() {
		PinterestApi api = new PinterestApi(user.getAccount());
		String rootPath = BotPaths.USER_ROOT + user.getAccount().getUsername();
		ObjectMapper mapper = new ObjectMapper();
		File rootDir = new File(rootPath);
		try {
			mapper.writeValue(new File(rootDir, "user.json"), api.getManagedUser());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public TaskType getType() {
		return TaskType.REFRESH;
	}

}
