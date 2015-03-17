package com.age.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.age.data.PinterestAccount;
import com.age.data.UserConfig;
import com.age.pinterest.bot.PinBot;

@SuppressWarnings("serial")
public class UserPanel extends JPanel implements ActionListener {
	private final JLabel user;
	private final JButton follow;
	private final JButton unfollow;
	private final JButton pin;
	private final JButton configure;
	private final PinterestAccount acc;
	private final PinBot bot;

	public UserPanel(PinBot bot, PinterestAccount acc, int width, int height) {
		this.bot = bot;
		this.acc = acc;
		Dimension dim = new Dimension(width, height);
		this.setSize(dim);
		Dimension btnDim = new Dimension((int) dim.getWidth() / 7, (int) (dim.getHeight() * 0.5f));
		user = new JLabel();
		user.setText(acc.getUser());
		user.setSize(btnDim);
		user.setPreferredSize(btnDim);

		follow = new JButton();
		follow.setSize(btnDim);
		follow.setPreferredSize(btnDim);
		follow.setText("follow");
		follow.addActionListener(this);

		unfollow = new JButton();
		unfollow.setSize(btnDim);
		unfollow.setPreferredSize(btnDim);
		unfollow.setText("unfollow");
		unfollow.addActionListener(this);

		pin = new JButton();
		pin.setSize(btnDim);
		pin.setPreferredSize(btnDim);
		pin.setText("pin");
		pin.addActionListener(this);

		configure = new JButton();
		configure.setSize(btnDim);
		configure.setPreferredSize(btnDim);
		configure.setText("configure");
		configure.addActionListener(this);

		this.add(user);
		this.add(follow);
		this.add(unfollow);
		this.add(pin);
		this.add(configure);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			UserConfig cfg = PinBot.getUserConfig(acc.getUser());
			if (e.getActionCommand().equals(follow.getText())) {
				bot.addFollowTask(acc.getUser(), cfg.getFollowCount(), cfg.getFollowTime());
			} else if (e.getActionCommand().equals(unfollow.getText())) {
				bot.addUnfollowTask(acc.getUser(), cfg.getMinFollowers(), cfg.getUnfollowTime());
			} else if (e.getActionCommand().equals(pin.getText())) {
				bot.addPinTask(acc.getUser(), cfg.getPinBoard(), cfg.getPinTime());
			} else if (e.getActionCommand().equals(configure.getText())) {
				new ConfigurePanel(acc, 300, 400);
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}
