package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.buildCharGrid;
import static nl.jessebrand.aoc.Utils.findGridPoints;
import static nl.jessebrand.aoc.Utils.findUniqueGridEntries;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
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

		final Set<Character> uniqueChars = findUniqueGridEntries(grid);
		uniqueChars.remove('.');
		
		System.out.println("1: " + uniqueChars.stream().map(c -> findAntinodes(grid, c)).flatMap(Set::stream).collect(Collectors.toSet()).size());
		System.out.println("2: " + uniqueChars.stream().map(c -> findAllAntinodes(grid, c)).flatMap(Set::stream).collect(Collectors.toSet()).size());
	}

	private static Set<Point> findAntinodes(Grid<Character> grid, final char c) {
		final List<Point> gridPoints = findGridPoints(grid, c);
		final Set<Point> result = new HashSet<>();
		for (int i0 = 0; i0 < gridPoints.size(); i0++) {
			final Point s0 = gridPoints.get(i0);
			for (int i1 = i0 + 1; i1 < gridPoints.size(); i1++) {
				final Point s1 = gridPoints.get(i1);
				final int xInc = s0.x() - s1.x();
				final int yInc = s0.y() - s1.y();
				final Point t0 = new Point(s0.x() + xInc, s0.y() + yInc);
				if (grid.contains(t0)) {
					result.add(t0);
				}
				final Point t1 = new Point(s1.x() - xInc, s1.y() - yInc);
				if (grid.contains(t1)) {
					result.add(t1);
				}
//				System.out.println("Calc for " + c + " " + s0 + " " + s1 + ": " + t0 + " and " + t1);
			}
		}
		return result;
	}

	private static Set<Point> findAllAntinodes(final Grid<Character> grid, final char c) {
		final List<Point> gridPoints = findGridPoints(grid, c);
		final Set<Point> result = new HashSet<>();
		for (int i0 = 0; i0 < gridPoints.size(); i0++) {
			final Point s0 = gridPoints.get(i0);
			for (int i1 = i0 + 1; i1 < gridPoints.size(); i1++) {
				final Point s1 = gridPoints.get(i1);
				final int xInc = s0.x() - s1.x();
				final int yInc = s0.y() - s1.y();
				Point t = s0;
				while (grid.contains(t)) {
					result.add(t);
					t = new Point(t.x() + xInc, t.y() + yInc);
				}
				t = s1;
				while (grid.contains(t)) {
					result.add(t);
					t = new Point(t.x() - xInc, t.y() - yInc);
				}
			}
		}
		return result;
	}

}
