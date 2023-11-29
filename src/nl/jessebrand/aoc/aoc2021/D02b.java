package nl.jessebrand.aoc.aoc2021;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.List;

public class D02b {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2021/d2");
		int x = 0, y = 0, aim = 0;
		for (final String line : lines) {
			if (line.startsWith("forward ")) {
				int value = Integer.parseInt(line.substring("forward ".length()));
				x += value;
				y += aim * value;
			}
			if (line.startsWith("down ")) {
				aim += Integer.parseInt(line.substring("down ".length()));
			}
			if (line.startsWith("up ")) {
				aim -= Integer.parseInt(line.substring("up ".length()));
			}
		}
		System.out.println(String.format("End position: x=%d y=%d total=%d", x, y, x*y));
		
	}
}
