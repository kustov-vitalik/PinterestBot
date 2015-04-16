package com.age.ui;

import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.age.data.User;
import com.age.pinterest.bot.PinBot;

@SuppressWarnings("serial")
public class UserDashboard extends JFrame {
	private static final int buttonW = 100;
	private static final int buttonH = 30;
	private static final List<String> tasks = Arrays.asList("Follow", "Unfollow", "Pin", "Repin", "Refresh");

	public UserDashboard() {
		int rows = 15, cols = tasks.size();
		this.setTitle("Matrix");
		JPanel panel = new JPanel(new GridLayout(rows, cols));
		List<User> users = PinBot.listUsers();
		for (User user : users) {
			panel.add(new UserPanel(user.getAccount().getUsername(), buttonW, buttonH));
		}
		this.add(panel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

}
