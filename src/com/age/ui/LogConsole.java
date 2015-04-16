package com.age.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.age.help.FileUtill;

public class LogConsole {
	private JFrame frame;
	private JTextArea console;
	private final String pathToLog;

	public LogConsole(String logName) {
		this.pathToLog = logName;
		setUpLog();
		startLogging();
	}

	private void setUpLog() {
		Dimension dim = new Dimension(400, 400);
		frame = new JFrame();
		frame.setName(pathToLog);
		frame.setTitle(pathToLog);
		console = new JTextArea();
		console.setText("");
		console.setToolTipText("console");
		console.setBackground(Color.BLACK);
		console.setCaretColor(Color.YELLOW);
		console.setEditable(false);
		Font font = new Font("Verdana", Font.BOLD, 13);
		console.setFont(font);
		console.setForeground(Color.GREEN);
		JScrollPane scroll = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setSize(dim);
		frame.setSize(dim);
		frame.setPreferredSize(dim);
		frame.add(scroll);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.pack();
	}

	private void startLogging() {
		new Thread(new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				while (true) {
					String text = "";
					try {
						text = FileUtill.getFileContents(pathToLog);
						console.setText(text);
						Thread.sleep(1000);
						if (frame == null || !frame.isShowing()) {
							frame.dispose();
							Thread.currentThread().stop();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}
