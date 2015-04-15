package com.age.dataframes;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;

import com.age.data.Board;
import com.age.data.User;
import com.age.param.PinParam;
import com.age.pinterest.bot.Scheduler;

@SuppressWarnings("serial")
public class PinDataFrame extends DataFrame {

	private final JTextArea intervalArea;
	private final JComboBox<Board> boardsCombo;
	private final User user;
	private final Scheduler scheduler;
	private final JButton startBtn;

	public PinDataFrame(User user, Scheduler scheduler, JButton triggerBtn) {
		super(triggerBtn);
		this.user = user;
		this.scheduler = scheduler;

		boardsCombo = new JComboBox<Board>();
		boardsCombo.setPreferredSize(dim);
		if (user.getBoards() != null) {
			for (Board b : user.getBoards()) {
				boardsCombo.addItem(b);
			}
		}
		this.addComponent(boardsCombo);
		intervalArea = this.addTextArea("30", "Interval in minutes");
		startBtn = this.addButton("Start");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(startBtn.getText())) {
			Board board = (Board) boardsCombo.getSelectedItem();
			long interval = Integer.parseInt(intervalArea.getText());
			interval *= 1000 * 60;
			PinParam pinParam = new PinParam(user, board, interval);
			scheduler.schedule(pinParam);
			this.trunOnBtn();
			this.dispose();
		}
	}

}
