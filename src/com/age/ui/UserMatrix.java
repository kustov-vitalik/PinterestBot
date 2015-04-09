package com.age.ui;

import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.age.data.PinterestAccount;
import com.age.pinterest.bot.PinBot;
import com.age.pinterest.bot.Scheduler;

@SuppressWarnings("serial")
public class UserMatrix extends JFrame {
	private static final int buttonW = 100;
	private static final int buttonH = 40;
	private static final List<String> tasks = Arrays.asList("Follow", "Unfollow", "Pin", "Repin", "Refresh");

	public UserMatrix(Scheduler scheduler) {
		int rows = 15, cols = tasks.size();
		this.setTitle("Matrix");
		JPanel panel = new JPanel(new GridLayout(rows, cols));
		List<PinterestAccount> accounts = PinBot.listAccount();
		for (PinterestAccount acc : accounts) {
			panel.add(new UserRow(acc.getUsername(), scheduler, buttonW, buttonH));
		}
		this.add(panel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new UserMatrix(new Scheduler());
	}
}
