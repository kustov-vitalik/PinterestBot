package com.age.dataframes;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.age.data.Board;
import com.age.data.User;
import com.age.pinterest.bot.PinBot;
import com.age.pinterest.bot.Scheduler;

@SuppressWarnings("serial")
public class RepinDataFrame extends DataFrame {
	private final Scheduler scheduler;
	private final User user;
	// private Board board;
	// private String keyword;
	// private String link;
	// private int repinCount;
	// private long interval;
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
		repinArea.setText("repin");
		repinArea.setToolTipText("repin");

		linkArea = new JTextArea();
		linkArea.setPreferredSize(dim);
		linkArea.setText("link");
		linkArea.setToolTipText("link");

		intervalArea = new JTextArea();
		intervalArea.setPreferredSize(dim);
		intervalArea.setText("link");
		intervalArea.setToolTipText("link");

		boardsCombo = new JComboBox<Board>();
		boardsCombo.setPreferredSize(dim);
		for (Board b : user.getBoards()) {
			boardsCombo.addItem(b);
		}

		startBtn = new JButton();
		startBtn.setPreferredSize(dim);
		startBtn.setText("Start");
		startBtn.setToolTipText("Start");

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

	}

	public static void main(String[] arg) {
		new RepinDataFrame(null, PinBot.getUser("huntjudith8"));
	}
}
