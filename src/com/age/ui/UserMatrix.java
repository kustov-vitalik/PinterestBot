package com.age.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class UserMatrix extends JFrame {
	private static final int buttonW = 100;
	private static final int buttonH = 40;
	private static final List<String> tasks = Arrays.asList("Follow", "Unfollow", "Pin", "Repin", "Refresh");

	public UserMatrix() {
		int rows = 15, cols = tasks.size();
		this.setTitle("Matrix");
		JPanel panel = new JPanel(new GridLayout(rows, cols));
		for (int i = 0; i < rows; i++) {
			panel.add(new UserRow("userewrfwefwefwef", buttonW, buttonH));
		}
		this.add(panel);
		this.pack();
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new UserMatrix();
	}
}
