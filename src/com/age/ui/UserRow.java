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

import com.age.pinterest.bot.PinBot;
import com.age.pinterest.task.TaskType;

@SuppressWarnings("serial")
public class UserRow extends JPanel implements ActionListener {
	List<TaskType> tasks = Arrays.asList(TaskType.FOLLOW, TaskType.UNFOLLOW, TaskType.PIN, TaskType.REPIN, TaskType.REFRESH);
	private final String username;

	public UserRow(String username, int w, int h) {
		this.username = username;
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
		PinBot.getUser(username);
		
	}

}
