package com.age.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
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
	private final List<TaskType> tasks = Arrays.asList(TaskType.FOLLOW, TaskType.UNFOLLOW, TaskType.PIN, TaskType.REPIN, TaskType.REFRESH);
	private List<JButton> taskButtons = new ArrayList<JButton>();
	private final String username;
	private final Scheduler scheduler;
	private JFrame dataFrame;

	public UserRow(String username, int w, int h) {
		this.username = username;
		this.scheduler = new Scheduler();
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
			taskButtons.add(btn);
			this.add(btn);
		}
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		String cmd = a.getActionCommand();
		if (cmd.equals(TaskType.REPIN.toString())) {
			handleTask(TaskType.REPIN);
		} else if (cmd.equals(TaskType.FOLLOW.toString())) {
			handleTask(TaskType.FOLLOW);
		} else if (cmd.equals(TaskType.UNFOLLOW.toString())) {
			handleTask(TaskType.UNFOLLOW);
		} else if (cmd.equals(TaskType.PIN.toString())) {
			handleTask(TaskType.PIN);
		} else if (cmd.equals(TaskType.REFRESH.toString())) {
			handleTask(TaskType.REFRESH);
		}
	}

	private boolean handleTask(TaskType type) {
		Thread task = scheduler.checkForTask(type);
		JButton btn = this.getButtonForTask(type);
		if (task != null) {
			scheduler.terminateTask(type);
			btn.setBackground(new JButton().getBackground());
			return false;
		}
		User user = PinBot.getUser(username);
		handleDataFrame(type, user);
		return true;
	}

	private JButton getButtonForTask(TaskType type) {
		for (JButton btn : taskButtons) {
			if (btn.getText().endsWith(type.toString())) {
				return btn;
			}
		}
		return null;
	}

	private void handleDataFrame(TaskType type, User user) {
		JButton btn = this.getButtonForTask(type);
		if (this.dataFrame != null) {
			this.dataFrame.dispose();
		}
		if (type.equals(TaskType.REPIN)) {
			this.dataFrame = new RepinDataFrame(scheduler, user, btn);
		} else if (type.equals(TaskType.FOLLOW)) {
			this.dataFrame = new FollowDataFrame(user, scheduler, btn);
		} else if (type.equals(TaskType.PIN)) {
			this.dataFrame = new PinDataFrame(user, scheduler, btn);
		} else if (type.equals(TaskType.UNFOLLOW)) {
			this.dataFrame = new UnfollowDataFrame(user, scheduler, btn);
		} else if (type.equals(TaskType.REFRESH)) {
			scheduler.schedule(new RefreshParam(user.getAccount()));
		}
	}
}
