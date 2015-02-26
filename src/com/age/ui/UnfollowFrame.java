package com.age.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.json.JSONException;

import com.age.pinterest.bot.PinBot;
import com.age.pinterest.task.Task;

@SuppressWarnings("serial")
public class UnfollowFrame extends JFrame implements ActionListener {
	private static final Logger logger = Logger.getLogger(UnfollowFrame.class);
	private static final Dimension mainSize = new Dimension(500, 500);
	private static final Dimension textSize = new Dimension((int) (mainSize.width * 0.3f), (int) (mainSize.height * 0.04f));
	private static final Dimension btnSize = new Dimension((int) (mainSize.width * 0.9f), (int) (mainSize.height * 0.05f));

	private final JComboBox<String> users;
	private final JButton start;
	private final JTextArea minFollowers;
	private final JTextArea interval;

	public UnfollowFrame() {
		this.setTitle("Unfollow");
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		users = new JComboBox<String>();
		List<String> usersItems = PinBot.listAccount();
		for (String s : usersItems) {
			users.addItem(s);
		}
		start = new JButton();
		start.setText("start");
		start.setSize(btnSize);
		start.setPreferredSize(btnSize);
		start.addActionListener(this);

		interval = new JTextArea();
		interval.setSize(textSize);
		interval.setToolTipText("Interval");
		interval.setPreferredSize(textSize);
		interval.setText("seconds");

		minFollowers = new JTextArea();
		minFollowers.setSize(textSize);
		minFollowers.setPreferredSize(textSize);
		minFollowers.setToolTipText("Min followers");
		minFollowers.setText("5000");

		panel.add(users);
		panel.add(minFollowers);
		panel.add(interval);
		panel.add(start);
		this.add(panel);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.pack();
		this.setResizable(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("start")) {
			String username = (String) users.getSelectedItem();
			PinBot bot = new PinBot();
			long time = Long.parseLong(interval.getText());
			time *= 1000;
			int min = Integer.parseInt(minFollowers.getText());
			try {
			bot.addUnfollowTask(username, min, time);
			} catch (IOException | JSONException | InterruptedException e1) {
				logger.error("Failed to start unfollow task", e1);
			}

		}

	}
}
