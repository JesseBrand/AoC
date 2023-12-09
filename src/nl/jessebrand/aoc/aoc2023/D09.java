package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.parseIntsFromString;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class D09 {
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d09");
		System.out.println(lines);
		final List<List<Integer>> ranges = parseRanges(lines);
		long resultA = 0;
		long resultB = 0;
		for (List<Integer> range: ranges) {
			resultA += predictNext(range, new NextValue());
			resultB += predictNext(range, new PreviousValue());
		}
		System.out.println("a: " + resultA);
		System.out.println("b: " + resultB);
	}
	
	private interface NextValueGenerator {
		int nextValue(List<Integer> range, int nextPrediction);
	}
	
	private static class NextValue implements NextValueGenerator {
		@Override
		public int nextValue(final List<Integer> range, final int nextPrediction) {
			return range.get(range.size() - 1) + nextPrediction;
		}
	}
	
	private static class PreviousValue implements NextValueGenerator {
		@Override
		public int nextValue(final List<Integer> range, final int nextPrediction) {
			return range.get(0) - nextPrediction;
		}
	}

	private static List<List<Integer>> parseRanges(List<String> lines) {
		final List<List<Integer>> result = new ArrayList<>();
		for (String line : lines) {
			result.add(parseIntsFromString(line));
		}
		return result;
	}

	private static int predictNext(List<Integer> range, NextValueGenerator valueGenerator) {
		if (areAllZeroes(range)) {
			return 0;
		}
		final List<Integer> newRange = new ArrayList<>();
		for (int i = 1; i < range.size(); i++) {
			newRange.add(range.get(i) - range.get(i - 1));
		}
		int rangeNext = predictNext(newRange, valueGenerator);
		return valueGenerator.nextValue(range, rangeNext);
	}

	private static boolean areAllZeroes(List<Integer> range) {
		boolean result = true;
		for (int i : range) {
			if (i != 0) {
				result = false;
			}
		}
		return result;
	}

}
