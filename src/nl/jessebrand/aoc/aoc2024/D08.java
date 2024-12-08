package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.buildCharGrid;
import static nl.jessebrand.aoc.Utils.findGridPoints;
import static nl.jessebrand.aoc.Utils.findUniqueGridEntries;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;

public class D08 {
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d08");
		final Grid<Character> grid = buildCharGrid(lines);
//		System.out.println(grid);
		
		System.out.println("1: " + search(grid, false));
		System.out.println("2: " + search(grid, true));
	}

	private static int search(final Grid<Character> grid, final boolean all) {
		final Set<Character> uniqueChars = findUniqueGridEntries(grid);
		uniqueChars.remove('.');
		return uniqueChars.stream().map(c -> findAntinodes(grid, c, all)).flatMap(Collection::stream).collect(Collectors.toSet()).size();
	}

	private static Collection<Point> findAntinodes(Grid<Character> grid, final char c, final boolean all) {
		final List<Point> gridPoints = findGridPoints(grid, c);
		final Collection<Point> result = new ArrayList<>();
		for (int i0 = 0; i0 < gridPoints.size(); i0++) {
			final Point s0 = gridPoints.get(i0);
			for (int i1 = i0 + 1; i1 < gridPoints.size(); i1++) {
				final Point s1 = gridPoints.get(i1);
				if (all) {
					result.addAll(findAllAntinodes(s0, s1, grid));
				} else {
					result.addAll(findAntinodes(s0, s1).stream().filter(grid::contains).toList());
				}
			}
		}
		return result;
	}

	private static Collection<Point> findAntinodes(final Point p0, final Point p1) {
		final int xInc = p0.x() - p1.x();
		final int yInc = p0.y() - p1.y();
		return List.of(
				new Point(p0.x() + xInc, p0.y() + yInc),
				new Point(p1.x() - xInc, p1.y() - yInc));
	}

	private static Collection<Point> findAllAntinodes(final Point p0, final Point p1, final Grid<?> grid) {
		final Collection<Point> result = new ArrayList<>();
		final int xInc = p0.x() - p1.x();
		final int yInc = p0.y() - p1.y();
		Point t = p0;
		while (grid.contains(t)) {
			result.add(t);
			t = new Point(t.x() + xInc, t.y() + yInc);
		}
		t = p1;
		while (grid.contains(t)) {
			result.add(t);
			t = new Point(t.x() - xInc, t.y() - yInc);
		}
		return result;
	}

}
