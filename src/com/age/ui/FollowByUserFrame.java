package com.age.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.age.pinterest.bot.PinBot;

@SuppressWarnings("serial")
public class FollowByUserFrame extends JFrame implements ActionListener {
	private static final Dimension mainSize = new Dimension(500, 500);
	private static final Dimension textSize = new Dimension((int) (mainSize.width * 0.3f), (int) (mainSize.height * 0.04f));
	private static final Dimension btnSize = new Dimension((int) (mainSize.width * 0.9f), (int) (mainSize.height * 0.05f));

	private final JComboBox<String> users;
	private final JButton start;
	private final JTextArea keyword;
	private final JTextArea interval;

	// private final JTextArea num;

	public FollowByUserFrame() {
		this.setTitle("Follow by user");
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
		interval.setPreferredSize(textSize);
		interval.setToolTipText("Interval");
		interval.setText("seconds");

		keyword = new JTextArea();
		keyword.setSize(textSize);
		keyword.setPreferredSize(textSize);
		keyword.setText("target user");
		keyword.setToolTipText("target user");

		// num = new JTextArea();
		// num.setSize(textSize);
		// num.setPreferredSize(textSize);
		// num.setText("5000");
		// num.setToolTipText("Count");

		panel.add(users);
		panel.add(keyword);
		panel.add(interval);
		// panel.add(num);
		panel.add(start);
		this.add(panel);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("start")) {
			String username = (String) users.getSelectedItem();
			PinBot bot = new PinBot();
			long time = Long.parseLong(interval.getText());
			time *= 1000;
			String word = keyword.getText();
			bot.addFollowByUserTaks(username, word, time);
		}

	}
}
