package com.age.dataframes;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;

import com.age.help.BotPaths;
import com.age.param.GeneratePinsParam;
import com.age.pinterest.bot.Scheduler;

@SuppressWarnings("serial")
public class GeneratePinsDataFrame extends DataFrame {

	private final JComboBox<String> tags;
	private final JButton startBtn;
	private final JTextArea sourceArea;
	private final Scheduler scheduler;

	public GeneratePinsDataFrame(JButton triggerButton, Scheduler scheduler) {
		super(triggerButton);
		this.scheduler = scheduler;
		tags = new JComboBox<String>();
		File imageDir = new File(BotPaths.IMAGES_ROOT);
		for (File f : imageDir.listFiles()) {
			tags.addItem(f.getName());
		}
		this.addComponent(tags);
		sourceArea = this.addTextArea("", "source");
		startBtn = this.addButton("Start");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(startBtn.getText())) {
			String source = sourceArea.getText();
			String tag = (String) tags.getSelectedItem();
			scheduler.schedule(new GeneratePinsParam(tag, source));
			this.dispose();
		}
	}

}
