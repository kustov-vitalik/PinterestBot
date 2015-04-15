package com.age.pinterest.task;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import com.age.help.BotPaths;
import com.age.param.RefreshParam;
import com.age.pinterest.api.PinterestApi;

public class RefreshUserTask extends Task {
	private final RefreshParam refreshParam;

	public RefreshUserTask(RefreshParam refreshParam) {
		super(refreshParam.getAccount().getUsername());
		this.refreshParam = refreshParam;
	}

	@Override
	public void run() {
		PinterestApi api = new PinterestApi(refreshParam.getAccount());
		String rootPath = BotPaths.USER_ROOT + refreshParam.getAccount().getUsername();
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
