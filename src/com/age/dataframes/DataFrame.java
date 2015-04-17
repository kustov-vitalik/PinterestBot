package com.age.dataframes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public abstract class DataFrame extends JFrame implements ActionListener {
	private static final Color buttonDefaultColor = new JButton().getBackground();
	protected final Dimension dim = new Dimension(100, 20);
	private final JButton triggerButton;
	private final JPanel panel;

	public DataFrame(JButton triggerButton) {
		this.triggerButton = triggerButton;
		this.panel = new JPanel();
		this.panel.setLayout(new FlowLayout());
		this.add(panel);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
	}

	protected void trunOffBtn() {
		triggerButton.setBackground(buttonDefaultColor);
	}

	protected void trunOnBtn() {
		triggerButton.setBackground(Color.GREEN);
	}

	protected JTextArea addTextArea(String text, String tooltip) {
		JTextArea area = new JTextArea();
		area.setPreferredSize(dim);
		area.setText(text);
		area.setToolTipText(tooltip);
		this.addComponent(area);
		return area;
	}

	protected JButton addButton(String text) {
		JButton btn = new JButton();
		btn.setPreferredSize(dim);
		btn.setText("Start");
		btn.setToolTipText("Start");
		btn.addActionListener(this);
		this.addComponent(btn);
		return btn;
	}

	protected void addComponent(Component c) {
		this.panel.add(c);
		this.pack();
	}

}
