package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class D03b {
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d03");
		System.out.println(lines);
		int total = 0;
		List<Number> numbers = new ArrayList<>();
		for (int y = 0; y < lines.size(); y++) {
			String line = lines.get(y);
			for (int x = 0; x < line.length(); x++) {
				char c = charAt(lines, x, y);
				int startX = -1;
				if (c >= '0' && c <= '9') {
					startX = x;
					while (c >= '0' && c <= '9') {
						x++;
						c = charAt(lines, x, y);
					}
					int number = Integer.parseInt(substring(lines, y, startX, x));
					numbers.add(new Number(number, y, startX, x - 1));
				}
			}
		}
		System.out.println(numbers);

		for (int y = 0; y < lines.size(); y++) {
			String line = lines.get(y);
			for (int x = 0; x < line.length(); x++) {
				char c = charAt(lines, x, y);
				if (c == '*') {
					final List<Number> adjacentNumbers = findNumbers(numbers, x, y);
					System.out.println(String.format("Gear at %d, %d: %d adjacent numbers", x, y, adjacentNumbers.size()));
					if (adjacentNumbers.size() == 2) {
						total += adjacentNumbers.get(0).number() * adjacentNumbers.get(1).number();
					}
				}
			}
		}		
		System.out.println(total);
	}
	
	private static List<Number> findNumbers(List<Number> numbers, int x, int y) {
		final List<Number> result = new ArrayList<>();
		for (final Number number : numbers) {
			if (y - 1 <= number.y() && y + 1 >= number.y() && x - 1 <= number.endX() && x + 1 >= number.startX()) {
				result.add(number);
			}
		}
		return result;
	}

	private record Number(int number, int y, int startX, int endX) {}

	private static String substring(List<String> lines, int y, int startX, int endX) {
		if (y < 0 || y > lines.size()) {
			return "";
		}
		return lines.get(y).substring(startX, endX);
	}

	private static char charAt(List<String> lines, int x, int y) {
		if (y < 0 || y >= lines.size()) {
			return '.';
		}
		if (x < 0 || x >= lines.get(y).length()) {
			return '.';
		}
		return lines.get(y).charAt(x);
	}

}
