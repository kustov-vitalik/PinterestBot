package com.age.pinterest.task;

import java.util.List;

import com.age.data.PinData;
import com.age.param.Repin;
import com.age.pinterest.api.PinterestApi;

public class RepinTask extends Task {

	private final Repin repinData;

	public RepinTask(Repin repinData) {
		this.repinData = repinData;
	}

	@Override
	public void run() {
		PinterestApi api = new PinterestApi(repinData.getUser());
		List<String> pinIds = api.searchPins(repinData.getKeyword(), repinData.getRepinCount());
		for (String id : pinIds) {
			String newPinId = api.repin(repinData.getBoard(), id, "");
			PinData pin = api.getPinInfo(id);
			api.editPin(repinData.getBoard(), newPinId, pin.getDescription(), repinData.getLink());
			this.sleep(repinData.getInterval());
		}
	}

	@Override
	public TaskType getType() {
		return TaskType.REPIN;
	}

}
