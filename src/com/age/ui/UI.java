package com.age.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class UI implements ActionListener {
	private static final Dimension mainSize = new Dimension(500, 500);
	private static final Dimension textSize = new Dimension((int) (mainSize.width * 0.3f), (int) (mainSize.height * 0.04f));
	private static final Dimension btnSize = new Dimension((int) (mainSize.width * 0.9f), (int) (mainSize.height * 0.05f));

	private JFrame mainFrame;
	private JButton addAccBtn;
	private JButton unfollowTask;
	private JButton followTask;
	private JButton pinTask;

	public UI() {
		init();
	}

	private void init() {
		mainFrame = new JFrame("Pinterest bot");
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		addAccBtn = new JButton();
		addAccBtn.addActionListener(this);
		addAccBtn.setText("Add Account");
		addAccBtn.setSize(btnSize);
		addAccBtn.setPreferredSize(btnSize);
		panel.add(addAccBtn);

		unfollowTask = new JButton();
		unfollowTask.addActionListener(this);
		unfollowTask.setText("Unfollow");
		unfollowTask.setSize(btnSize);
		unfollowTask.setPreferredSize(btnSize);
		panel.add(unfollowTask);

		followTask = new JButton();
		followTask.addActionListener(this);
		followTask.setText("Follow");
		followTask.setSize(btnSize);
		followTask.setPreferredSize(btnSize);
		panel.add(followTask);

		pinTask = new JButton();
		pinTask.addActionListener(this);
		pinTask.setText("Pin");
		pinTask.setSize(btnSize);
		pinTask.setPreferredSize(btnSize);
		panel.add(pinTask);

		mainFrame.add(panel);
		mainFrame.setSize(mainSize);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);

	}

	public static void main(String[] args) {
		new UI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Add Account")) {
			new AddAccountFrame();
		} else if (e.getActionCommand().equals("Unfollow")) {
			new UnfollowFrame();
		} else if (e.getActionCommand().equals("Follow")) {
			new FollowFrame();
		} else if (e.getActionCommand().equals("Pin")) {
			new PinFrame();
		}
	}

}
