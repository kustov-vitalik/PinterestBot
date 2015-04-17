package com.age.help;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FileUtill {

	public static void saveToFile(String destination, String name, String content) throws FileNotFoundException {
		File root = new File(destination);
		if (!root.exists() || !root.isDirectory()) {
			root.mkdirs();
		}
		PrintWriter out = null;
		try {
			String path = destination + "\\" + name;
			out = new PrintWriter(path);
			out.println(content);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	public static void appendToFile(String file, String content) {
		try {
			File f = new File(file);
			f.createNewFile();
			FileWriter fw = new FileWriter(file, true);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<String> getAllFiles(String path) {
		ArrayList<String> paths = new ArrayList<String>();
		File rootFile = new File(path);
		for (File f : rootFile.listFiles()) {
			paths.add(f.getAbsolutePath());
		}
		return paths;

	}

	public static String getFileContents(String path) {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded, "UTF-8");
		} catch (IOException e) {
			// swallow since it only show there are no logs.
		}
		return "";
	}

	public static ArrayList<String> getFileListContents(String root) throws IOException {
		File rootFile = new File(root);
		ArrayList<String> list = new ArrayList<String>();
		for (File f : rootFile.listFiles()) {
			list.add(getFileContents(f.getAbsolutePath()));
		}
		return list;
	}

	public static void stripDuplicatesFromFile(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		Set<String> lines = new HashSet<String>(50000);
		String line;
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}
		reader.close();
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		for (String unique : lines) {
			writer.write(unique);
			writer.newLine();
		}
		writer.close();
	}

}
