package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.assertEquals;
import static nl.jessebrand.aoc.Utils.parseLines;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class D12 {
	
	public static void main(String[] args) throws IOException {
		runTests();
		
		final List<String> lines = readFile("2023/d12");
		final List<Arrangement> arrangementsA = parseLines(lines, D12::parseArrangement);
//		System.out.println(arrangementsA);
		long totalA = arrangementsA.parallelStream().map(D12::findSolutions).reduce(0L, Long::sum);
		out("a: %d", totalA);
		
		final List<Arrangement> arrangementsB = parseLines(lines, D12::parseRepeatedArrangement);
//		System.out.println(arrangementsB);
		long totalB = arrangementsB.parallelStream().map(D12::findSolutions).reduce(0L, Long::sum);
		out("b: %d", totalB);
	}

	private static void runTests() {
		runTest(parseArrangement("#??????#??. 2,7"), 1);
		runTest(parseArrangement("?????.#??.???##?##?# 3,2,10"), 3);
		runTest(parseArrangement("#???##???.?.???? 7,1,1,1"), 10);
		runTest(parseArrangement("?###.????????#####?? 3,12"), 3);
		runTest(parseArrangement("?.??..#?##?# 1,6"), 3);
		runTest(parseArrangement("...?#.???.??. 2,1,1"), 7);
		runTest(parseArrangement("????##?????.????? 6,2,1"), 46);
		runTest(parseArrangement("??##..????.?#.?. 3,1,1,1,1"), 3);
		runTest(parseArrangement("...?#.???.??. 2,1,1"), 7);
		runTest(parseArrangement("???????#??##???? 9"), 5);
		runTest(parseArrangement("?.???????#??##???? 1,9"), 25);
		runTest(parseArrangement("?.?###??..? 4,1"), 3);
		runTest(parseArrangement("??#????#???.?..??. 5,3,1,1"), 9);
		runTest(parseArrangement("?.#??#?##?.?###???.#??#?##?.?###???.#??#?##?.?###???.#??#?##?.?###???.#??#?##?.?###? 1,1,6,4,1,1,6,4,1,1,6,4,1,1,6,4,1,1,6,4"), 162);
	}

	private static void runTest(Arrangement arrangement, int i) {
		assertEquals(i, findSolutions(arrangement));
	}

	private static long findSolutions(Arrangement a) {
		long total = findSolution(a.string() + ".", 0, a.sets().get(0), a.sets().subList(1, a.sets.size()));
		System.out.println(a + ": " + total);
		return total;
	}

	private static long findSolution(String string, int start, int setLength, List<Integer> remaining) {
		long total = 0;
//		int matches = 0;
		for (int i = start; i < string.length(); i++) {
			if (fits(string, start, setLength, i)) {
//				matches++;
				if (remaining.size() == 0) {
					if (string.indexOf('#', setLength + i) == -1) {
						total++;
					}
				} else {
					total += findSolution(string, i + setLength + 1, remaining.get(0), remaining.subList(1, remaining.size()));
				}
			}
		}
//		System.out.println(String.format("Solution of %s %d,%s: %d (%d matches)", string, set, remaining, total, matches));
		return total;
	}

	private static boolean fits(final String string, final int strStart, final int length, final int startPos) {
//		out("%s %d %d %d", string, start, length, startPos);
		for (int i = 0; i < length; i++) {
			if (string.charAt(startPos + i) == '.') {
				return false;
			}
		}
		if (string.length() > startPos + length && string.charAt(startPos + length) == '#') {
			return false;
		}
		for (int i = strStart; i < startPos; i++) {
			if (string.charAt(i) == '#') {
				return false;
			}
		}
//		System.out.println(String.format("%s %d fits", string.substring(startPos), length));
		return true;
	}

	private static void out(final String template, final Object... params) {
		System.out.println(String.format(template, params));
	}

	static Arrangement parseArrangement(final String line) {
		final String[] split = line.split("\\ ");
		return new Arrangement(split[0], toInt(split[1].split(",")));
	}

	static Arrangement parseRepeatedArrangement(final String line) {
		final String[] split = line.split("\\ ");
		return new Arrangement(repeat(split[0], 5, "?"), toInt(repeat(split[1], 5, ",").split(",")));
	}
	
	private static String repeat(String string, int count, String sep) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			if (i > 0) {
				sb.append(sep);
			}
			sb.append(string);
		}
		return sb.toString();
	}

	private static List<Integer> toInt(String[] split) {
		List<Integer> result = new ArrayList<>();
		for (String s : split) {
			result.add(Integer.parseInt(s));
		}
		return result;
	}

	private static record Arrangement(String string, List<Integer> sets) {}

}

// 9759
// 8099
