package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.buildCharGrid;
import static nl.jessebrand.aoc.Utils.findGridPoint;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
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
//		out(grid);
		
		out("Part 1: %d", fillBeam(grid));
//		out(grid);
		out("Part 2: %d", countOptions(grid));
		out();
	}

	private static int fillBeam(final Grid<Character> grid) {
		final Point start = findGridPoint(grid, 'S');
		return fillBeamFor(grid, start.add(0, 1));
	}

	private static int fillBeamFor(final Grid<Character> grid, final Point p) {
		final Point r = runA(grid, p);
		if (r == null) {
			return 0;
		}
		return 1 + fillBeamFor(grid, r.add(1, 1)) + fillBeamFor(grid, r.add(-1, 1));
	}

	/**
	 * Input: position where to start with beam.
	 * Returns position of splitter.
	 */
	private static Point runA(final Grid<Character> grid, final Point start) {
		Point p = start;
		while (p.y() < grid.getHeight()) {
			if (grid.get(p) == '|') {
				return null;
			}
			if (grid.get(p) == '^') {
				return p;
			}
			grid.set(p, '|');
			p = p.add(0, 1);
		}
		return null;
	}

	private static long countOptions(final Grid<Character> grid) {
		final Point start = findGridPoint(grid, 'S');
		return countOptionsFor(grid, start.add(0, 1));
	}

	private static final Map<Point, Long> CACHE = new HashMap<>();

	private static long countOptionsFor(final Grid<Character> grid, final Point p) {
		if (CACHE.containsKey(p)) {
			return CACHE.get(p);
		}
		final Point r = runB(grid, p);
		if (r == null) {
			return 1;
		}
		final long result = countOptionsFor(grid, r.add(1, 1)) + countOptionsFor(grid, r.add(-1, 1));
		CACHE.put(p, result);
		return result;
	}

	/**
	 * Input: position where to start with beam.
	 * Returns position of splitter.
	 */
	private static Point runB(final Grid<Character> grid, final Point start) {
		Point p = start;
		while (p.y() < grid.getHeight()) {
			if (grid.get(p) == '^') {
				return p;
			}
			p = p.add(0, 1);
		}
		return null;
	}
}

// 1609
// 12472142047197
