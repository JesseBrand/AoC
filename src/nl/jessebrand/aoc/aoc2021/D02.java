package nl.jessebrand.aoc.aoc2021;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.List;

public class D02 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2021/d2");
		int x = 0, y = 0;
		for (final String line : lines) {
			if (line.startsWith("forward ")) {
				x += Integer.parseInt(line.substring("forward ".length()));
			}
			if (line.startsWith("down ")) {
				y += Integer.parseInt(line.substring("down ".length()));
			}
			if (line.startsWith("up ")) {
				y -= Integer.parseInt(line.substring("up ".length()));
			}
		}
		System.out.println(String.format("End position: x=%d y=%d total=%d", x, y, x*y));
		
	}
}
