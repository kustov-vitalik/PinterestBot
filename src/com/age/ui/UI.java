package com.age.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.age.dataframes.AddAccountFrame;
import com.age.dataframes.GeneratePinsDataFrame;
import com.age.dataframes.ScrapeDataFrame;
import com.age.pinterest.bot.PinBot;
import com.age.pinterest.bot.Scheduler;

public class UI implements ActionListener, KeyListener {
	private static final Dimension mainSize = new Dimension(300, 300);
	private static final Dimension btnSize = new Dimension((int) (mainSize.width * 0.9f), (int) (mainSize.height * 0.1f));

	private final JFrame mainFrame;
	private final JButton addAccBtn;
	private final JButton scrapeButton;
	private final JButton genBasicPinsButton;
	private final JButton dashboard;
	private final Scheduler scheduler;

	public UI(Scheduler scheduler) {
		this.scheduler = scheduler;
		mainFrame = new JFrame("Pinterest bot");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 1));
		addAccBtn = new JButton();
		addAccBtn.addActionListener(this);
		addAccBtn.setText("Add Account");
		addAccBtn.setSize(btnSize);
		addAccBtn.setPreferredSize(btnSize);
		panel.add(addAccBtn);

		dashboard = new JButton();
		dashboard.setText("User Dashboard");
		dashboard.setSize(btnSize);
		dashboard.setPreferredSize(btnSize);
		dashboard.addActionListener(this);
		panel.add(dashboard);

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

		mainFrame.setResizable(false);
		mainFrame.add(panel);
		mainFrame.setSize(mainSize);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		mainFrame.setFocusable(true);
		mainFrame.addKeyListener(this);
		mainFrame.pack();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		if (e.getActionCommand().equals(addAccBtn.getText())) {
			new AddAccountFrame(addAccBtn, scheduler);
		} else if (e.getActionCommand().equals(scrapeButton.getText())) {
			new ScrapeDataFrame(scrapeButton, scheduler);
		} else if (e.getActionCommand().equals(genBasicPinsButton.getText())) {
			new GeneratePinsDataFrame(genBasicPinsButton, scheduler);
		} else if (e.getActionCommand().equals(dashboard.getText())) {
			new UserDashboard();
		}
	}

	public static void main(String[] args) {
		PinBot.setUpDirTree();
		new UI(new Scheduler());
	}

	@Override
	public void keyReleased(KeyEvent k) {
	}

	@Override
	public void keyTyped(KeyEvent k) {
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
