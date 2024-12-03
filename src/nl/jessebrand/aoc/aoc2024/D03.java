package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.parsePoint;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.jessebrand.aoc.Point;

public class D03 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d03");
		System.out.println("A");
		parseInstructions(lines);
		
		
		List<String> list = new ArrayList<>();
		boolean endsWithDo = true;
		for (final String line : lines) {
			System.out.println(line);
			String[] doSplit = line.split("do\\(\\)");
			System.out.println(Arrays.deepToString(doSplit));
			boolean firstInLine = true;
			for (String dos: doSplit) {
				if (firstInLine) {
					firstInLine = false;
					if (!endsWithDo) {
						continue;
					}
				}
				int i = dos.indexOf("don't()");
				if (i == -1) {
					list.add(dos);
					endsWithDo = true;
				} else {
					list.add(dos.substring(0, i));
					endsWithDo = false;
				}
			}
		}
		System.out.println("B");
		parseInstructions(list);
	}

	private static void parseInstructions(List<String> lines) {
		int total = 0;
		final Pattern p = Pattern.compile("mul\\([\\d]{1,3},[\\d]{1,3}\\)");
		for (final String line : lines) {
			Matcher m = p.matcher(line);
			while (m.find()) {
				System.out.println(m.group());
				int result = handleInstruction(m.group());
				System.out.println(result);
				total += result;
			}
		}
		System.out.println(total);
	}

	private static int handleInstruction(final String str) {
		final Point p = parsePoint(str.substring(4, str.length() - 1));
		return p.x() * p.y();
	}
}

// 90044227