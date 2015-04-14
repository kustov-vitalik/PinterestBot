package com.age.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.age.data.User;
import com.age.dataframes.FollowDataFrame;
import com.age.dataframes.PinDataFrame;
import com.age.dataframes.RepinDataFrame;
import com.age.dataframes.UnfollowDataFrame;
import com.age.param.RefreshParam;
import com.age.pinterest.bot.PinBot;
import com.age.pinterest.bot.Scheduler;
import com.age.pinterest.task.TaskType;

@SuppressWarnings("serial")
public class UserRow extends JPanel implements ActionListener {
	List<TaskType> tasks = Arrays.asList(TaskType.FOLLOW, TaskType.UNFOLLOW, TaskType.PIN, TaskType.REPIN, TaskType.REFRESH);
	private final String username;
	private final Scheduler scheduler;

	public UserRow(String username, Scheduler scheduler, int w, int h) {
		this.username = username;
		this.scheduler = scheduler;
		this.setLayout(new GridLayout(1, tasks.size()));
		JTextField textArea = new JTextField();
		textArea.setPreferredSize(new Dimension(w * 2, h));
		textArea.setText(username);
		textArea.setOpaque(false);
		textArea.setEditable(false);
		textArea.setFont(new Font("Verdana", Font.BOLD, 16));
		textArea.setHorizontalAlignment(JTextField.CENTER);
		this.add(textArea);

		for (TaskType type : tasks) {
			JButton btn = new JButton();
			btn.setText(type.toString());
			btn.setPreferredSize(new Dimension(w, h));
			btn.addActionListener(this);
			this.add(btn);
		}
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		User user = PinBot.getUser(username);
		String cmd = a.getActionCommand();
		if (cmd.equals(TaskType.REPIN.toString())) {
			new RepinDataFrame(scheduler, user);
		} else if (cmd.equals(TaskType.FOLLOW.toString())) {
			new FollowDataFrame(user, scheduler);
		} else if (cmd.equals(TaskType.UNFOLLOW.toString())) {
			new UnfollowDataFrame(user, scheduler);
		} else if (cmd.equals(TaskType.PIN.toString())) {
			new PinDataFrame(user, scheduler);
		} else if (cmd.equals(TaskType.REFRESH.toString())) {
			scheduler.schedule(new RefreshParam(user.getAccount()));
		}
	}
	private void handleTask()
	{
		
	}

}
