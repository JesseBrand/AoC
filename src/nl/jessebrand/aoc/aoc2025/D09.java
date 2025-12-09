package nl.jessebrand.aoc.aoc2025;

import static java.util.stream.Collectors.toCollection;
import static nl.jessebrand.aoc.Utils.combinations;
import static nl.jessebrand.aoc.Utils.drawPath;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.parsePoints;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.Utils.simpleGridfill;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;

public class D09 {


	public static void main(final String[] args) throws IOException {
		solve("2025/d09ex");
		solve("2025/d09");
	}

	private static void solve(final String file) throws IOException {
		final List<String> lines = readFile(file);
		final List<Point> points = parsePoints(lines, ",");
		out(points);

		final List<Pair> pairs = orderedPairs(points);
		out("Part 1: %d", size(pairs.get(0)));

		final List<Integer> xs = points.stream().mapToInt(p -> p.x()).distinct().sorted().boxed().toList();
		final List<Integer> ys = points.stream().mapToInt(p -> p.y()).distinct().sorted().boxed().toList();
//		out("xs: %s", xs);
//		out("ys: %s", ys);
		final Grid<Boolean> grid = new Grid<>(xs.size(), ys.size(), false);
		for (int i = 0; i < points.size() - 1; i++) {
			drawMappedPath(grid, points.get(i), points.get(i + 1), xs, ys);
		}
		drawMappedPath(grid, points.get(points.size() - 1), points.get(0), xs, ys);
//		out(grid);
		simpleGridfill(grid);
		out(grid);
		
		final Pair pair = filterPairsFirst(grid, pairs, xs, ys);
		out("%s (%s-%s)", pair, mapToGrid(pair.p1(), xs, ys), mapToGrid(pair.p2(), xs, ys));
		out("Part 2: %d", size(pair));
		out();
	}

	private static Pair filterPairsFirst(final Grid<Boolean> grid, final List<Pair> pairs, final List<Integer> xs, final List<Integer> ys) {
		return pairs.stream().filter(pair -> {
			final Point p1 = mapToGrid(pair.p1(), xs, ys);
			final Point p2 = mapToGrid(pair.p2(), xs, ys);
			final int minX = Math.min(p1.x(), p2.x());
			final int maxX = Math.max(p1.x(), p2.x());
			final int minY = Math.min(p1.y(), p2.y());
			final int maxY = Math.max(p1.y(), p2.y());
			for (int x = minX; x <= maxX; x++) {
				for (int y = minY; y <= maxY; y++) {
					if (!grid.get(x, y)) {
						return false;
					}
				}
			}
			return true;
		}).findFirst().get();
	}

	private static void drawMappedPath(final Grid<Boolean> grid, final Point p1, final Point p2, final List<Integer> xs, final List<Integer> ys) {
		final Point gp1 = mapToGrid(p1, xs, ys);
		final Point gp2 = mapToGrid(p2, xs, ys);
		drawPath(grid, gp1, gp2, true);
	}

	private static Point mapToGrid(final Point point, final List<Integer> xs, final List<Integer> ys) {
		return new Point(xs.indexOf(point.x()), ys.indexOf(point.y()));
	}

	private static List<Pair> orderedPairs(final List<Point> points) {
		final List<Pair> pairs = combinations(points).stream().map(t -> new Pair(t.l1(), t.l2(), size(t.l1(), t.l2()))).collect(toCollection(ArrayList::new));
		Collections.sort(pairs);
		Collections.reverse(pairs);
		return pairs;
	}

	private record Pair(Point p1, Point p2, long size) implements Comparable<Pair> {
		@Override
		public String toString() {
			return String.format("%s-%s: %d", p1, p2, size);
		}

		@Override
		public int compareTo(final Pair o) {
			return Long.compare(size(), o.size());
		}
	}

	private static long size(final Pair pair) {
		return size(pair.p1(), pair.p2());
	}

	private static long size(final Point p1, final Point p2) {
		return (long) (Math.abs(p1.x() - p2.x()) + 1) * (Math.abs(p1.y() - p2.y()) + 1);
	}
}

// 2147362140 wrong
// 4763040296
// 1396494456
