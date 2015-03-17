package com.age.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.codehaus.jackson.map.ObjectMapper;

import com.age.data.Board;
import com.age.data.PinterestAccount;
import com.age.data.UserConfig;
import com.age.help.BotPaths;

@SuppressWarnings("serial")
public class ConfigurePanel extends JFrame implements ActionListener {
	private static final String CONFIG_FILE_ROOT = BotPaths.USER_ROOT + "%s/";
	private static final String CONFIG_FILE = "config.json";

	private final JLabel unfollowLabel;
	private final JTextArea unfollowTimeArea;
	private final JTextArea minFollowersArea;

	private final JLabel followLabel;
	private final JTextArea followTimeArea;
	private final JTextArea followCountArea;

	private final JLabel pinLabel;
	private final JTextArea pinTimeArea;
	private final JComboBox<Board> boardsCombo;

	private final JButton subbmit;

	private final PinterestAccount account;

	public ConfigurePanel(PinterestAccount account, int width, int height) {
		this.account = account;
		JPanel panel = new JPanel();
		int w = (int) (width / 1.3f);
		int h = height / 15;
		Dimension dim = new Dimension(w, h);
		unfollowLabel = new JLabel();
		unfollowLabel.setText("Unfollow");
		unfollowLabel.setPreferredSize(dim);

		unfollowTimeArea = new JTextArea();
		unfollowTimeArea.setText("11");
		unfollowTimeArea.setPreferredSize(dim);

		minFollowersArea = new JTextArea();
		minFollowersArea.setText("5000");
		minFollowersArea.setPreferredSize(dim);

		panel.add(unfollowLabel);
		panel.add(unfollowTimeArea);
		panel.add(minFollowersArea);

		followLabel = new JLabel();
		followLabel.setText("Follow");
		followLabel.setPreferredSize(dim);

		followTimeArea = new JTextArea();
		followTimeArea.setText("11");
		followTimeArea.setPreferredSize(dim);

		followCountArea = new JTextArea();
		followCountArea.setText("5000");
		followCountArea.setPreferredSize(dim);

		panel.add(followLabel);
		panel.add(followTimeArea);
		panel.add(followCountArea);

		pinLabel = new JLabel();
		pinLabel.setText("Pin");
		pinLabel.setPreferredSize(dim);

		pinTimeArea = new JTextArea();
		pinTimeArea.setText("60");
		pinTimeArea.setPreferredSize(dim);

		boardsCombo = new JComboBox<Board>();
		boardsCombo.setPreferredSize(dim);

		panel.add(pinLabel);
		panel.add(pinTimeArea);
		panel.add(boardsCombo);

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
			long unfollowTime = Long.parseLong(unfollowTimeArea.getText());
			int minFollowers = Integer.parseInt(minFollowersArea.getText());
			long followTime = Long.parseLong(followTimeArea.getText());
			int followCount = Integer.parseInt(followCountArea.getText());
			long pinTime = Long.parseLong(pinTimeArea.getText());
			Board pinBoard = (Board) boardsCombo.getSelectedItem();
			followTime *= 1000;
			unfollowTime *= 1000;
			pinTime *= 1000 * 60;

			UserConfig config = new UserConfig();
			config.setFollowCount(followCount);
			config.setFollowTime(followTime);
			config.setUnfollowTime(unfollowTime);
			config.setMinFollowers(minFollowers);
			config.setPinTime(pinTime);
			config.setPinBoard(pinBoard);

			try {
				File root = new File(String.format(CONFIG_FILE_ROOT, account.getUser()));
				root.mkdirs();
				File cfgFile = new File(root, CONFIG_FILE);
				System.out.println(cfgFile.getAbsolutePath());
				cfgFile.createNewFile();
				ObjectMapper mapper = new ObjectMapper();
				mapper.writeValue(cfgFile, config);
			} catch (Exception exc) {
				exc.printStackTrace();
			}

			System.out.println("Submit");
			this.dispose();
		}

	}
}
