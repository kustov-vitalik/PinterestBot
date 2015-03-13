package com.age.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import com.age.pinterest.bot.PinBot;

@SuppressWarnings("serial")
public class ScrapeFrame extends JFrame implements ActionListener {
	private static final Dimension mainSize = new Dimension(500, 500);
	private static final Dimension textSize = new Dimension((int) (mainSize.width * 0.3f), (int) (mainSize.height * 0.04f));
	private static final Dimension btnSize = new Dimension((int) (mainSize.width * 0.9f), (int) (mainSize.height * 0.05f));

	private final JButton start;
	private final JTextArea tag;
	private final JTextArea keyword;
	private final JTextArea count;

	public ScrapeFrame() {
		this.setTitle("Scrape");
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		tag = new JTextArea();
		tag.setSize(textSize);
		tag.setPreferredSize(textSize);
		tag.setText("tag");
		tag.setToolTipText("tag");
		panel.add(tag);

		keyword = new JTextArea();
		keyword.setSize(textSize);
		keyword.setPreferredSize(textSize);
		keyword.setText("keyword");
		keyword.setToolTipText("keyword");
		panel.add(keyword);

		count = new JTextArea();
		count.setSize(textSize);
		count.setPreferredSize(textSize);
		count.setText("count");
		count.setToolTipText("count");
		panel.add(count);

		start = new JButton();
		start.setText("start");
		start.setSize(btnSize);
		start.setPreferredSize(btnSize);
		start.addActionListener(this);
		panel.add(start);

		this.add(panel);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if (a.getActionCommand().equals("start")) {
			String tagTxt = tag.getText();
			String keyowrdTxt = keyword.getText();
			int size = Integer.parseInt(count.getText());
			PinBot bot = new PinBot();
			bot.addScrapeTask(keyowrdTxt, tagTxt, size);
			this.dispose();
		}

	}

}
