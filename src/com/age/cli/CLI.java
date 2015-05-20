package com.age.cli;

public class CLI {

	public static void main(String[] args) {
		args = new String[] { "user", "follow", "5000", "12", "true" };
		CommandParser parser = new CommandParser();
		StringBuilder builder = new StringBuilder();
		for (String s : args) {
			builder.append(s + " ");
		}
		System.out.println(builder.toString());
		parser.parse(builder.toString());

		// while (true) {
		//
		// }
	}
}
