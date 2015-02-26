package com.age.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class CapturePane extends JPanel implements Consumer {

	private JTextArea output;

	public CapturePane() {
		setLayout(new BorderLayout());
		output = new JTextArea();
		add(new JScrollPane(output));
	}

	@Override
	public void appendText(final String text) {
		if (EventQueue.isDispatchThread()) {
			output.append(text);
			output.setCaretPosition(output.getText().length());
		} else {

			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					appendText(text);
				}
			});

		}
	}
}

interface Consumer {
	public void appendText(String text);
}

class StreamCapturer extends OutputStream {

	private StringBuilder buffer;
	private String prefix;
	private Consumer consumer;
	private PrintStream old;

	public StreamCapturer(String prefix, Consumer consumer, PrintStream old) {
		this.prefix = prefix;
		buffer = new StringBuilder(128);
		buffer.append("[").append(prefix).append("] ");
		this.old = old;
		this.consumer = consumer;
	}

	@Override
	public void write(int b) throws IOException {
		char c = (char) b;
		String value = Character.toString(c);
		buffer.append(value);
		if (value.equals("\n")) {
			consumer.appendText(buffer.toString());
			buffer.delete(0, buffer.length());
			buffer.append("[").append(prefix).append("] ");
		}
		old.print(c);
	}
}