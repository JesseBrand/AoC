package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.buildCharGrid;
import static nl.jessebrand.aoc.Utils.findGridPoints;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;

public class D04 {

	private static enum Direction {
		NW(-1, -1),
		N(0, -1),
		NE(1, -1),
		E(1, 0),
		SE(1, 1),
		S(0, 1),
		SW(-1, 1),
		W(-1, 0);

		private final int xInc;
		private final int yInc;

		Direction(int xInc, int yInc) {
			this.xInc = xInc;
			this.yInc = yInc;
		}

		public int xInc() {
			return xInc;
		}

		public int yInc() {
			return yInc;
		}
	};

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d04");
		final Grid<Character> grid = buildCharGrid(lines);

		final long result1 = findGridPoints(grid, 'X').stream().mapToLong(p -> find(grid, p, "XMAS", 0)).sum();
		System.out.println(result1);

		final long result2 = findGridPoints(grid, 'A').stream().filter(p -> findCrossed(grid, p, "MAS", 1)).count();
		System.out.println(result2);
	}

	private static long find(final Grid<Character> grid, final Point p, final String str, final int initPos) {
		return Arrays.stream(Direction.values()).filter(dir -> matches(grid, p, dir, str, initPos)).count();
	}

	private static boolean findCrossed(final Grid<Character> grid, final Point p, final String str, final int initPos) {
		return (matches(grid, p, Direction.NW, str, initPos) || matches(grid, p, Direction.SE, str, initPos))
				&& (matches(grid, p, Direction.NE, str, initPos) || matches(grid, p, Direction.SW, str, initPos));
	}

	private static boolean matches(final Grid<Character> grid, final Point p, final Direction dir, final String str, final int initPos) {
		for (int i = 0; i < str.length(); i++) {
			if (i == initPos) {
				continue;
			}
			if (!matches(grid, p, (i - initPos) * dir.xInc(), (i - initPos) * dir.yInc(), str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	private static boolean matches(final Grid<Character> grid, final Point p, final int xInc, final int yInc, final char c) {
		return grid.getOr(p.x() + xInc, p.y() + yInc, (char) 0) == c;
	}
}

// 2642
// 1974
