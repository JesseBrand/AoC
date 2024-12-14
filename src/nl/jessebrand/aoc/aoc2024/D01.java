package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.*;
import static nl.jessebrand.aoc.Utils.parseColumnsAsInts;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

public class D01 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d01");
		final List<List<Integer>> cols = parseColumnsAsInts(lines);
		for (List<Integer> list : cols) {
			list.sort(null);
		}
		out(cols);

		final int size = cols.get(0).size();
		final List<Integer> list1 = cols.get(0);
		final List<Integer> list2 = cols.get(1);
		
		out("1: %s", IntStream.range(0, size).map(i -> Math.abs(list1.get(i) - list2.get(i))).sum());
		
		out("2: %s", list1.stream().mapToLong(i -> i * countOccurances(list2, i)).sum());
	}
}
