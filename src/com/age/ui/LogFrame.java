package com.age.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogFrame {
	private static JTextArea console;

	public static void setUpLog() {
		Dimension dim = new Dimension(400, 600);
		JFrame frame = new JFrame();
		frame.setTitle("Scrape");
		console = new JTextArea();
		console.setText("");
		console.setToolTipText("console");
		console.setBackground(Color.BLACK);
		console.setCaretColor(Color.YELLOW);
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

	public synchronized static void log(String text) {
		console.append(text + "\n");
	}

}
