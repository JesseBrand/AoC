package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.parseLines;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.jessebrand.aoc.Utils;

public class D02 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d02");
		final List<List<Integer>> reports = parseLines(lines, Utils::parseIntsFromString);
//		System.out.println(reports);
		
		System.out.println(reports.stream().filter(D02::isSafe1).count());
		System.out.println(reports.stream().filter(D02::isSafe2).count());
	}
	
	private static boolean isSafe1(final List<Integer> report) {
		return simpleCheck(report, 1, 3) || simpleCheck(report, -3, -1);
	}
	
	private static boolean isSafe2(final List<Integer> report) {
		return check(report, 1, 3) || check(report, -3, -1);
	}

	private static boolean simpleCheck(final List<Integer> report, final int minInc, final int maxInc) {
		for (int i = 1; i < report.size(); i++) {
			if (!isWithinDiff(report, i - 1, i, minInc, maxInc)) {
				return false;
			}
		}
		return true;
	}

	private static boolean check(final List<Integer> report, final int minInc, final int maxInc) {
		if (simpleCheck(report, minInc, maxInc)) {
			return true;
		}
		System.out.println(report + " unsafe");
		for (int i = 0; i < report.size(); i++) {
			final List<Integer> subList = new ArrayList<>(report);
			subList.remove(i);
			System.out.print(" trying " + subList + "... ");
			if (simpleCheck(subList, minInc, maxInc)) {
				System.out.println("NOW SAFE");
				return true;
			}
			System.out.println("unsafe");
		}
		return false;
	}

	private static boolean isWithinDiff(final List<Integer> report, final int iLeft, final int iRight, final int minInc, final int maxInc) {
		final int change = report.get(iRight) - report.get(iLeft);
		return change >= minInc && change <= maxInc;
	}
}
