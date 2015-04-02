package com.age.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Log {
	private static JTextArea console;

	public static void setUpLog() {
		Dimension dim = new Dimension(400, 600);
		System.out.println("Set log");
		JFrame frame = new JFrame();
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
		JScrollPane scroll = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setSize(dim);
		frame.setSize(dim);
		frame.setPreferredSize(dim);
		frame.add(scroll);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.pack();
	}

	public synchronized static void log(String text) {
		if (console == null) {
			setUpLog();
		}
		console.append(text + "\n");
		int len = console.getDocument().getLength();
		console.setCaretPosition(len);
		System.out.println(text);
	}

}
