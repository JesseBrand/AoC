package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.parsePoints;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.Utils.solveAStar;
import static nl.jessebrand.aoc.Utils.visualize;

import java.io.IOException;
import java.util.List;

import nl.jessebrand.aoc.AStarRenderer;
import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Path;
import nl.jessebrand.aoc.Point;

public class D18 {

	private static final int SCALE = 10;
	
	public static void main(String[] args) throws IOException {
		solve("2024/d18ex", 7, 12);
		solve("2024/d18", 71, 1024);
	}
	
	private static void solve(final String file, final int gridSize, final int limit) throws IOException {
		final List<String> lines = readFile(file);
		final Grid<Boolean> grid = new Grid<>(gridSize, gridSize, false);
		final List<Point> points = parsePoints(lines, ",");
		solve1(grid, points, limit, file);
		solve2(grid, points, file);
	}

	private static void solve1(final Grid<Boolean> grid, final List<Point> points, final int limit, final String title) {
		final Point start = new Point(0, 0);
		final Point end = new Point(grid.getWidth() - 1, grid.getHeight() - 1);
		points.stream().limit(limit).forEach(p -> grid.set(p, true));
//		out(grid);
		final Path path = solveAStar(grid, start, end);
		out("1: %d", path.length() - 1);
		final AStarRenderer renderer = new AStarRenderer(grid, path, SCALE);
		visualize(title, grid.getWidth() * SCALE, grid.getHeight() * SCALE, renderer::render);
	}

	private static void solve2(final Grid<Boolean> grid, final List<Point> points, final String title) {
		final Point start = new Point(0, 0);
		final Point end = new Point(grid.getWidth() - 1, grid.getHeight() - 1);
		for (final Point p : points) {
			grid.set(p, true);
			final Path path = solveAStar(grid, start, end);
			if (path == null) {
				out("2: %s", p);
				return;
			}
		}
		throw new IllegalStateException();
	}
}
