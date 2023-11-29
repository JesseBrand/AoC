package nl.jessebrand.aoc.aoc2021;

import static nl.jessebrand.aoc.Utils.*;
import static nl.jessebrand.aoc.Utils.readFileCSInts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class D7 {
	
	public static void main(String[] args) throws IOException {
		final List<Integer> input = readFileCSInts("2021/d7");
		final int total = countTotal(input);
		
		int min = findMin(input);
		int max = findMax(input);
		
		int lowest = Integer.MAX_VALUE;
		int lowestX = 0;
		
		for (int x = min; x <= max; x++) {
			int value = countTotal(calcDiffs1(input, x));
			System.out.println(String.format("Total fuel for X=%d: %d", x, value));
			if (value < lowest) {
				lowest = value;
				lowestX = x;
			}
			lowest = Math.min(lowest, value);
		}
		System.out.println(String.format("Result 1: %d (X=%s)", lowest, lowestX));

		lowest = Integer.MAX_VALUE;
		
		for (int x = min; x <= max; x++) {
			int value = countTotal(calcDiffs2(input, x));
			System.out.println(String.format("Total fuel for X=%d: %d", x, value));
			if (value < lowest) {
				lowest = value;
				lowestX = x;
			}
			lowest = Math.min(lowest, value);
		}
		System.out.println(String.format("Result 2: %d (X=%s)", lowest, lowestX));
	}

	private static List<Integer> calcDiffs1(List<Integer> input, int meanVal) {
		final List<Integer> result = new ArrayList<>(input.size());
		for (int i : input) {
			result.add(Math.abs(i - meanVal));
		}
		return result;
	}

	private static List<Integer> calcDiffs2(List<Integer> input, int meanVal) {
		final List<Integer> result = new ArrayList<>(input.size());
		for (int i : input) {
			result.add(nthTriNumber(Math.abs(i - meanVal)));
		}
		return result;
	}
}

// 362755 too high
