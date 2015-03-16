package com.age.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.age.data.PinterestAccount;

public class ConfigurePanel extends JFrame implements ActionListener {
	private final JLabel unfollowLabel;
	private final JTextArea unfollowTimeArea;
	private final JTextArea minFollowersArea;

	private final JLabel followLabel;
	private final JTextArea followTimeArea;
	private final JTextArea followCountArea;

	private final JLabel pinLabel;
	private final JTextArea pinTimeArea;
	private final JComboBox<String> boardsCombo;
	private final JTextArea urlArea;

	private final JButton subbmit;

	public ConfigurePanel(PinterestAccount account, int width, int height) {
		JPanel panel = new JPanel();
		int w = (int) (width / 1.3f);
		int h = height / 15;
		Dimension dim = new Dimension(w, h);
		unfollowLabel = new JLabel();
		unfollowLabel.setText("Unfollow");
		unfollowLabel.setPreferredSize(dim);

		unfollowTimeArea = new JTextArea();
		unfollowTimeArea.setText("time");
		unfollowTimeArea.setPreferredSize(dim);

		minFollowersArea = new JTextArea();
		minFollowersArea.setText("min followers");
		minFollowersArea.setPreferredSize(dim);

		panel.add(unfollowLabel);
		panel.add(unfollowTimeArea);
		panel.add(minFollowersArea);

		followLabel = new JLabel();
		followLabel.setText("Follow");
		followLabel.setPreferredSize(dim);

		followTimeArea = new JTextArea();
		followTimeArea.setText("time");
		followTimeArea.setPreferredSize(dim);

		followCountArea = new JTextArea();
		followCountArea.setText("count");
		followCountArea.setPreferredSize(dim);

		panel.add(followLabel);
		panel.add(followTimeArea);
		panel.add(followCountArea);

		pinLabel = new JLabel();
		pinLabel.setText("Pin");
		pinLabel.setPreferredSize(dim);

		pinTimeArea = new JTextArea();
		pinTimeArea.setText("time");
		pinTimeArea.setPreferredSize(dim);

		boardsCombo = new JComboBox<String>();
		boardsCombo.setPreferredSize(dim);

		urlArea = new JTextArea();
		urlArea.setText("Url");
		urlArea.setPreferredSize(dim);

		panel.add(pinLabel);
		panel.add(pinTimeArea);
		panel.add(boardsCombo);
		panel.add(urlArea);

		subbmit = new JButton();
		subbmit.addActionListener(this);
		subbmit.setText("subbmit");
		subbmit.setPreferredSize(dim);
		panel.add(subbmit);

		panel.setSize(width, height);
		panel.setPreferredSize(new Dimension(width, height));
		this.setSize(width, height);
		this.setLayout(new FlowLayout());
		this.add(panel);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.pack();

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals(subbmit.getText())) {
			System.out.println("Submit");
		}

	}
}
