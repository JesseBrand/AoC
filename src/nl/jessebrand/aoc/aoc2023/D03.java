package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.charAtSafe;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.Utils.substring;

import java.io.IOException;
import java.util.List;

public class D03 {
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d03");
		int total = 0;
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
					final char symbol = findSymbol(lines, startX - 1, x, y - 1, y + 1);
					if (symbol != 0) {
						System.out.println(String.format("Added %d [r%d c%d] because of %c", number, y, x, symbol));
						total += number;
					} else {
						System.out.println(String.format("Skipped %d [r%d c%d]", number, y, x));
					}
				}
			}
			System.out.println();
		}
		System.out.println(total);
	}

	private static char findSymbol(final List<String> lines, final int startX, final int endX, final int startY, final int endY) {
		for (int y = startY; y <= endY; y++) {
			for (int x = startX; x <= endX; x++) {
				char c = charAtSafe(lines, x, y);
				if (c != '.' && !(c >= '0' && c <= '9')) {
					return c;
				}
			}
		}
		return 0;
	}
}
