package com.age.pinterest.task;

import java.util.List;

import com.age.data.Pin;
import com.age.param.RepinParam;
import com.age.pinterest.api.PinterestApi;

public class RepinTask extends Task {

	private static final String REPIN_HISTORY_PATH="";
	private final RepinParam repinData;

	public RepinTask(RepinParam repinData) {
		this.repinData = repinData;
	}

	@Override
	public void run() {
		PinterestApi api = new PinterestApi(repinData.getUser());
		List<String> pinIds = api.searchPins(repinData.getKeyword(), repinData.getRepinCount());
		for (String id : pinIds) {
			String newPinId = api.repin(repinData.getBoard(), id, "");
			System.out.println("Repining pin:  " + id);
			Pin pin = api.getPinInfo(id);
			api.editPin(repinData.getBoard(), newPinId, pin.getDescription(), repinData.getLink());
			this.sleep(repinData.getInterval());
		}
	}

	@Override
	public TaskType getType() {
		return TaskType.REPIN;
	}

}
