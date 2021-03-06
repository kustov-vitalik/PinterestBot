package com.age.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.apache.commons.io.FileUtils;

import com.age.dataframes.AddAccountFrame;
import com.age.dataframes.GeneratePinsDataFrame;
import com.age.dataframes.ScrapeDataFrame;
import com.age.help.BotPaths;
import com.age.help.FileLogger;
import com.age.pinterest.bot.PinBot;
import com.age.pinterest.bot.Scheduler;

public class UI implements ActionListener {
	private static final Dimension mainSize = new Dimension(300, 300);
	private static final Dimension btnSize = new Dimension((int) (mainSize.width * 0.9f), (int) (mainSize.height * 0.1f));

	private final JFrame mainFrame;
	private final JButton addAccBtn;
	private final JButton scrapeButton;
	private final JButton genBasicPinsButton;
	private final JButton dashboard;
	private final JButton sysLogsBtn;
	private final Scheduler scheduler;
	private final JButton cleanAllLogs;
	public final static FileLogger syslog = new FileLogger(BotPaths.SYSLOGS);

	public UI(Scheduler scheduler) {
		this.scheduler = scheduler;
		mainFrame = new JFrame("Pinterest bot");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(6, 1));
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

		sysLogsBtn = new JButton();
		sysLogsBtn.addActionListener(this);
		sysLogsBtn.setText("Sys Logs");
		sysLogsBtn.setSize(btnSize);
		sysLogsBtn.setPreferredSize(btnSize);
		panel.add(sysLogsBtn);

		cleanAllLogs = new JButton();
		cleanAllLogs.addActionListener(this);
		cleanAllLogs.setText("Clean all logs");
		cleanAllLogs.setSize(btnSize);
		cleanAllLogs.setPreferredSize(btnSize);
		panel.add(cleanAllLogs);

		mainFrame.setResizable(false);
		mainFrame.add(panel);
		mainFrame.setSize(mainSize);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		mainFrame.setFocusable(true);
		mainFrame.setResizable(false);
		mainFrame.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		UI.syslog.log(e.getActionCommand());
		if (e.getActionCommand().equals(addAccBtn.getText())) {
			new AddAccountFrame(addAccBtn, scheduler);
		} else if (e.getActionCommand().equals(scrapeButton.getText())) {
			new ScrapeDataFrame(scrapeButton, scheduler);
		} else if (e.getActionCommand().equals(genBasicPinsButton.getText())) {
			new GeneratePinsDataFrame(genBasicPinsButton, scheduler);
		} else if (e.getActionCommand().equals(dashboard.getText())) {
			new UserDashboard();
		} else if (e.getActionCommand().equals(sysLogsBtn.getText())) {
			new LogConsole(BotPaths.SYSLOGS);
		} else if (e.getActionCommand().equals(cleanAllLogs.getText())) {
			cleanLogs();
		}
	}

	public static void main(String[] args) {
		PinBot.setUpDirTree();
		new UI(new Scheduler());
	}

	private void cleanLogs() {
		File usersLogDir = new File(BotPaths.LOGS);
		File sysLogs = new File(BotPaths.SYSLOGS);
		if (usersLogDir.listFiles() != null) {
			for (File f : usersLogDir.listFiles()) {
				FileUtils.deleteQuietly(f);
			}
		}
		FileUtils.deleteQuietly(sysLogs);
	}
}
