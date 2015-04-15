package com.age.dataframes;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JTextArea;

import com.age.param.ScrapeParam;
import com.age.pinterest.bot.Scheduler;

@SuppressWarnings("serial")
public class ScrapeDataFrame extends DataFrame {
	private final JTextArea keywordArea;
	private final JTextArea tagArea;
	private final JTextArea countArea;
	private final JButton startBtn;

	private final Scheduler scheduler;

	public ScrapeDataFrame(JButton triggerButton, Scheduler scheduler) {
		super(triggerButton);
		this.scheduler = scheduler;
		keywordArea = this.addTextArea("keyword", "keyword");
		tagArea = this.addTextArea("tag", "tag");
		countArea = this.addTextArea("100", "count");
		startBtn = this.addButton("Start");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(startBtn.getText())) {
			String keyword = keywordArea.getText();
			String tag = tagArea.getText();
			int count = Integer.parseInt(countArea.getText());
			scheduler.schedule(new ScrapeParam(keyword, tag, count));
			this.dispose();
		}

	}

}
