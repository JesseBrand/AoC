package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.charAtSafe;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.Utils.substring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class D03b {
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d03");
		System.out.println(lines);
		int total = 0;
		final List<Number> numbers = new ArrayList<>();
		for (int y = 0; y < lines.size(); y++) {
			final String line = lines.get(y);
			for (int x = 0; x < line.length(); x++) {
				char c = charAtSafe(lines, x, y);
				if (c >= '0' && c <= '9') {
					final int startX = x;
					while (c >= '0' && c <= '9') {
						x++;
						c = charAtSafe(lines, x, y);
					}
					final int number = Integer.parseInt(substring(lines, y, startX, x));
					numbers.add(new Number(number, y, startX, x - 1));
				}
			}
		}
		for (int y = 0; y < lines.size(); y++) {
			final String line = lines.get(y);
			for (int x = 0; x < line.length(); x++) {
				char c = charAtSafe(lines, x, y);
				if (c == '*') {
					final List<Number> adjacentNumbers = findNumbers(numbers, x, y);
					System.out.println(String.format("Gear at [r%d c%d]: %d adjacent numbers", y, x, adjacentNumbers.size()));
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

}
