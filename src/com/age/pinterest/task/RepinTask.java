package com.age.pinterest.task;

import java.util.Iterator;
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
		logger.log("Starting repin task for user " + repinParam.getUser());
		logger.log("Keyword is " + repinParam.getKeyword());
		PinterestApi api = new PinterestApi(repinParam.getUser());
		List<String> pinIds = api.searchPins(repinParam.getKeyword(), repinParam.getRepinCount());
		String pathToHistory = String.format(REPIN_HISTORY_FORMAT, repinParam.getUser().getAccount().getUsername());
		String history = FileUtill.getFileContents(pathToHistory);
		Iterator<String> iter = pinIds.iterator();
		while (iter.hasNext()) {
			String id = iter.next();
			if (!history.contains(id)) {
				String newPinId = api.repin(repinParam.getBoard(), id, "");
				logger.log("Repining pin:  " + id);
				Pin pin = api.getPinInfo(id);
				api.editPin(repinParam.getBoard(), newPinId, pin.getDescription(), repinParam.getLink());
				FileUtill.appendToFile(pathToHistory, id + "\n");
				this.sleep(repinParam.getInterval());
			} else {
				logger.log("Skiping repin with id " + id);
			}
			iter.remove();
			logger.log("Remaining repins " + pinIds.size());
		}
		logger.log("Repin task ended");
	}

	@Override
	public TaskType getType() {
		return TaskType.REPIN;
	}

}
