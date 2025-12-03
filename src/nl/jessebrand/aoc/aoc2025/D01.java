package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.List;

public class D01 {

	public static void main(final String[] args) throws IOException {
		solve("2025/d01ex");
		solve("2025/d01");
	}

	private static void solve(final String file) throws IOException {
		final List<String> lines = readFile(file);
		out(lines);
		
		int dial = 50;
		int r1 = 0;
		int r2 = 0;
		
		for (final String line : lines) {
			int val = Integer.parseInt(line.substring(1));;
			if (line.startsWith("L")) {
				val = -val;
			}
			r2 += Math.abs(val) / 100;
			val %= 100;
			if (dial + val >= 100) {
				r2++;
			}
			if (dial > 0 && dial + val <= 0) {
				r2++;
			}
			dial += val + 100;
			dial %= 100;
			if (dial == 0) {
				r1++;
			}
//			out("%s > %d - %d/%d", instr, r.value, r.a, r.b);
		}
		out("Part 1: %d", r1);
		out("Part 2: %d", r2);
		out();
	}
}

// 6580 wrong
// 5998 wrong
// 6558
