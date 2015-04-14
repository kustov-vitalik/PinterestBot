package com.age.dataframes;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
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

	public UnfollowDataFrame(User user, Scheduler scheduler) {
		this.user = user;
		this.scheduler = scheduler;

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		minFollowersArea = new JTextArea();
		minFollowersArea.setPreferredSize(dim);
		minFollowersArea.setText("min followers");
		minFollowersArea.setToolTipText("min followers");

		intervalArea = new JTextArea();
		intervalArea.setPreferredSize(dim);
		intervalArea.setText("11");
		intervalArea.setToolTipText("Interval in seconds");

		startBtn = new JButton();
		startBtn.setPreferredSize(dim);
		startBtn.setText("Start");
		startBtn.setToolTipText("Start");
		startBtn.addActionListener(this);

		panel.add(minFollowersArea);
		panel.add(intervalArea);
		panel.add(startBtn);

		this.add(panel);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.pack();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(startBtn.getText())) {
			int minFollowers = Integer.parseInt(minFollowersArea.getText());
			long interval = Integer.parseInt(intervalArea.getText());
			interval *= 1000;
			UnfollowParam unfollowParam = new UnfollowParam(user, minFollowers, interval);
			scheduler.schedule(unfollowParam);
			this.dispose();
		}
	}

}
