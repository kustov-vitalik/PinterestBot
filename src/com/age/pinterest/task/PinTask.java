package com.age.pinterest.task;

import java.io.File;
import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;

import com.age.data.Board;
import com.age.data.Pin;
import com.age.data.PinterestAccount;
import com.age.help.BotPaths;
import com.age.help.FileUtill;
import com.age.pinterest.api.PinterestApi;
import com.age.ui.Log;

public class PinTask extends Task {
	private static final String PINS_LOCATION_URL = BotPaths.ROOT_DIR + "/Users/%s/pins";
	private final long interval;
	private Board board;
	private final PinterestAccount acc;

	public PinTask(PinterestAccount acc, Board board, long interval) {
		this.interval = interval;
		this.board = board;
		this.acc = acc;

	}

	@Override
	public void run() {
		PinterestApi api = new PinterestApi(acc);
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<String> pinFiles = FileUtill.getAllFiles(String.format(PINS_LOCATION_URL, acc.getUser()));
		System.out.println("Pinning to " + board.getName());
		System.out.println("Interval " + interval);
		do {
			try {
				String filePath = pinFiles.get(0);
				Pin pin = mapper.readValue(new File(filePath), Pin.class);
				api.pin(pin, board);
				new File(filePath).delete();
				pinFiles.remove(0);
			} catch (Exception e) {
				Log.log("Failed to pin  " + e.getMessage());
			}
		} while (!pinFiles.isEmpty() && this.intervalPassed(interval));
		Log.log("No more pins for " + acc.getUser());
	}

}
