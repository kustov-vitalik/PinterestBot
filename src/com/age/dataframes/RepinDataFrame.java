package com.age.dataframes;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
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

	public RepinDataFrame(Scheduler scheduler, User user) {
		this.scheduler = scheduler;
		this.user = user;

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		keywordArea = new JTextArea();
		keywordArea.setPreferredSize(dim);
		keywordArea.setText("keyword");
		keywordArea.setToolTipText("keyword");

		repinArea = new JTextArea();
		repinArea.setPreferredSize(dim);
		repinArea.setText("repin count");
		repinArea.setToolTipText("repin count");

		linkArea = new JTextArea();
		linkArea.setPreferredSize(dim);
		linkArea.setText("link");
		linkArea.setToolTipText("link");

		intervalArea = new JTextArea();
		intervalArea.setPreferredSize(dim);
		intervalArea.setText("10");
		intervalArea.setToolTipText("Interval in minutes");

		boardsCombo = new JComboBox<Board>();
		boardsCombo.setPreferredSize(dim);
		if (user.getBoards() != null) {
			for (Board b : user.getBoards()) {
				boardsCombo.addItem(b);
			}
		}

		startBtn = new JButton();
		startBtn.setPreferredSize(dim);
		startBtn.setText("Start");
		startBtn.setToolTipText("Start");
		startBtn.addActionListener(this);

		panel.add(keywordArea);
		panel.add(linkArea);
		panel.add(repinArea);
		panel.add(intervalArea);
		panel.add(boardsCombo);
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
			String keyword = keywordArea.getText();
			String link = linkArea.getText();
			int repinCount = Integer.parseInt(repinArea.getText());
			long interval = Integer.parseInt(intervalArea.getText());
			interval *= 1000 * 60;
			RepinParam param = new RepinParam(user, board, keyword, link, repinCount, interval);
			scheduler.schedule(param);
			this.dispose();
		}
	}
}
