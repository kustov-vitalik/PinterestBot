package com.age.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.age.help.FileUtill;

public class Log {
	private JFrame frame;
	private JTextArea console;
	private final String pathToLog;

	public Log(String pathToLog) {
		this.pathToLog = pathToLog;
		setUpLog();
		startLogging();
	}

	private void setUpLog() {
		Dimension dim = new Dimension(400, 600);
		System.out.println("Set log");
		frame = new JFrame();
		frame.setName("Log");
		frame.setTitle("Log");
		console = new JTextArea();
		console.setText("");
		console.setToolTipText("console");
		console.setBackground(Color.BLACK);
		console.setCaretColor(Color.YELLOW);
		console.setEditable(false);
		Font font = new Font("Verdana", Font.BOLD, 15);
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
						System.out.println("Logging");
						if (frame == null || !frame.isActive()) {
							frame.dispose();
							Thread.currentThread().stop();
						}

					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}
