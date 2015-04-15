package com.age.pinterest.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;

import com.age.data.Pin;
import com.age.help.BotPaths;
import com.age.help.FileLogger;
import com.age.help.FileUtill;
import com.age.param.PinParam;
import com.age.pinterest.api.PinterestApi;

public class PinTask extends Task {
	private static final String PINS_LOCATION_URL = BotPaths.ROOT_DIR + "/Users/%s/pins";
	private final PinParam pinParam;

	public PinTask(PinParam pinParam) {
		super(pinParam.getUser().getAccount().getUsername());
		this.pinParam = pinParam;
	}

	@Override
	public void run() {
		ObjectMapper mapper = new ObjectMapper();
		PinterestApi api = new PinterestApi(pinParam.getUser());
		ArrayList<String> pinFiles = FileUtill.getAllFiles(String.format(PINS_LOCATION_URL, pinParam.getUser().getAccount().getUsername()));
		for (String pinFile : pinFiles) {
			try {
				String filePath = pinFile;
				Pin pin = mapper.readValue(new File(filePath), Pin.class);
				api.pin(pin, pinParam.getBoard());
				new File(filePath).delete();
				logger.log("Remaining pins " + pinFiles.size());
				this.sleep(pinParam.getInterval());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.log("No more pins for " + pinParam.getUser().getAccount().getUsername());
	}

	@Override
	public TaskType getType() {
		return TaskType.PIN;
	}

}
