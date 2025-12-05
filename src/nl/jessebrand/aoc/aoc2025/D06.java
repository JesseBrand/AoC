package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.List;

public class D06 {

	public static void main(final String[] args) throws IOException {
		solve("2025/d01ex");
		solve("2025/d01");
	}

	private static void solve(final String file) throws IOException {
		final List<String> lines = readFile(file);
		out(lines);

//		out("Part 1: %d", resultA);
//		out("Part 2: %d", resultB);
	}
}

