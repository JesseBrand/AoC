package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.buildCharGrid;
import static nl.jessebrand.aoc.Utils.findGridPoint;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;

public class D07 {

	public static void main(final String[] args) throws IOException {
		solve("2025/d07ex");
		solve("2025/d07");
	}

	private static void solve(final String file) throws IOException {
		final List<String> lines = readFile(file);
		final Grid<Character> grid = buildCharGrid(lines);
		out(grid);
		
		int resultA = fillBeam(grid);
		long resultB = countBeamOptions(grid);

		out("Part 1: %d", resultA);
		out("Part 2: %d", resultB);
	}

	private static int fillBeam(Grid<Character> grid) {
		final Point start = findGridPoint(grid, 'S');
		final List<Point> points = new ArrayList<>();
		points.add(start.add(0, 1));

		int result = 0;
		while (!points.isEmpty()) {
			final Point p = points.get(0);
			points.remove(p);
			Point r = runA(grid, p);
			if (r != null) {
				points.add(r.add(1, 1));
				points.add(r.add(-1, 1));
				result++;
			}
		}
		out(grid);
		return result;
	}

	private static long countBeamOptions(Grid<Character> grid) {
		final Point start = findGridPoint(grid, 'S');
		return calcFor(grid, start.add(0, 1));
	}

	private static final Map<Point, Long> CACHE = new HashMap<>();

	private static long calcFor(Grid<Character> grid, Point p) {
		if (CACHE.containsKey(p)) {
			return CACHE.get(p);
		}
		Point r = runB(grid, p);
		if (r == null) {
			return 1;
		}
		long result = calcFor(grid, r.add(1, 1)) + calcFor(grid, r.add(-1, 1));
		CACHE.put(p, result);
		return result;
	}

	/**
	 * Input: position where to start with beam.
	 * Returns position of splitter.
	 */
	private static Point runA(Grid<Character> grid, Point p) {
		int x = p.x();
		int y = p.y();
		while (y < grid.getHeight()) {
			if (grid.get(x, y) == '|') {
				return null;
			}
			if (grid.get(x, y) == '^') {
				return new Point(x, y);
			}
			grid.set(x, y, '|');
			y++;
		}
		return null;
	}

	/**
	 * Input: position where to start with beam.
	 * Returns position of splitter.
	 */
	private static Point runB(Grid<Character> grid, Point p) {
		int x = p.x();
		int y = p.y();
		while (y < grid.getHeight()) {
			if (grid.get(x, y) == '^') {
				return new Point(x, y);
			}
			y++;
		}
		return null;
	}
}

