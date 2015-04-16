package com.age.pinterest.task;

import java.util.List;

import com.age.data.Pin;
import com.age.help.BotPaths;
import com.age.help.FileUtill;
import com.age.param.RepinParam;
import com.age.pinterest.api.PinterestApi;

public class RepinTask extends Task {

	private static final String REPIN_HISTORY_FORMAT = BotPaths.USER_ROOT + "%s/repinHistory.txt";
	private final RepinParam repinParam;

	public RepinTask(RepinParam repinParam) {
		super(repinParam.getUser().getAccount().getUsername());
		this.repinParam = repinParam;
	}

	@Override
	public void run() {
		PinterestApi api = new PinterestApi(repinParam.getUser());
		List<String> pinIds = api.searchPins(repinParam.getKeyword(), repinParam.getRepinCount());
		String pathToHistory = String.format(REPIN_HISTORY_FORMAT, repinParam.getUser().getAccount().getUsername());
		String history = FileUtill.getFileContents(pathToHistory);
		for (String id : pinIds) {
			if (!history.contains(id)) {
				 String newPinId = api.repin(repinParam.getBoard(), id, "");
				 logger.log("Repining pin:  " + id);
				 Pin pin = api.getPinInfo(id);
				 api.editPin(repinParam.getBoard(), newPinId,
				 pin.getDescription(), repinParam.getLink());
				FileUtill.appendToFile(pathToHistory, id + "\n");
				 this.sleep(repinParam.getInterval());
				this.sleep(2000);
			} else {
				System.out.println("Skiping repin with id " + id);
			}
		}
	}

	@Override
	public TaskType getType() {
		return TaskType.REPIN;
	}

}
