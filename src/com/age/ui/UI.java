package com.age.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.apache.log4j.PropertyConfigurator;

import com.age.pinterest.bot.PinBot;

public class UI implements ActionListener {
	private static final Dimension mainSize = new Dimension(300, 300);
	private static final Dimension btnSize = new Dimension((int) (mainSize.width * 0.9f), (int) (mainSize.height * 0.1f));

	private final JFrame mainFrame;
	private final JButton addAccBtn;
	private final JButton scrapeButton;
	private final JButton genBasicPinsButton;
	private final JButton dashboard;
	private final PinBot bot;

	public UI(PinBot bot) {
		this.bot = bot;
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
		mainFrame.pack();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(addAccBtn.getText())) {
			new AddAccountFrame();
		} else if (e.getActionCommand().equals(scrapeButton.getText())) {
			new ScrapeFrame();
		} else if (e.getActionCommand().equals(genBasicPinsButton.getText())) {
			new GenerateBasicPinsFrame();
		} else if (e.getActionCommand().equals(dashboard.getText())) {
			new UsersDashboard(bot);
		}
	}

	public static void main(String[] args) {
		PinBot.setUpDirTree();
		LogFrame.setUpLog();
		new UI(new PinBot());
	}
}
