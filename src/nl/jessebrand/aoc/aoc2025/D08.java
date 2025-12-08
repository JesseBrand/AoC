package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.euclideanDistance;
import static nl.jessebrand.aoc.Utils.*;
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
import nl.jessebrand.aoc.Tuple;

public class D08 {

	private static final Comparator<Connection> COMPARATOR = new Comparator<Connection>() {
		@Override
		public int compare(final Connection c1, final Connection c2) {
			return Double.compare(c1.distance(), c2.distance());
		}
	};

	public static void main(final String[] args) throws IOException {
		solve("2025/d08ex", 10);
		solve("2025/d08", 1000);
	}

	private static void solve(final String file, final int max) throws IOException {
		final List<String> lines = readFile(file);
		final List<Point3> points = parsePoint3s(lines);
		final List<Connection> conns = new ArrayList<>(combinations(points).stream().map(t -> new Connection(t)).toList());
		conns.sort(COMPARATOR);
		
		final Map<Point3, Integer> groups = new HashMap<>();
		for (int i = 0; i < points.size(); i++) {
			groups.put(points.get(i), i);
		}

		for (int count = 0; ; count++) {
			final Connection conn = conns.get(count);
			final Integer g1 = groups.get(conn.p1());
			final Integer g2 = groups.get(conn.p2());
			if (g1 != g2) {
				groups.entrySet().stream().filter(e -> e.getValue() == g2).forEach(e -> e.setValue(g1));
//				out("M: %s-%s: Merged group %d into %d, %s", conn.p1(), conn.p2(), g2, g1, groupCounts(groups));
			}
			if (count + 1 == max) {
				final List<Long> groupCounts = groupCounts(groups);
				Collections.sort(groupCounts);
				Collections.reverse(groupCounts);
				out("Part 1: %d", groupCounts.get(0) * groupCounts.get(1) * groupCounts.get(2));
			}
			if (isUniform(groups.values())) {
				out("Part 2: %d", (long) conn.p1().x() * conn.p2().x());
				break;
			}
		}
		out();
	}

	private static List<Long> groupCounts(final Map<Point3, Integer> groups) {
		return new ArrayList<>(IntStream.range(0, groups.size()).mapToLong(
				i -> groups.values().stream().filter(v -> v == i).count()
		).mapToObj(l -> l).toList());
	}

	private static record Connection(Point3 p1, Point3 p2, double distance) {
		private Connection(final Tuple<Point3> t) {
			this(t.l1(), t.l2());
		}
		private Connection(final Point3 p1, final Point3 p2) {
			this(p1, p2, euclideanDistance(p1, p2));
		}
	}
}

// 153328 correct
// 1800654614 too low
// 6095621910
