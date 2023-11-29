package nl.jessebrand.aoc.aoc2021;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.List;

public class D01 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2021/d1");
		int result = 0;
		int i = 0;
		for (final String line : lines) {
			int j = Integer.parseInt(line);
			if (i != 0 && j > i) {
				result++;
			}
			i = j;
		}
		System.out.println(result);
	}
}
