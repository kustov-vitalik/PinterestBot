package com.age.pinterest.task;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import com.age.data.PinterestAccount;
import com.age.help.BotPaths;
import com.age.pinterest.api.PinterestApi;

public class AddAccountTask extends Task {

	private final PinterestAccount account;

	public AddAccountTask(PinterestAccount account) {
		this.account = account;
	}

	@Override
	public void run() {
		PinterestApi api = new PinterestApi(account);
		String root = BotPaths.USER_ROOT + account.getUsername();
		File rootDir = new File(root);
		rootDir.mkdirs();
		File pinDir = new File(rootDir, "pins");
		pinDir.mkdir();
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File(rootDir, "user.json"), api.getManagedUser());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public TaskType getType() {
		return TaskType.ADD_ACCOUNT;
	}

}
