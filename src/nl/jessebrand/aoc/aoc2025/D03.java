package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.List;

public class D03 {

	public static void main(final String[] args) throws IOException {
		solve("2025/d03ex");
		solve("2025/d03");
	}

	private static void solve(final String file) throws IOException {
		final List<String> lines = readFile(file);
//		out(lines);
		final long totalA = lines.parallelStream().mapToLong(line -> findBiggestX(line, 2)).sum();
		final long totalB = lines.parallelStream().mapToLong(line -> findBiggestX(line, 12)).sum();
		out("Part 1: %d", totalA);
		out("Part 2: %d", totalB);
	}

	private static long findBiggestX(final String line, final int charCount) {
		final int i = highestIndex(line, charCount - 1);
		return Long.parseLong("" + line.charAt(i) + (charCount == 1 ? "" : findBiggestX(line.substring(i + 1), charCount - 1)));
	}

	private static final char[] CHARS = "98765431210".toCharArray();
	
	private static int highestIndex(final String line, final int remaining) {
		for (char c : CHARS) {
			final int i = line.indexOf(c);
			if (i != -1 && i + remaining < line.length()) {
				return i;
			}
		}
		throw new IllegalArgumentException("\"" + line + "\" " + remaining);
	}
}

// 17432
// 173065202451341
