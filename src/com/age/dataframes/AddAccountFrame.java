package com.age.dataframes;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JTextArea;

import com.age.data.PinterestAccount;
import com.age.param.AddAccountParam;
import com.age.pinterest.bot.Scheduler;

@SuppressWarnings("serial")
public class AddAccountFrame extends DataFrame {

	private final JTextArea emailArea;
	private final JTextArea passArea;
	private final JTextArea userArea;
	private final JButton startBtn;
	private final Scheduler scheduler;

	public AddAccountFrame(JButton triggerButton, Scheduler scheduler) {
		super(triggerButton);
		this.scheduler = scheduler;
		emailArea = this.addTextArea("email", "email");
		passArea = this.addTextArea("pass", "pass");
		userArea = this.addTextArea("user", "user");
		startBtn = this.addButton("Start");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(startBtn.getText())) {
			String email = emailArea.getText();
			String pass = passArea.getText();
			String username = userArea.getText();
			PinterestAccount acc = new PinterestAccount(email, pass, username);
			scheduler.schedule(new AddAccountParam(acc));
			this.dispose();
		}
	}
}
