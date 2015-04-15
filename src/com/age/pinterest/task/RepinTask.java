package com.age.pinterest.task;

import java.util.List;

import com.age.data.Pin;
import com.age.param.RepinParam;
import com.age.pinterest.api.PinterestApi;

public class RepinTask extends Task {

	private static final String REPIN_HISTORY_PATH = "";
	private final RepinParam repinParam;

	public RepinTask(RepinParam repinParam) {
		super(repinParam.getUser().getAccount().getUsername());
		this.repinParam = repinParam;
	}

	@Override
	public void run() {
		PinterestApi api = new PinterestApi(repinParam.getUser());
		List<String> pinIds = api.searchPins(repinParam.getKeyword(), repinParam.getRepinCount());
		for (String id : pinIds) {
			String newPinId = api.repin(repinParam.getBoard(), id, "");
			System.out.println("Repining pin:  " + id);
			Pin pin = api.getPinInfo(id);
			api.editPin(repinParam.getBoard(), newPinId, pin.getDescription(), repinParam.getLink());
			this.sleep(repinParam.getInterval());
		}
	}

	@Override
	public TaskType getType() {
		return TaskType.REPIN;
	}

}
