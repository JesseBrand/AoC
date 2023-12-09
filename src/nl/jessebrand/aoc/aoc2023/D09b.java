package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.parseIntsFromString;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class D09b {
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d09");
		System.out.println(lines);
		final List<List<Integer>> ranges = parseRanges(lines);
		long result = 0;
		for (List<Integer> range: ranges) {
			int next = predictNext(range);
			result += next;
		}
		System.out.println(result);
	}

	private static List<List<Integer>> parseRanges(List<String> lines) {
		final List<List<Integer>> result = new ArrayList<>();
		for (String line : lines) {
			result.add(parseIntsFromString(line));
		}
		return result;
	}

	private static int predictNext(List<Integer> range) {
		if (allZeroes(range)) {
			return 0;
		}
		final List<Integer> newRange = new ArrayList<>();
		for (int i = 1; i < range.size(); i++) {
			newRange.add(range.get(i) - range.get(i - 1));
		}
		int rangeNext = predictNext(newRange);
		return range.get(range.size() - 1) + rangeNext;
	}

	private static boolean allZeroes(List<Integer> range) {
		boolean result = true;
		for (int i : range) {
			if (i != 0) {
				result = false;
			}
		}
		return result;
	}
	
	

}
