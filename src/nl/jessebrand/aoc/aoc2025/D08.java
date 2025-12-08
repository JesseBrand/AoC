package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.parsePoint3s;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import nl.jessebrand.aoc.Point3;

public class D08 {

	private static final Comparator<Connection> COMPARATOR = new Comparator<Connection>() {

		@Override
		public int compare(Connection o1, Connection o2) {
			return Double.compare(o1.distance(), o2.distance());
		}
		
	};

	public static void main(final String[] args) throws IOException {
		solve("2025/d08ex", 10);
		solve("2025/d08", 1000);
	}

	private static void solve(final String file, final int max) throws IOException {
		final List<String> lines = readFile(file);
		List<Point3> points = parsePoint3s(lines);
		List<Connection> conns = allCombinations(points);
		conns.sort(COMPARATOR);
		
		int count = 0;
		final Map<Point3, Integer> groups = new HashMap<>();
		for (int i = 0; i < points.size(); i++) {
			groups.put(points.get(i), i);
		}
		while (true) {
			final Connection conn = conns.get(count);
			final Integer g1 = groups.get(conn.p1());
			final Integer g2 = groups.get(conn.p2());
			if (g1 == g2) {
				// ignore
//				out("I: %s-%s already connected: ignored, %s", conn.p1(), conn.p2(), groupCounts(groups));
			} else {
				// merge
				groups.entrySet().stream().filter(e -> e.getValue() == g2).forEach(e -> e.setValue(g1));
//				out("M: %s-%s: Merged group %d into %d, %s", conn.p1(), conn.p2(), g2, g1, groupCounts(groups));
			}
			count++;
			if (count == max) {
				final List<Long> groupCounts = groupCounts(groups);
				Collections.sort(groupCounts);
				Collections.reverse(groupCounts);
//				out(groupCounts);
				out("Part 1: %d", groupCounts.get(0) * groupCounts.get(1) * groupCounts.get(2));
			}
			if (allSameGroup(groups)) {
				out("%d * %d", conn.p1().x(), conn.p2().x());
				out("Part 2: %d", (long) conn.p1().x() * conn.p2().x());
				break;
			}
		}
		out();
	}

	private static boolean allSameGroup(Map<Point3, Integer> groups) {
		final int group1 = groups.values().iterator().next();
		for (int i : groups.values()) {
			if (i != group1) {
				return false;
			}
		}
		return true;
//		return groups.values().stream().filter(i -> (i != group1)).findAny().isEmpty());
	}

	private static List<Long> groupCounts(Map<Point3, Integer> groups) {
		return new ArrayList<>(IntStream.range(0, groups.size()).mapToLong(i -> groups.values().stream().filter(v -> v == i).count()).mapToObj(l -> l).toList());
	}

	private static List<Connection> allCombinations(List<Point3> points) {
		final List<Connection> result = new ArrayList<>();
		for (int i = 0; i < points.size(); i++) {
			for (int j = i + 1; j < points.size(); j++) {
				result.add(new Connection(points.get(i), points.get(j)));
			}
		}
		return result;
	}

	private static record Connection(Point3 p1, Point3 p2, double distance) {
		private Connection(Point3 p1, Point3 p2) {
			this(p1, p2, euclideanDistance(p1, p2));
		}
	}

	public static double euclideanDistance(Point3 p1, Point3 p2) {
		return Math.sqrt(Math.pow(p1.x() - p2.x(), 2) + Math.pow(p1.y() - p2.y(), 2) + Math.pow(p1.z() - p2.z(), 2));
	}
}

// 153328 correct
// 1800654614 too low
// 6095621910
