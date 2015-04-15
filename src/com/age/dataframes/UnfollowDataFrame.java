package com.age.dataframes;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JTextArea;

import com.age.data.User;
import com.age.param.UnfollowParam;
import com.age.pinterest.bot.Scheduler;

@SuppressWarnings("serial")
public class UnfollowDataFrame extends DataFrame {

	private final User user;
	private final Scheduler scheduler;
	private final JButton startBtn;
	private final JTextArea intervalArea;
	private final JTextArea minFollowersArea;

	public UnfollowDataFrame(User user, Scheduler scheduler, JButton triggerBtn) {
		super(triggerBtn);
		this.user = user;
		this.scheduler = scheduler;
		minFollowersArea = this.addTextArea("5000", "min followers");
		intervalArea = this.addTextArea("11", "Interval in seconds");
		startBtn = this.addButton("Start");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(startBtn.getText())) {
			int minFollowers = Integer.parseInt(minFollowersArea.getText());
			long interval = Integer.parseInt(intervalArea.getText());
			interval *= 1000;
			UnfollowParam unfollowParam = new UnfollowParam(user, minFollowers, interval);
			scheduler.schedule(unfollowParam);
			this.trunOnBtn();
			this.dispose();
		}
	}

}
