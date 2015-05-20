package com.age.pinterest.bot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.age.data.User;
import com.age.data.UserConfig;
import com.age.help.BotPaths;

public class PinBot {
	public static void setUpDirTree() {
		File root = new File(BotPaths.ROOT_DIR);
		root.mkdirs();
		root = new File(BotPaths.USER_ROOT);
		root.mkdirs();
		root = new File(BotPaths.PINS_ROOT);
		root.mkdirs();
		root = new File(BotPaths.IMAGES_ROOT);
		root.mkdirs();
		root = new File(BotPaths.LOGS);
		root.mkdirs();
	}


	public static List<User> listUsers() {

		List<User> users = new ArrayList<User>();
		File rootDir = new File(BotPaths.USER_ROOT);
		ObjectMapper mapper = new ObjectMapper();
		for (File f : rootDir.listFiles()) {
			try {
				File user = new File(f, "user.json");
				users.add(mapper.readValue(user, User.class));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return users;
	}

	public static User getUser(String username) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			File usersRoot = new File(BotPaths.USER_ROOT);
			File userFile = new File(usersRoot, username + "/user.json");
			return mapper.readValue(userFile, User.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new User();
	}
	

	public static UserConfig getUserConfig(String user) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		File usersRoot = new File(BotPaths.USER_ROOT);
		File cfgFile = new File(usersRoot, user + "/config.json");
		return mapper.readValue(cfgFile, UserConfig.class);
	}
}
