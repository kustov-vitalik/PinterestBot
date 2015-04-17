package com.age.pinterest.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.codehaus.jackson.map.ObjectMapper;

import com.age.data.Pin;
import com.age.help.BotPaths;
import com.age.help.FileUtill;
import com.age.param.PinParam;
import com.age.pinterest.api.PinterestApi;

public class PinTask extends Task {
	private static final String PINS_LOCATION_URL = BotPaths.USER_ROOT + "%s/pins";
	private final PinParam pinParam;

	public PinTask(PinParam pinParam) {
		super(pinParam.getUser().getAccount().getUsername());
		this.pinParam = pinParam;
	}

	@Override
	public void run() {
		logger.log("Starting pin task for user " + pinParam.getUser().getAccount().getUsername());
		ObjectMapper mapper = new ObjectMapper();
		PinterestApi api = new PinterestApi(pinParam.getUser());
		ArrayList<String> pinFiles = FileUtill.getAllFiles(String.format(PINS_LOCATION_URL, pinParam.getUser().getAccount().getUsername()));
		logger.log("There are " + pinFiles.size() + " pins in pin folder.");
		Iterator<String> iter = pinFiles.iterator();
		while (iter.hasNext()) {
			try {
				String filePath = iter.next();
				Pin pin = mapper.readValue(new File(filePath), Pin.class);
				api.pin(pin, pinParam.getBoard());
				new File(filePath).delete();
				iter.remove();
				logger.log("Remaining pins " + pinFiles.size());
				this.sleep(pinParam.getInterval());
			} catch (IOException e) {
				logger.log("Failed to read pin from file", e);
			}
		}
		logger.log("No more pins for " + pinParam.getUser().getAccount().getUsername());
	}

	@Override
	public TaskType getType() {
		return TaskType.PIN;
	}

}
