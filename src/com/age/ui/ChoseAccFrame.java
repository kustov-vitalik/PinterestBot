package com.age.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.age.help.AccountManager;
import com.age.pinterest.bot.PinBot;
import com.sun.jna.platform.win32.Advapi32Util.Account;

@SuppressWarnings("serial")
public class ChoseAccFrame extends JFrame implements ActionListener{
	private static final Logger logger = Logger.getLogger(FollowFrame.class);
	private static final Dimension mainSize = new Dimension(500, 500);
	private static final Dimension textSize = new Dimension((int) (mainSize.width * 0.3f), (int) (mainSize.height * 0.04f));
	private static final Dimension btnSize = new Dimension((int) (mainSize.width * 0.9f), (int) (mainSize.height * 0.05f));

	private final JComboBox<String> users;
	private final JButton chose;

	public ChoseAccFrame(AccountManager manager) {
		this.setTitle("Chose Account");
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		users = new JComboBox<String>();
		manager.getBords();
		List<String> usersItems = PinBot.listAccount();
		for (String s : usersItems) {
			users.addItem(s);
		}
		panel.add(users);
		
		chose = new JButton();
		chose.setText("chose");
		chose.setSize(btnSize);
		chose.setPreferredSize(btnSize);
		chose.addActionListener(this);



		panel.add(chose);
		this.add(panel);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.pack();
		this.setResizable(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.dispose();
	}

}


