package com.age.dataframes;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
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

	public PinDataFrame(User user, Scheduler scheduler) {
		this.user = user;
		this.scheduler = scheduler;

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		boardsCombo = new JComboBox<Board>();
		boardsCombo.setPreferredSize(dim);
		if (user.getBoards() != null) {
			for (Board b : user.getBoards()) {
				boardsCombo.addItem(b);
			}
		}
		intervalArea = new JTextArea();
		intervalArea.setPreferredSize(dim);
		intervalArea.setText("interval");
		intervalArea.setToolTipText("interval");

		startBtn = new JButton();
		startBtn.setPreferredSize(dim);
		startBtn.setText("Start");
		startBtn.setToolTipText("Start");
		startBtn.addActionListener(this);

		panel.add(boardsCombo);
		panel.add(intervalArea);
		panel.add(startBtn);

		this.add(panel);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.pack();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(startBtn.getText())) {
			Board board = (Board) boardsCombo.getSelectedItem();
			long interval = Integer.parseInt(intervalArea.getText());
			PinParam pinParam = new PinParam(user, board, interval);
			scheduler.schedule(pinParam);
			this.dispose();
		}
	}

}
