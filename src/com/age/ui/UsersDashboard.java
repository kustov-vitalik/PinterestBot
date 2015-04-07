package com.age.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.age.data.PinterestAccount;
import com.age.pinterest.bot.PinBot;

@SuppressWarnings("serial")
public class UsersDashboard extends JFrame implements ActionListener {
	private final JFrame mainFrame;
	private static final int width = 500;
	private static final int height = 200;

	public UsersDashboard(PinBot bot, KeyListener keyListener) {
		mainFrame = new JFrame("User board");
		mainFrame.setLayout(null);
		mainFrame.setResizable(false);

		JPanel mainPanel = new JPanel();
//		for (PinterestAccount acc : PinBot.listAccount()) {
//			mainPanel.add(new UserPanel(bot, acc, width, height / 4));
//		}

		mainPanel.setLayout(new GridLayout(PinBot.listAccount().size(), 1));
		mainPanel.setSize(width, height);

		JScrollPane scrollPane = new JScrollPane(mainPanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(0, 0, width, height);

		JPanel contentPane = new JPanel(null);
		contentPane.setPreferredSize(new Dimension(width, height));
		contentPane.add(scrollPane);

		mainFrame.setContentPane(contentPane);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		mainFrame.setFocusable(true);
		mainFrame.addKeyListener(keyListener);
		mainFrame.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
	}

}
