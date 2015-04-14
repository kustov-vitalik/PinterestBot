package com.age.dataframes;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
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

	public FollowDataFrame(User user, Scheduler scheduler) {
		this.user = user;
		this.scheduler = scheduler;

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		countArea = new JTextArea();
		countArea.setPreferredSize(dim);
		countArea.setText("count");
		countArea.setToolTipText("count");

		intervalArea = new JTextArea();
		intervalArea.setPreferredSize(dim);
		intervalArea.setText("11");
		intervalArea.setToolTipText("Interval in seconds");

		startBtn = new JButton();
		startBtn.setPreferredSize(dim);
		startBtn.setText("Start");
		startBtn.setToolTipText("Start");
		startBtn.addActionListener(this);

		panel.add(countArea);
		panel.add(intervalArea);
		panel.add(startBtn);

		this.add(panel);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if (a.getActionCommand().equals(startBtn.getText())) {
			int count = Integer.parseInt(countArea.getText());
			long interval = Integer.parseInt(intervalArea.getText());
			interval *= 1000;
			FollowParam followParam = new FollowParam(user, count, interval);
			scheduler.schedule(followParam);
			this.dispose();
		}
	}

}
