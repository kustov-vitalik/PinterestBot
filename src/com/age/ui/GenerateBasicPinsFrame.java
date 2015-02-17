package com.age.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.age.help.BotPaths;
import com.age.pinterest.bot.PinBot;

@SuppressWarnings("serial")
public class GenerateBasicPinsFrame extends JFrame implements ActionListener {
	private static final Dimension mainSize = new Dimension(500, 500);
	private static final Dimension textSize = new Dimension((int) (mainSize.width * 0.3f), (int) (mainSize.height * 0.04f));
	private static final Dimension btnSize = new Dimension((int) (mainSize.width * 0.9f), (int) (mainSize.height * 0.05f));

	private final JComboBox<String> tags;
	private final JButton start;
	private final JTextArea source;

	public GenerateBasicPinsFrame() {
		this.setTitle("Generate basic pins");
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		tags = new JComboBox<String>();
		File imageDir = new File(BotPaths.IMAGES_DIR);
		System.out.println(imageDir.getAbsolutePath());
		for (File f : imageDir.listFiles()) {
			tags.addItem(f.getName());
		}
		start = new JButton();
		start.setText("start");
		start.setSize(btnSize);
		start.setPreferredSize(btnSize);
		start.addActionListener(this);

		source = new JTextArea();
		source.setSize(textSize);
		source.setPreferredSize(textSize);
		source.setText("source");
		source.setToolTipText("source");

		panel.add(tags);
		panel.add(source);
		panel.add(start);
		this.add(panel);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("start")) {
			PinBot bot = new PinBot();
			String tag = (String) tags.getSelectedItem();
			String sourceText = source.getText();
			bot.addGenerateBasicPinsTask(tag, sourceText);
		}

	}
}
