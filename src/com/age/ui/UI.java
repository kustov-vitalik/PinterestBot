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
	private static final Dimension btnSize = new Dimension((int) (mainSize.width * 0.9f), (int) (mainSize.height * 0.05f));

	private final JFrame mainFrame;
	private final JButton addAccBtn;
	private final JButton unfollowTask;
	private final JButton followTask;
	private final JButton pinTask;
	private final JButton followByUserButton;
	private final JButton scrapeButton;
	private final JButton genBasicPinsButton;

	public UI() {
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

		followByUserButton = new JButton();
		followByUserButton.addActionListener(this);
		followByUserButton.setText("Follow By User");
		followByUserButton.setSize(btnSize);
		followByUserButton.setPreferredSize(btnSize);
		panel.add(followByUserButton);

		pinTask = new JButton();
		pinTask.addActionListener(this);
		pinTask.setText("Pin");
		pinTask.setSize(btnSize);
		pinTask.setPreferredSize(btnSize);
		panel.add(pinTask);

		scrapeButton = new JButton();
		scrapeButton.addActionListener(this);
		scrapeButton.setText("Scrape");
		scrapeButton.setSize(btnSize);
		scrapeButton.setPreferredSize(btnSize);
		panel.add(scrapeButton);

		genBasicPinsButton = new JButton();
		genBasicPinsButton.addActionListener(this);
		genBasicPinsButton.setText("Generate Pins");
		genBasicPinsButton.setSize(btnSize);
		genBasicPinsButton.setPreferredSize(btnSize);
		panel.add(genBasicPinsButton);

		mainFrame.add(panel);
		mainFrame.setSize(mainSize);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);

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
		} else if (e.getActionCommand().equals("Follow By User")) {
			new FollowByUserFrame();
		} else if (e.getActionCommand().equals("Scrape")) {
			new ScrapeFrame();
		} else if (e.getActionCommand().equals("Generate Pins")) {
			new GenerateBasicPinsFrame();
		}
	}

	public static void main(String[] args) {
		new UI();
	}
}
