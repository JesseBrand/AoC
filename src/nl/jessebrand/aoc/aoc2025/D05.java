package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.contains;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.parseLongFromStrings;
import static nl.jessebrand.aoc.Utils.parseLongTuplesFromStrings;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.Utils.splitOnEmpty;
import static nl.jessebrand.aoc.Utils.union;

import java.io.IOException;
import java.util.List;

import nl.jessebrand.aoc.Tuple;

public class D05 {

	public static void main(final String[] args) throws IOException {
		solve("2025/d05ex");
		solve("2025/d05");
	}

	private static void solve(final String file) throws IOException {
		final List<String> lines = readFile(file);
		final Input input = parseInput(lines);
		out(input.ranges);
		out(input.ingredients);

		final long totalA = input.ingredients.stream().filter(l -> contains(input.ranges, l)).count();
		out("Part 1: %d", totalA);
		final List<Tuple<Long>> merged = union(input.ranges);
//		out(merged);
		final long totalB = merged.stream().mapToLong(D05::count).sum();
		out("Part 2: %d", totalB);
		out();
	}

	private record Input(List<Tuple<Long>> ranges, List<Long> ingredients) {}

	private static Input parseInput(final List<String> lines) {
		final List<List<String>> split = splitOnEmpty(lines);
		final List<Tuple<Long>> ranges = parseLongTuplesFromStrings(split.get(0));
		final List<Long> ingredients = parseLongFromStrings(split.get(1));
		return new Input(ranges, ingredients);
	}

	private static long count(final Tuple<Long> range) {
		return range.l2() - range.l1() + 1;
	}
}

// 359913027576322
