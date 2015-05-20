package com.age.cli;

public class CommandParser {

	public void parse(String cmd) {
		String[] args = cmd.split(" ");
		System.out.println(args[0]);
		System.out.println(args[1]);
		System.out.println(args[2]);
		System.out.println(args[3]);
		System.out.println(args[4]);
	}

}
