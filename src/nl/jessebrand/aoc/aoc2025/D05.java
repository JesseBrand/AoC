package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.mergeRanges;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.parseLongFromStrings;
import static nl.jessebrand.aoc.Utils.parseLongTuplesFromStrings;
import static nl.jessebrand.aoc.Utils.readFile;

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

		final long totalA = input.ingredients.stream().filter(l -> isIncluded(l, input.ranges)).count();
		out("1: %d", totalA);
		final List<Tuple<Long>> merged = mergeRanges(input.ranges);
//		out(merged);
		final long totalB = merged.stream().mapToLong(D05::count).sum();
		out("2: %d", totalB);
		out();
	}

	private record Input(List<Tuple<Long>> ranges, List<Long> ingredients) {}

	private static Input parseInput(final List<String> lines) {
		for (int i = 0; i < lines.size(); i++) {
			final String line = lines.get(i);
			if (!line.isEmpty()) {
				continue;
			}
			final List<Tuple<Long>> ranges = parseLongTuplesFromStrings(lines.subList(0, i));
			final List<Long> ingredients = parseLongFromStrings(lines.subList(i + 1, lines.size()));
			return new Input(ranges, ingredients);
		}
		throw new IllegalStateException();
	}

	private static boolean isIncluded(final long l, final List<Tuple<Long>> ranges) {
		for (final Tuple<Long> range : ranges) {
			if (l >= range.l1() && l <= range.l2()) {
				return true;
			}
		}
		return false;
	}

	private static long count(final Tuple<Long> range) {
		return range.l2() - range.l1() + 1;
	}
}

// 359913027576322
