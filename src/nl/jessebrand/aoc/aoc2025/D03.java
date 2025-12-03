package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.*;

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
		long totalA = lines.parallelStream().map(line -> findBiggestX(line, 2)).reduce((a, b) -> a + b).get();
		long totalB = lines.parallelStream().map(line -> findBiggestX(line, 12)).reduce((a, b) -> a + b).get();
		out("1: %d", totalA);
		out("2: %d", totalB);
	}

	private static long findBiggestX(String line, int count) {
		int i = highestIndex(line, count - 1);
		return Long.parseLong("" + line.charAt(i) + (count == 1 ? "" : findBiggestX(line.substring(i + 1), count - 1)));
	}

	private static final char[] C = {'9', '8', '7', '6', '5', '4', '3', '2', '1', '0'};
	
	private static int highestIndex(String line, int count) {
		for (char c : C) {
			int i = line.indexOf(c);
			if (i != -1 && i + count < line.length()) {
//				out("Highest in %s with %d left: %d", line, count, i);
				return i;
			}
		}
		throw new IllegalStateException("\"" + line + "\" " + count);
	}
}

// 17432
// 173065202451341
