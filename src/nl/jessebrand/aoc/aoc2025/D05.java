package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.parseLongsFromStringList;
import static nl.jessebrand.aoc.Utils.parseLongsFromStrings;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.jessebrand.aoc.Tuple;

public class D05 {

	public static void main(final String[] args) throws IOException {
		solve("2025/d05ex");
		solve("2025/d05");
	}

	private static void solve(final String file) throws IOException {
		final List<String> lines = readFile(file);
		List<List<Long>> ranges = null;
		List<Long> ingredients = null;
		for (int i = 0; i < lines.size(); i++) {
			final String line = lines.get(i);
			if (!line.isEmpty()) {
				continue;
			}
			ranges = parseLongsFromStrings(lines.subList(0, i), "-");
			ingredients = parseLongsFromStringList(lines.subList(i + 1, lines.size()));
			break;
		}
		out(ranges);
		out(ingredients);
		
		final List<Tuple<Long>> ranges2 = toTuples(ranges);
		final long totalA = ingredients.stream().filter(l -> isIncluded(l, ranges2)).count();
		out("1: %d", totalA);
		final List<Tuple<Long>> merged = mergeRanges(ranges2);
		out(merged);
		final long totalB = merged.stream().mapToLong(D05::count).sum();
		out("2: %d", totalB);
		out();
	}

	private static List<Tuple<Long>> mergeRanges(final List<Tuple<Long>> ranges) {
		final List<Tuple<Long>> result = new ArrayList<>(ranges);
		for (int i = 0; i < result.size(); i++) {
			for (int j = i + 1; j < result.size(); j++) {
				if (overlaps(result.get(i), result.get(j))) {
					result.set(i, merge(result.get(i), result.get(j)));
					result.remove(j);
					j = i;
				}
			}
		}
		return result;
	}

	private static boolean overlaps(Tuple<Long> r1, Tuple<Long> r2) {
		return (r1.l1() >= r2.l1() && r1.l1() <= r2.l2())
				|| (r2.l1() >= r1.l1() && r2.l1() <= r1.l2());
	}

	private static Tuple<Long> merge(Tuple<Long> r1, Tuple<Long> r2) {
		return new Tuple<Long>(Math.min(r1.l1(), r2.l1()), Math.max(r1.l2(), r2.l2()));
	}

	private static List<Tuple<Long>> toTuples(List<List<Long>> ranges) {
		return ranges.stream().map(r -> new Tuple<Long>(r.get(0), r.get(1))).toList();
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
