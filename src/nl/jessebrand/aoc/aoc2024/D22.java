package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.jessebrand.aoc.Utils;

public class D22 {

	private static record Combination(int i0, int i1, int i2, int i3) {
		public int[] nrs() {
			return new int[] {i0, i1, i2, i3};
		}
		
		public String toString() {
			return String.format("%d,%d,%d,%d", i0, i1, i2, i3);
		}
	}

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d22");
		final List<List<Long>> list = lines.stream().mapToLong(s -> Long.parseLong(s)).mapToObj(l -> calculate(l, 2000)).toList();
		out("1: %d", list.stream().mapToLong(D22::last).sum());
		final List<List<Integer>> lastList = list.stream().map(ll -> (List<Integer>) ll.stream().map(l -> "" + l).mapToInt(s -> Integer.parseInt(s.substring(s.length() - 1))).mapToObj(i -> i).toList()).toList();
		final List<Combination> combinations = gatherCombinations();
		out("Lists: %d of %d", lastList.size(), lastList.get(0).size());
		out("Combinations: %d", combinations.size());
//		out("Combinations: %s", combinations);
//		out("ex: %d", lastList.stream().mapToInt(il -> findFirstOccurance(il, new Combination(-2, 1, -1, 3)) ).sum());

		final Map<Combination, Integer> map = new HashMap<>();
		final long ms = System.currentTimeMillis();

//		out(lastList.parallelStream().mapToInt(il -> findFirstOccurance(il, new Combination(-1, 0, -1, 2))).sum());
//		out(Utils.glue("+", lastList.parallelStream().mapToInt(il -> findFirstOccurance(il, new Combination(-1, 0, -1, 2))).mapToObj(i->i).toList()));	

		combinations.parallelStream().forEach(c -> map.put(c, lastList.parallelStream().mapToInt(il -> findFirstOccurance(il, c)).sum()));
		out(map);
		final Combination highest = map.entrySet().stream().reduce((e0, e1) -> e0.getValue() < e1.getValue() ? e1 : e0).map(e -> e.getKey()).get();
		out("2: %d (%s)", map.get(highest), highest);
		out("Duration: %ds (%d lines)", (System.currentTimeMillis() - ms) / 1000, lines.size());

//		out("2: %d", combinations.stream().mapToInt(c -> lastList.stream().mapToInt(il -> findFirstOccurance(il, c)).sum()).max().getAsInt());
//		out(lastList);
//		out(calculate(123, 2000));
	}
	
	private static int findFirstOccurance(final List<Integer> secrets, final Combination c) {
		for (int i = 0; i < secrets.size() - 4; i++) {
			final int[] is = {secrets.get(i), secrets.get(i + 1), secrets.get(i + 2), secrets.get(i + 3), secrets.get(i + 4)};
			if (is[1] - is[0] == c.i0()
					&& is[2] - is[1] == c.i1()
					&& is[3] - is[2] == c.i2()
					&& is[4] - is[3] == c.i3()) {
				return is[4];
			}
		}
		return 0;
	}

	private static List<Combination> gatherCombinations() {
		final List<Combination> result = new ArrayList<>(10000);
		for (int i0 = -9 ; i0 < 10; i0++) {
			for (int i1 = -9 ; i1 < 10; i1++) {
				for (int i2 = -9 ; i2 < 10; i2++) {
					for (int i3 = -9 ; i3 < 10; i3++) {
						result.add(new Combination(i0, i1, i2, i3));
					}
				}
			}
		}
		
		return result.stream().filter(D22::isValid).toList();
	}

	private static <T> T last(final List<T> list) {
		return list.get(list.size() - 1);
	}

	private static List<Long> calculate(final long secret, final int times) {
		final List<Long> result = new ArrayList<>(times);
		long l = secret;
		for (int i = 0; i < times; i++) {
			l = prune(mix(l, l * 64));
			l = prune(mix(l, l / 32));
			l = prune(mix(l, l * 2048));
			result.add(l);
		}
		return result;
	}

	private static long prune(final long l) {
		return l % 16777216;
	}

	private static long mix(final long l1, final long l2) {
		return l1 ^ l2;
	}

	private static boolean isValid(final Combination c) {
		int min = 9, max = 0;
		for (int i : c.nrs()) {
			min += i;
			max += i;
			if (min < 0 || max > 9) {
				return false;
			}
		}
		return true;
	}
}

// 2089
