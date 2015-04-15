package com.age.ui;

import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.age.data.User;
import com.age.pinterest.bot.PinBot;

@SuppressWarnings("serial")
public class UserMatrix extends JFrame {
	private static final int buttonW = 60;
	private static final int buttonH = 30;
	private static final List<String> tasks = Arrays.asList("Follow", "Unfollow", "Pin", "Repin", "Refresh");

	public UserMatrix() {
		int rows = 15, cols = tasks.size();
		this.setTitle("Matrix");
		JPanel panel = new JPanel(new GridLayout(rows, cols));
		List<User> users = PinBot.listUsers();
		for (User user : users) {
			panel.add(new UserRow(user.getAccount().getUsername(), buttonW, buttonH));
		}
		this.add(panel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

}
