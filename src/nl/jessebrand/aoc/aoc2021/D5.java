package nl.jessebrand.aoc.aoc2021;

import static nl.jessebrand.aoc.Utils.parsePoint;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;
import nl.jessebrand.aoc.Tuple;

public class D5 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2021/d5");
		final List<Tuple<Point>> vents = parseLines(lines);
		
		Grid<Integer> grid = buildGrid(vents);
		System.out.println(grid);
		
		System.out.println("2+: " + count(grid, 2));
	}

	private static int count(final Grid<Integer> grid, final int min) {
		int count = 0;
		for (int y = 0; y < grid.getHeight(); y++) {
			for (int x = 0; x < grid.getWidth(); x++) {
				if (grid.get(x, y) >= min) {
					count++;
				}
			}
		}
		return count;
	}

	private static Grid<Integer> buildGrid(final List<Tuple<Point>> vents) {
		int width = 0, height = 0;
		for (final Tuple<Point> vent : vents) {
			width = Math.max(width, Math.max(vent.l1().x(),  vent.l2().x()));
			height = Math.max(height, Math.max(vent.l1().y(),  vent.l2().y()));
		}
		
		final Grid<Integer> grid = new Grid<>(width + 1, height + 1, "");
		for (int y = 0; y < grid.getHeight(); y++) {
			for (int x = 0; x < grid.getWidth(); x++) {
				grid.set(x, y, 0);
			}
		}
		
		for (final Tuple<Point> vent : vents) {
			if (vent.l1().y() == vent.l2().y()) {
				// horizontal line
				int y = vent.l1().y();
				int minX = Math.min(vent.l1().x(), vent.l2().x());
				int maxX = Math.max(vent.l1().x(), vent.l2().x());
				System.out.println(String.format("Draw line from %d,%d to %d,%d (horizontal)", minX, y, maxX, y));
				for (int x = minX; x <= maxX; x++) {
					grid.set(x, y, grid.get(x, y) + 1);
				}
			} else if (vent.l1().x() == vent.l2().x()) {
				// vertical line
				int x = vent.l1().x();
				int minY = Math.min(vent.l1().y(), vent.l2().y());
				int maxY = Math.max(vent.l1().y(), vent.l2().y());
				System.out.println(String.format("Draw line from %d,%d to %d,%d (vertical)", x, minY, x, maxY));
				for (int y = minY; y <= maxY; y++) {
					grid.set(x, y, grid.get(x, y) + 1);
				}
			} else {
				// diagonal
				int x1 = vent.l1().x();
				int x2 = vent.l2().x();
				int yFrom;
				int yTo;
				int xFrom, xTo, yInc;
				if (x1 < x2) {
					xFrom = x1;
					xTo = x2;
					yFrom = vent.l1().y();
					yTo = vent.l2().y();
				} else {
					xFrom = x2;
					xTo = x1;
					yFrom = vent.l2().y();
					yTo = vent.l1().y();
				}
				if (yFrom < yTo) {
					yInc = 1;
				} else {
					yInc = -1;
				}
				System.out.println(String.format("%s: draw line from %d,%d to %d,%d (diagonal)", vent.toString(), xFrom, yFrom, xTo, yTo));
				for (int i = 0; xFrom + i <= xTo; i++) {
					int x = xFrom + i;
					int y = yFrom + (i * yInc);
					grid.set(x, y, grid.get(x, y) + 1);
				}
			}
		}
		
		return grid;
	}

	private static List<Tuple<Point>> parseLines(List<String> lines) {
		final List<Tuple<Point>> result = new ArrayList<>(lines.size());
		for (final String line : lines) {
			String[] split = line.split(" -> ");
			result.add(new Tuple<Point>(parsePoint(split[0]), parsePoint(split[1])));
		}
		return result;
	}
		
	
}

// 18607 = too low