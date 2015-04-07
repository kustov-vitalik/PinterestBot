package com.age.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LabelRow extends JPanel {
	public LabelRow(List<String> strings, int w, int h) {
		this.setLayout(new GridLayout(1, strings.size()));
		for (String s : strings) {
			JLabel label = new JLabel();
			label.setSize(w, h);
			label.setPreferredSize(new Dimension(w, h));
			label.setText(s);
			this.add(label);
		}
	}

}
