package nl.jessebrand.aoc.aoc2021;

import static nl.jessebrand.aoc.Utils.readFileInts;

import java.io.IOException;
import java.util.List;

public class D01b {

	public static void main(String[] args) throws IOException {
		final List<Integer> lines = readFileInts("2021/d1");
		int result = 0;
		int last = 0;
		for (int i = 0; i < lines.size(); i++) {
			if (i >= 2) {
				int cur = lines.get(i - 2) + lines.get(i - 1) + lines.get(i);
				if (last > 0 && last < cur) {
					result++;
				}
				last = cur;
			}
		}
		System.out.println(result);
	}
}

//1944 too high