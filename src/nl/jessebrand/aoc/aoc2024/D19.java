package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D19 {
	
	private static int current = 0;
	private static int total = 0;
	
	private static Map<String, Long> CACHE = new HashMap<>();

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d19");
		final List<String> available = Arrays.asList(lines.get(0).split(", "));
//		out(available);
		final List<String> desired = lines.subList(2, lines.size());
//		out(desired);
		total = desired.size();
		final List<Long> result = desired.stream().map(d -> checkPossible(d, available, true)).toList();
		out("1: %d", result.size());
		out("2: %d", result.stream().mapToLong(i -> i).sum());
	}

	private static long checkPossible(final String design, final List<String> available, final boolean outer) {
		if (CACHE.containsKey(design)) {
			return CACHE.get(design);
		}
		long sum = 0;
		for (final String a : available) {
			if (design.equals(a)) {
				sum++;
				continue;
			}
			if (!design.startsWith(a)) {
				continue;
			}
//			out("%s matches %s, continue with %s", design, a, design.substring(a.length()));
			sum += checkPossible(design.substring(a.length()), available, false);
		}
		if (outer) {
			out("%d/%d %s: %d", current++, total, design, sum);
		}
		CACHE.put(design, sum);
		return sum;
	}
}
