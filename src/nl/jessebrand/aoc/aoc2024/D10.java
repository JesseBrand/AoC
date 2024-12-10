package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.buildIntGrid;
import static nl.jessebrand.aoc.Utils.findGridPoints;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;

public class D10 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d10");
		Grid<Integer> grid = buildIntGrid(lines);
		System.out.println(grid);
		
		System.out.println("1: " + findGridPoints(grid, 0).stream().mapToInt(p -> findUniqueRoutesByTrailhead(p, grid)).sum());
		System.out.println("2: " + findGridPoints(grid, 0).stream().mapToInt(p -> findRoutesByTrailhead(p, grid)).sum());
		
	}

	private static int findUniqueRoutesByTrailhead(Point p, Grid<Integer> grid) {
		return new HashSet<>(findRoutes(p, grid, 1, 9)).size();
	}

	private static int findRoutesByTrailhead(Point p, Grid<Integer> grid) {
		return findRoutes(p, grid, 1, 9).size();
	}

	/**
	 * returns set of points reachable from p with level i
	 */
	private static List<Point> findRoutes(Point p, Grid<Integer> grid, final int i, final int max) {
		final List<Point> nextPoints = getNeighbours(p).stream().filter(grid::contains).filter(p1 -> grid.get(p1) == i).toList();
		if (i == max) {
			return nextPoints;
		}
		return nextPoints.stream().map(p1 -> findRoutes(p1, grid, i + 1, max)).flatMap(Collection::stream).toList();
	}

	private static List<Point> getNeighbours(final Point p) {
		return Arrays.asList(
				new Point(p.x(), p.y() - 1),
				new Point(p.x() + 1, p.y()),
				new Point(p.x(), p.y() + 1),
				new Point(p.x() - 1, p.y()));
	}
	
	
		
}
