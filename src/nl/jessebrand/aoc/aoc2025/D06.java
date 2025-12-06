package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class D06 {

	public static void main(final String[] args) throws IOException {
		solve("2025/d06ex");
		solve("2025/d06");
	}

	private static enum Operator {
		PLUS("+") {
			@Override
			long apply(long a, long b) {
				return a + b;
			}
		},
		MULTI("*") {
			@Override
			long apply(long a, long b) {
				return a * b;
			}
		};

		private final String symbol;

		private Operator(String symbol) {
			this.symbol = symbol;
		}

		static final Operator from(final String symbol) {
			for (final Operator o : values()) {
				if (o.symbol.equals(symbol)) {
					return o;
				}
			}
			throw new IllegalArgumentException(symbol);
		}

		abstract long apply(final long a, final long b);
		
	}

	private static void solve(final String file) throws IOException {
		final List<String> lines = readFile(file);
		List<Problem> problems = parseProblems(lines);
		long resultA = 0;
		for (final Problem problem : problems) {
			List<Long> longs = problem.strings().stream().map(String::trim).map(Long::parseLong).toList();
			resultA += solve(longs, problem.o);
		}
		
		long resultB = 0;
		for (final Problem problem : problems) {
			List<Long> longs = convertLongs(problem.strings());
			resultB += solve(longs, problem.o);
		}

		out("Part 1: %d", resultA);
		out("Part 2: %d", resultB);
	}

	private static List<Problem> parseProblems(List<String> lines) {
		final List<Problem> result = new ArrayList<>();
		final int length = lines.get(0).length();
		int start = 0;
		for (int i = 0; i <= length; i++) {
			if (i == length || eachBlank(lines, i)) {
				final int from = start;
				final int to = i;
				List<String> strings = lines.stream().map(l -> l.substring(from, to)).toList();
				out(strings);
				Operator o = Operator.from(strings.get(strings.size() - 1).trim());
				strings = strings.subList(0, strings.size() - 1);
				result.add(new Problem(strings, o));
				start = i + 1;
			}
		}
		return result;
	}

	private static boolean eachBlank(List<String> lines, int i) {
		for (String line : lines) {
			if (line.charAt(i) != ' ') {
				return false;
			}
		}
		return true;
	}

	private record Problem(List<String> strings, Operator o) {}


	private static long solve(final List<Long> ints, final Operator o) {
		return ints.stream().reduce(o::apply).get();
	}

	private static List<Long> convertLongs(List<String> strings) {
		final List<Long> result = new ArrayList<>();
		for (int i = 0; i < strings.get(0).length(); i++) {
			result.add(Long.parseLong(fromEach(strings, i).trim()));
		}
		out(result);
		return result;
	}

	private static String fromEach(List<String> strings, int i) {
		String result = "";
		Object[] cs = strings.stream().map(s -> s.charAt(i)).toArray();
		for (Object c : cs) {
			result += (char) c;
		}
		return result;
	}
}

