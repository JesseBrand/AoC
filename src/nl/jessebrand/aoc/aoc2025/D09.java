package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.combinations;
import static nl.jessebrand.aoc.Utils.*;
import static nl.jessebrand.aoc.Utils.parsePoints;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;

public class D09 {


	public static void main(final String[] args) throws IOException {
		solve("2025/d09ex");
		solve("2025/d09");
	}

	private static void solve(final String file) throws IOException {
		final List<String> lines = readFile(file);
		List<Point> points = parsePoints(lines, ",");
		out(points);

		final List<Pair> pairs = orderedPairs(points);
		out("Part 1: %d", size(pairs.get(0)));

		List<Integer> xs = points.stream().mapToInt(p -> p.x()).distinct().sorted().boxed().toList();
		List<Integer> ys = points.stream().mapToInt(p -> p.y()).distinct().sorted().boxed().toList();
		out("xs: %s", xs);
		out("ys: %s", ys);
		final Grid<Character> grid = new Grid<>(xs.size(), ys.size(), '.');
		for (int i = 0; i < points.size() - 1; i++) {
			drawPath(grid, points.get(i), points.get(i + 1), 'X', xs, ys);
		}
		drawPath(grid, points.get(points.size() - 1), points.get(0), 'X', xs, ys);
//		for (final Point point : points) {
//			grid.set(mapToGrid(point, xs, ys), '#');
//		}
		out(grid);
		fill(grid);
		out(grid);
		
		final Pair r2 = filterPairsFirst(grid, pairs, xs, ys);
		out("%s (%s-%s)", r2, mapToGrid(r2.p1(), xs, ys), mapToGrid(r2.p2(), xs, ys));
		out("Part 2: %d", size(r2));
		out();
	}

	private static void fill(Grid<Character> grid) {
		for (int y = 0; y < grid.getHeight(); y++) {
			for (int x = 0; x < grid.getWidth(); x++) {
				if (grid.get(x, y) == 'X') {
					continue;
				}
				if (!(yFree(grid, x, 0, y) || yFree(grid, x, y, grid.getHeight() - 1) || xFree(grid, y, 0, x) || xFree(grid, y, x, grid.getWidth() - 1))) {
					grid.set(x, y, 'X');
				}
			}
		}
	}

	private static boolean yFree(final Grid<Character> grid, int x, int yMin, int yMax) {
		return IntStream.range(yMin, yMax + 1).filter(y -> grid.get(x, y) == 'X').findAny().isEmpty();
	}

	private static boolean xFree(final Grid<Character> grid, int y, int xMin, int xMax) {
		return IntStream.range(xMin, xMax + 1).filter(x -> grid.get(x, y) == 'X').findAny().isEmpty();
	}

	private static Pair filterPairsFirst(final Grid<Character> grid, final List<Pair> pairs, final List<Integer> xs, final List<Integer> ys) {
		return pairs.stream().filter(pair -> {
			final Point p1 = mapToGrid(pair.p1(), xs, ys);
			final Point p2 = mapToGrid(pair.p2(), xs, ys);
			final int minX = Math.min(p1.x(), p2.x());
			final int maxX = Math.max(p1.x(), p2.x());
			final int minY = Math.min(p1.y(), p2.y());
			final int maxY = Math.max(p1.y(), p2.y());
			for (int x = minX; x <= maxX; x++) {
				for (int y = minY; y <= maxY; y++) {
//					out("(%d,%d): %c", x, y, grid.get(x, y));
					if (grid.get(x, y) == '.') {
//						out("false for %s", pair);
						return false;
					}
				}
			}
//			out("true for %s (%s-%s)", pair, p1, p2);
			return true;
		}).findFirst().get();
	}

	private static void drawPath(Grid<Character> grid, Point p1, Point p2, final char c, final List<Integer> xs, final List<Integer> ys) {
		final Point gp1 = mapToGrid(p1, xs, ys);
		final Point gp2 = mapToGrid(p2, xs, ys);
		for (Point p : fromTo(gp1, gp2)) {
			grid.set(p, c);
		}
	}

	private static List<Point> fromTo(Point gp1, Point gp2) {
		if (gp1.x() == gp2.x()) {
			int yMin = Math.min(gp1.y(), gp2.y());
			int yMax = Math.max(gp1.y(), gp2.y());
			return IntStream.range(yMin, yMax + 1).mapToObj(y -> new Point(gp1.x(), y)).toList();
		}
		if (gp1.y() == gp2.y()) {
			int xMin = Math.min(gp1.x(), gp2.x());
			int xMax = Math.max(gp1.x(), gp2.x());
			return IntStream.range(xMin, xMax + 1).mapToObj(x -> new Point(x, gp1.y())).toList();
		}
		throw new IllegalArgumentException("x and y not matching");
	}

	private static Point mapToGrid(Point point, List<Integer> xs, List<Integer> ys) {
		return new Point(xs.indexOf(point.x()), ys.indexOf(point.y()));
	}

	private static List<Pair> orderedPairs(final List<Point> points) {
		final List<Pair> pairs = new ArrayList<>(combinations(points).stream().map(t -> new Pair(t.l1(), t.l2(), size(t.l1(), t.l2()))).toList());
		pairs.sort(COMPARATOR);
		Collections.reverse(pairs);
		return pairs;
	}

	private record Pair(Point p1, Point p2, long size) {
		@Override
		public String toString() {
			return String.format("%s-%s: %d", p1, p2, size);
		}
	}

	private static final Comparator<Pair> COMPARATOR = new Comparator<>() {

		@Override
		public int compare(final Pair o1, final Pair o2) {
			return Long.compare(o1.size(), o2.size());
		}
	};

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
