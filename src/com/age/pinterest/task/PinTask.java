package com.age.pinterest.task;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.lang3.Validate;
import org.codehaus.jackson.map.ObjectMapper;

import com.age.data.Board;
import com.age.data.PinData;
import com.age.data.User;
import com.age.help.BotPaths;
import com.age.help.FileUtill;
import com.age.pinterest.api.PinterestApi;
import com.age.ui.Log;

public class PinTask extends Task {
	private static final String PINS_LOCATION_URL = BotPaths.ROOT_DIR + "/Users/%s/pins";
	private final long interval;
	private Board board;
	private final User user;

	public PinTask(User user, Board board, long interval) {
		Validate.notNull(board, "Null board in Pin task");
		this.interval = interval;
		this.board = board;
		this.user = user;

	}

	@Override
	public void run() {
		ObjectMapper mapper = new ObjectMapper();
		PinterestApi api = new PinterestApi(user);
		ArrayList<String> pinFiles = FileUtill.getAllFiles(String.format(PINS_LOCATION_URL, user.getAccount().getUser()));
		do {
			if (this.intervalPassed(interval)) {
				try {
					String filePath = pinFiles.get(0);
					PinData pin = mapper.readValue(new File(filePath), PinData.class);
					api.pin(pin, board);
					new File(filePath).delete();
					pinFiles.remove(0);
					Log.log("Remaining pins " + pinFiles.size());
					Log.log("Pin interval is " + interval);
				} catch (Exception e) {
					Log.log("Failed to pin  " + e.getMessage());
				}
			}
		} while (!pinFiles.isEmpty());
		Log.log("No more pins for " + user.getAccount().getUser());
	}

	@Override
	public TaskType getType() {
		return TaskType.PIN;
	}

}
