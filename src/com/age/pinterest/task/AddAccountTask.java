package com.age.pinterest.task;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import com.age.help.BotPaths;
import com.age.param.AddAccountParam;
import com.age.pinterest.api.PinterestApi;
import com.age.ui.UI;

public class AddAccountTask extends Task {

	private final AddAccountParam addAccountParam;

	public AddAccountTask(AddAccountParam addAccountParam) {
		super(addAccountParam.getAccount().getUsername());
		this.addAccountParam = addAccountParam;
	}

	@Override
	public void run() {
		UI.syslog.log("Adding account " + addAccountParam.getAccount().getEmail());
		PinterestApi api = new PinterestApi(addAccountParam.getAccount());
		String root = BotPaths.USER_ROOT + addAccountParam.getAccount().getUsername();
		File rootDir = new File(root);
		rootDir.mkdirs();
		File pinDir = new File(rootDir, "pins");
		pinDir.mkdir();
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File(rootDir, "user.json"), api.getManagedUser());
		} catch (IOException e) {
			UI.syslog.log("Failed to add account " + addAccountParam.getAccount().getEmail(), e);
		}

		UI.syslog.log("Account " + addAccountParam.getAccount().getEmail() + " was added");

	}

	@Override
	public TaskType getType() {
		return TaskType.ADD_ACCOUNT;
	}

}
