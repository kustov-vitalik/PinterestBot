package com.age.dataframes;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;

import com.age.data.Board;
import com.age.data.User;
import com.age.param.RepinParam;
import com.age.pinterest.bot.Scheduler;

@SuppressWarnings("serial")
public class RepinDataFrame extends DataFrame {
	private final Scheduler scheduler;
	private final User user;
	private final JButton startBtn;
	private final JComboBox<Board> boardsCombo;
	private final JTextArea keywordArea;
	private final JTextArea linkArea;
	private final JTextArea repinArea;
	private final JTextArea intervalArea;

	public RepinDataFrame(Scheduler scheduler, User user, JButton triggerBtn) {
		super(triggerBtn);
		this.scheduler = scheduler;
		this.user = user;

		keywordArea = this.addTextArea("keyword", "keyword");
		repinArea = this.addTextArea("100", "repin count");
		linkArea = this.addTextArea("", "link");
		intervalArea = this.addTextArea("10", "Interval in minutes");
		boardsCombo = new JComboBox<Board>();
		boardsCombo.setPreferredSize(dim);
		if (user.getBoards() != null) {
			for (Board b : user.getBoards()) {
				boardsCombo.addItem(b);
			}
		}
		this.addComponent(boardsCombo);
		startBtn = this.addButton("Start");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(startBtn.getText())) {
			Board board = (Board) boardsCombo.getSelectedItem();
			String keyword = keywordArea.getText();
			String link = linkArea.getText();
			int repinCount = Integer.parseInt(repinArea.getText());
			long interval = Integer.parseInt(intervalArea.getText());
			interval *= 1000 * 60;
			RepinParam param = new RepinParam(user, board, keyword, link, repinCount, interval);
			scheduler.schedule(param);
			this.trunOnBtn();
			this.dispose();
		}
	}
}
