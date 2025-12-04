package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.*;

import java.io.IOException;
import java.util.List;

public class D05 {

	public static void main(final String[] args) throws IOException {
		solve("2025/d05ex");
		solve("2025/d05");
	}

	private static void solve(final String file) throws IOException {
		final List<String> lines = readFile(file);
		out(lines);
	}
}
