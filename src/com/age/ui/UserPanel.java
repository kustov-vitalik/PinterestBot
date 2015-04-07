package com.age.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.age.data.PinterestAccount;
import com.age.data.UserConfig;
import com.age.pinterest.bot.PinBot;
import com.age.pinterest.task.TaskType;

@SuppressWarnings("serial")
public class UserPanel extends JPanel implements ActionListener {
	private final JButton refreshBtn;
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
		refreshBtn = new JButton();
		refreshBtn.setText("R");
		refreshBtn.setSize(btnDim);
		refreshBtn.setPreferredSize(btnDim);
		refreshBtn.addActionListener(this);

		user = new JLabel();
		user.setText(acc.getUsername());
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
		this.add(refreshBtn);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals(follow.getText())) {
				toggleTask(TaskType.FOLLOW, follow);
			} else if (e.getActionCommand().equals(unfollow.getText())) {
				toggleTask(TaskType.UNFOLLOW, unfollow);
			} else if (e.getActionCommand().equals(pin.getText())) {
				toggleTask(TaskType.PIN, pin);
			} else if (e.getActionCommand().equals(configure.getText())) {
				new ConfigurePanel(acc, 300, 400);
			} else if (e.getActionCommand().equals(refreshBtn.getText())) {
				PinBot.addAccount(acc);
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	private void toggleTask(TaskType type, JButton btn) throws ClientProtocolException, IOException, JSONException,
			InterruptedException {
		UserConfig cfg = PinBot.getUserConfig(acc.getUsername());
		if (bot.getTask(type) == null) {
			Log.log("Add " + type);
			addTask(type, cfg);
			btn.setBackground(Color.GREEN);
		} else {
			Log.log("Stoping " + type);
			bot.terminateTask(type);
			btn.setBackground(new JButton().getBackground());
		}
	}

	private void addTask(TaskType type, UserConfig cfg) throws ClientProtocolException, IOException, JSONException,
			InterruptedException {
		switch (type) {
		case FOLLOW:
			bot.addFollowTask(acc.getUsername(), cfg.getFollowCount(), cfg.getFollowTime());
			break;
		case UNFOLLOW:
			bot.addUnfollowTask(acc.getUsername(), cfg.getMinFollowers(), cfg.getUnfollowTime());
			break;
		case PIN:
			bot.addPinTask(acc.getUsername(), cfg.getPinBoard(), cfg.getPinTime());
			break;
		default:
			return;
		}
	}
}
