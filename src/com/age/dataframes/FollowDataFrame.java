package com.age.dataframes;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JTextArea;

import com.age.data.User;
import com.age.param.FollowParam;
import com.age.pinterest.bot.Scheduler;

@SuppressWarnings("serial")
public class FollowDataFrame extends DataFrame {

	private final Scheduler scheduler;
	private final User user;
	private final JTextArea countArea;
	private final JTextArea intervalArea;
	private final JButton startBtn;

	public FollowDataFrame(User user, Scheduler scheduler, JButton triggerBtn) {
		super(triggerBtn);
		this.user = user;
		this.scheduler = scheduler;

		countArea = this.addTextArea("500", "count");
		intervalArea = this.addTextArea("11", "seconds");
		startBtn = this.addButton("Start");
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if (a.getActionCommand().equals(startBtn.getText())) {
			int count = Integer.parseInt(countArea.getText());
			long interval = Integer.parseInt(intervalArea.getText());
			interval *= 1000;
			FollowParam followParam = new FollowParam(user, count, interval);
			scheduler.schedule(followParam);
			this.trunOnBtn();
			this.dispose();
		}
	}

}
