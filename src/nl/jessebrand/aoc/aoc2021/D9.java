package nl.jessebrand.aoc.aoc2021;

import static nl.jessebrand.aoc.Utils.buildIntGrid;
import static nl.jessebrand.aoc.Utils.countTotal;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.Utils.toGridValues;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;

public class D9 {
	
	public static void main(String...args) throws IOException {
		final List<String> lines = readFile("2021/d9");
		final Grid<Integer> grid = buildIntGrid(lines);
		System.out.println(grid);
		List<Point> lowPoints = findLowPoints(grid);
		List<Integer> lowValues = toGridValues(grid, lowPoints);
		int result1 = countTotal(lowValues) + lowPoints.size();
		System.out.println("Result1 = " + result1);
		List<Integer> basins = findBasins(grid, lowPoints);
		Collections.sort(basins);
		System.out.println("Basins: " + basins);
		System.out.println("Result2 = " + (basins.get(basins.size() - 3) * basins.get(basins.size() - 2) * basins.get(basins.size() - 1)));
	}

	private static List<Integer> findBasins(final Grid<Integer> grid, final List<Point> lowPoints) {
		List<Integer> result = new ArrayList<>(lowPoints.size());
		for (final Point lowPoint : lowPoints) {
			int total = 0;
			List<Point> toAnalyze = Arrays.asList(lowPoint);
			List<Point> analyzed = new ArrayList<>(toAnalyze);
			while (!toAnalyze.isEmpty()) {
				List<Point> nextToAnalyze = new ArrayList<>();
				for (final Point point : toAnalyze) {
					System.out.println("Analyzing point " + point);
					total++;
					if (point.x() != 0 && grid.get(point.x() - 1, point.y()) != 9) {
						Point newPoint = new Point(point.x() - 1, point.y());
						if (!analyzed.contains(newPoint)) {
							nextToAnalyze.add(newPoint);
							analyzed.add(newPoint);
						}
					}
					if (point.x() != grid.getWidth() - 1 && grid.get(point.x() + 1, point.y()) != 9) {
						Point newPoint = new Point(point.x() + 1, point.y());
						if (!analyzed.contains(newPoint)) {
							nextToAnalyze.add(newPoint);
							analyzed.add(newPoint);
						}
					}

					if (point.y() != 0 && grid.get(point.x(), point.y() - 1) != 9) {
						Point newPoint = new Point(point.x(), point.y() - 1);
						if (!analyzed.contains(newPoint)) {
							nextToAnalyze.add(newPoint);
							analyzed.add(newPoint);
						}
					}
					if (point.y() != grid.getHeight() - 1 && grid.get(point.x(), point.y() + 1) != 9) {
						Point newPoint = new Point(point.x(), point.y() + 1);
						if (!analyzed.contains(newPoint)) {
							nextToAnalyze.add(newPoint);
							analyzed.add(newPoint);
						}
					}
				}
				toAnalyze = nextToAnalyze;
			}
			result.add(total);
		}
		return result;
	}

	private static List<Point> findLowPoints(Grid<Integer> grid) {
		final List<Point> result = new ArrayList<>();
		for (int y = 0; y < grid.getHeight(); y++) {
			for (int x = 0; x < grid.getWidth(); x++) {
				final int value = grid.get(x, y);
				if ((x == 0 || value < grid.get(x - 1, y))
						&& (y == 0 || value < grid.get(x, y - 1))
						&& (x == grid.getWidth() - 1 || value < grid.get(x + 1, y))
						&& (y == grid.getHeight() - 1 || value < grid.get(x, y + 1))) {
					result.add(new Point(x, y));
				}
			}
		}
		return result;
	}
}
