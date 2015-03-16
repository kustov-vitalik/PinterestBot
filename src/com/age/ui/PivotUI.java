package com.age.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import com.age.data.PinterestAccount;
import com.age.pinterest.bot.PinBot;

@SuppressWarnings("serial")
public class PivotUI extends JFrame implements ActionListener {
	private final JFrame mainFrame;
	private static final int width = 1000;
	private static final int height = 600;

	public PivotUI() {
		mainFrame = new JFrame("Pinterest bot");
		mainFrame.setLayout(null);
		mainFrame.setResizable(false);
		JPanel mainPanel = new JPanel();
		mainPanel.add(new UserPanel(new PinterestAccount("pesho.com", "pesho", "pesho"), width, height / 8));
		mainPanel.add(new UserPanel(new PinterestAccount("pesho.com", "pesho", "gosho"), width, height / 8));
		mainPanel.add(new UserPanel(new PinterestAccount("pesho.com", "pesho", "tosho"), width, height / 8));
		mainPanel.add(new UserPanel(new PinterestAccount("pesho.com", "pesho", "b"), width, height / 8));
		mainPanel.add(new UserPanel(new PinterestAccount("pesho.com", "pesho", "pedssho"), width, height / 8));
		mainPanel.add(new UserPanel(new PinterestAccount("pesho.com", "pesho", "sdfs"), width, height / 8));

		mainPanel.setLayout(new GridLayout(20, 1));
		mainPanel.setSize(width, height);
		// mainPanel.setSize(width, height);

		JScrollPane scrollPane = new JScrollPane(mainPanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(0, 0, width, height);

		JPanel contentPane = new JPanel(null);
		contentPane.setPreferredSize(new Dimension(width, height));
		contentPane.add(scrollPane);

		mainFrame.setContentPane(contentPane);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		mainFrame.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
	}

	public static void main(String[] args) {
		new PivotUI();
	}
}
