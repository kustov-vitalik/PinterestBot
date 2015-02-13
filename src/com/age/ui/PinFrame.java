package com.age.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.age.pinterest.bot.PinBot;

@SuppressWarnings("serial")
public class PinFrame extends JFrame implements ActionListener {

	private static final Dimension mainSize = new Dimension(500, 500);
	private static final Dimension textSize = new Dimension((int) (mainSize.width * 0.3f), (int) (mainSize.height * 0.04f));
	private static final Dimension btnSize = new Dimension((int) (mainSize.width * 0.9f), (int) (mainSize.height * 0.05f));

	private final JComboBox<String> users;
	private final JButton start;
	private final JTextArea interval;
	private final JTextArea board;

	public PinFrame() {
		this.setTitle("Pin");
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		users = new JComboBox<String>();
		List<String> usersItems = PinBot.listAccount();
		for (String s : usersItems) {
			users.addItem(s);
		}
		panel.add(users);

		interval = new JTextArea();
		interval.setSize(textSize);
		interval.setPreferredSize(textSize);
		interval.setToolTipText("Interval");
		interval.setText("Minutes");
		panel.add(interval);

		board = new JTextArea();
		board.setSize(textSize);
		board.setPreferredSize(textSize);
		board.setText("BOARD");
		board.setToolTipText("Count");
		panel.add(board);

		start = new JButton();
		start.setText("start");
		start.setSize(btnSize);
		start.setPreferredSize(btnSize);
		start.addActionListener(this);
		panel.add(start);

		this.add(panel);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if (a.getActionCommand().equals("start")) {
			PinBot bot = new PinBot();
			String user = (String) this.users.getSelectedItem();
			String boardStr = board.getText();
			long time = Long.parseLong(interval.getText());
			time *= 1000 * 60;
			try {
				bot.addPinTask(user, boardStr, time);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}