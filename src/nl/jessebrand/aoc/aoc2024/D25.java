package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.glue;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class D25 {

	public static void main(String[] args) throws IOException {
		solve("2024/d25ex");
	}
	
	private static record Combination(boolean key, int[] lengths) {
		public String toString() {
			return String.format("%s[%s]", key ? "KEY" : "LCK", glue(",", lengths));
		}
	}
	
	private static void solve(final String file) throws IOException {
		final List<String> lines = readFile("2024/d25");
		final List<Combination> combos = new ArrayList<>();
		for (int i = 0; i < lines.size(); i += 8) {
			combos.add(parseCombination(lines.subList(i, i + 7)));
		}
		final List<Combination> locks = combos.stream().filter(c -> !c.key()).toList();
		final List<Combination> keys = combos.stream().filter(c -> c.key()).toList();
		out("1: %d", locks.stream().mapToLong(l -> {
			return keys.stream().filter(k -> fits(l, k)).count();
		}).sum());
	}

	private static boolean fits(final Combination l, final Combination k) {
		for (int i = 0; i < l.lengths().length; i++) {
			if (l.lengths()[i] + k.lengths()[i] > 5) {
				return false;
			}
		}
		return true;
	}

	private static Combination parseCombination(final List<String> list) {
		final List<String> subList = list.subList(1, 6); 
		int[] lengths = IntStream.range(0, 5).map(i -> {
			return (int) subList.stream().map(s -> s.charAt(i)).filter(c -> c == '#').count();
		}).toArray();
		if (list.get(0).equals(".....") && list.get(6).equals("#####")) {
			// key
			return new Combination(true, lengths);
		}
		if (list.get(0).equals("#####") && list.get(6).equals(".....")) {
			// lock
			return new Combination(false, lengths);
		}
		throw new IllegalStateException("" + list);
	}
}
