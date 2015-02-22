package com.age.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import com.age.pinterest.bot.PinBot;
import com.age.pinterest.config.PinterestAccount;
import com.age.pinterest.task.UnFollowTask;

@SuppressWarnings("serial")
public class AddAccountFrame extends JFrame implements ActionListener {
	private static final Logger logger =  Logger.getLogger(AddAccountFrame.class);
	private static final Dimension mainSize = new Dimension(500, 500);
	private static final Dimension textSize = new Dimension((int) (mainSize.width * 0.3f), (int) (mainSize.height * 0.04f));
	private static final Dimension btnSize = new Dimension((int) (mainSize.width * 0.3f), (int) (mainSize.height * 0.05f));

	private final JTextArea email;
	private final JTextArea pass;
	private final JTextArea user;
	private final JButton addAccount;

	public AddAccountFrame() {
		this.setTitle("Add Account");
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		email = new JTextArea();
		email.setPreferredSize(textSize);
		email.setSize(textSize);
		email.setToolTipText("email");
		email.setText("email");

		pass = new JTextArea();
		pass.setPreferredSize(textSize);
		pass.setSize(textSize);
		pass.setToolTipText("pass");
		pass.setText("pass");

		user = new JTextArea();
		user.setPreferredSize(textSize);
		user.setSize(textSize);
		user.setToolTipText("username");
		user.setText("username");

		addAccount = new JButton();
		addAccount.setPreferredSize(textSize);
		addAccount.setSize(btnSize);
		addAccount.setText("Add account");
		addAccount.addActionListener(this);

		panel.add(email);
		panel.add(pass);
		panel.add(user);
		panel.add(addAccount);

		this.add(panel);
		this.setVisible(true);
		this.setSize(mainSize);
		this.setLocationRelativeTo(null);
		this.pack();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Add account")) {
			PinterestAccount acc = new PinterestAccount();
			acc.setEmail(email.getText());
			acc.setPassword(pass.getText());
			acc.setUser(user.getText());
			try {
				PinBot.addAccount(acc);
			} catch (IOException e1) {
				logger.error("",e1);
			}
		}
	}
}
