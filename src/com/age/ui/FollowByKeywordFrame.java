package com.age.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import com.age.pinterest.bot.PinBot;

@SuppressWarnings("serial")
public class FollowByKeywordFrame extends JFrame implements ActionListener {
	private static final Logger logger = Logger.getLogger(FollowFrame.class);
	private static final Dimension mainSize = new Dimension(500, 500);
	private static final Dimension textSize = new Dimension((int) (mainSize.width * 0.3f), (int) (mainSize.height * 0.04f));
	private static final Dimension btnSize = new Dimension((int) (mainSize.width * 0.9f), (int) (mainSize.height * 0.05f));

	private final JButton start;
	private final JTextArea interval;
	private final JTextArea num;
	private final JTextArea keyword;

	public FollowByKeywordFrame() {
		this.setTitle("Follow by keyword");
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		start = new JButton();
		start.setText("start");
		start.setSize(btnSize);
		start.setPreferredSize(btnSize);
		start.addActionListener(this);

		interval = new JTextArea();
		interval.setSize(textSize);
		interval.setPreferredSize(textSize);
		interval.setToolTipText("Interval in seconds");
		interval.setText("10");

		keyword = new JTextArea();
		keyword.setSize(textSize);
		keyword.setPreferredSize(textSize);
		keyword.setToolTipText("keyword");
		keyword.setText("keyword");

		num = new JTextArea();
		num.setSize(textSize);
		num.setPreferredSize(textSize);
		num.setText("5000");
		num.setToolTipText("Count");

		panel.add(keyword);
		panel.add(interval);
		panel.add(num);
		panel.add(start);
		this.add(panel);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.pack();
		this.setResizable(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.dispose();
	}

}
