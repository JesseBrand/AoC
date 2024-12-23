package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.glue;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.jessebrand.aoc.Tuple;

public class D23 {

	public record Triple<T>(T l1, T l2, T l3) {
		@Override
		public String toString() {
			return String.format("%s-%s-%s", l1, l2, l3);
		}
	}

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d23");
		final List<Tuple<String>> pairs = lines.stream().map(D23::parsePair).toList();
		final List<Triple<String>> sets = findTriplets(pairs);
		out(sets); // 1411
		out("1: %d", sets.stream().filter(t -> t.l1().startsWith("t") || t.l2().startsWith("t") || t.l3().startsWith("t")).count());
		final Collection<List<String>> allSets = findAllSets(pairs);
		out(allSets);
		out("2: %s", glue(",", allSets.stream().reduce((s1, s2) -> s1.size() > s2.size() ? s1 : s2).get())); // aq,bn,ch,dt,gu,ow,pk,qy,tv,us,yx,zg,zu
	}

	private static Collection<List<String>> findAllSets(final List<Tuple<String>> pairs) {
		final Collection<List<String>> result = new HashSet<>();
		for (final Tuple<String> pair : pairs) {
			result.addAll(findAllContaining(pairs, Arrays.asList(pair.l1(), pair.l2())));
		}
		return result;
	}

	private static Collection<List<String>> findAllContaining(final List<Tuple<String>> pairs, final List<String> values) {
		final Set<List<String>> result = new HashSet<>();
		for (final Tuple<String> pair : pairs) {
			String l1 = pair.l1();
			String l2 = pair.l2();
			if (l1 != values.get(0)) {
				continue;
			}
			if (values.contains(l2)) {
				continue;
			}
			boolean match = true;
			for (final String value : values) {
				if (!contains(pairs, value, l2)) {
					match = false;
				}
			}
			if (match) {
				final List<String> newList = new ArrayList<>(values);
				newList.add(l2);
				result.add(newList);
				result.addAll(findAllContaining(pairs, newList));
			}
		}
		return result;
	}

	private static Tuple<String> parsePair(final String line) {
		final List<String> split = Arrays.asList(line.split("-"));
		Collections.sort(split);
		return new Tuple<>(split.get(0).intern(), split.get(1).intern());
	}

	private static List<Triple<String>> findTriplets(List<Tuple<String>> pairs) {
		final List<Triple<String>> result = new ArrayList<>();
		for (final Tuple<String> pair1 : pairs) {
			final String s0 = pair1.l1();
			final String s1 = pair1.l2();
			for (final Tuple<String> pair2 : pairs) {
				if (pair2.l1() == s0 && pair2.l2() != s1) {
					final String s2 = pair2.l2();
//					out("%s-%s-%s: %b,%b", s0, s1, s2, contains(pairs, s1, s2), contains(result, s0, s1, pair2.l2()));
					if (contains(pairs, s1, s2) && !contains(result, s0, s1, pair2.l2())) {
						result.add(new Triple<>(s0, s1, pair2.l2()));
					}
				}
			}
		}
		return result;
	}

	private static boolean contains(List<Tuple<String>> tuples, String s0, String s1) {
		for (final Tuple<String> tuple : tuples) {
			if (tuple.l1() == s0 && tuple.l2() == s1) {
				return true;
			}
		}
		return false;
	}

	private static boolean contains(List<Triple<String>> triples, String s0, String s1, String s2) {
		for (final Triple<String> triple : triples) {
			if (triple.l1() == s0 && triple.l2() == s1 && triple.l3() == s2) {
				return true;
			}
		}
		return false;
	}

}
