package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D01 {
	
	private static final Map<String, Integer> mapping = buildMap();

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d1");
		System.out.println(lines);
		int total = 0;
		for (String line : lines) {
			String first = null;
			String last = null;
			for (int i = 0; i < line.length(); i++) {
				char c = line.charAt(i);
				if (c >= '0' && c <= '9') {
					last = "" + c;
					if (first == null) {
						first = last;
					}
				}
				for (String mapKey : mapping.keySet()) {
					if (line.substring(i).startsWith(mapKey)) {
						last = "" + mapping.get(mapKey);
						if (first == null) {
							first = last;
						}
					}
				}
			}
			System.out.println(String.format("%s : %s - %s", line, first, last));
			total += Integer.parseInt(first + last);
		}
		System.out.println(total);
	}

	private static Map<String, Integer> buildMap() {
		Map<String, Integer> result = new HashMap<>();
		result.put("one", 1);
		result.put("two", 2);
		result.put("three", 3);
		result.put("four", 4);
		result.put("five", 5);
		result.put("six", 6);
		result.put("seven", 7);
		result.put("eight", 8);
		result.put("nine", 9);
		return result;
	}
}
