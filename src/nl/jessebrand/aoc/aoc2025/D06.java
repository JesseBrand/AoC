package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nl.jessebrand.aoc.Operator;

public class D06 {

	public static void main(final String[] args) throws IOException {
		solve("2025/d06ex");
		solve("2025/d06");
	}

	private static void solve(final String file) throws IOException {
		final List<String> lines = readFile(file);
		final List<Problem> problems = parseProblems(lines);
		long resultA = problems.stream().mapToLong(p -> p.o.apply(p.strings().stream().map(String::trim).map(Long::parseLong).toList())).sum();
		long resultB = problems.stream().mapToLong(p -> p.o.apply(convertLongs(p.strings()))).sum();

		out("Part 1: %d", resultA);
		out("Part 2: %d", resultB);
	}

	private static List<Problem> parseProblems(final List<String> lines) {
		final List<Problem> result = new ArrayList<>();
		final int length = lines.get(0).length();
		int start = 0;
		for (int i = 0; i <= length; i++) {
			if (i == length || eachBlank(lines, i)) {
				final int from = start;
				final int to = i;
				List<String> strings = lines.stream().map(l -> l.substring(from, to)).toList();
				final Operator o = Operator.from(strings.get(strings.size() - 1).trim());
				strings = strings.subList(0, strings.size() - 1);
				result.add(new Problem(strings, o));
				start = i + 1;
			}
		}
		return result;
	}

	private static boolean eachBlank(final List<String> lines, final int i) {
		return !lines.stream().filter(s -> s.charAt(i) != ' ').findAny().isPresent();
	}

	private record Problem(List<String> strings, Operator o) {}

	private static List<Long> convertLongs(final List<String> strings) {
		final List<Long> result = new ArrayList<>();
		for (int i = 0; i < strings.get(0).length(); i++) {
			result.add(Long.parseLong(fromEach(strings, i).trim()));
		}
		return result;
	}

	private static String fromEach(final List<String> strings, final int i) {
		return strings.stream().map(s -> s.charAt(i)).map(c -> c.toString()).collect(Collectors.joining());
	}
}

// 5335495999141
// 10142723156431
