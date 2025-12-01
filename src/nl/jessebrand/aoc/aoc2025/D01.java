package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.*;
import static nl.jessebrand.aoc.Utils.parseColumnsAsInts;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

public class D01 {

	public static void main(String[] args) throws IOException {
		run("2025/d01ex");
		run("2025/d01");
	}

	private static void run(final String file) throws IOException {
		final List<String> lines = readFile(file);
		out(lines);
		
		int r = 50;
		int a = 0;
		int b = 0;
		
		for (final String instr : lines) {
			int v = Integer.parseInt(instr.substring(1));;
			if (instr.startsWith("L")) {
				v = -v;
			}
			b += Math.abs(v) / 100;
			v %= 100;
			if (r + v >= 100) {
				b++;
			}
			if (r > 0 && r + v <= 0) {
				b++;
			}
			r += v + 100;
			r %= 100;
			if (r == 0) {
				a++;
			}
//			out("%s > %d - %d/%d", instr, r.value, r.a, r.b);
		}
		out("1: %d", a);
		out("2: %d", b);
		out();
	}
}

// 6580 wrong
// 5998 wrong
// 6558
