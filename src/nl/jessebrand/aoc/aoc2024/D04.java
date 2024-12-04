package nl.jessebrand.aoc.aoc2024;

import nl.jessebrand.aoc.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static nl.jessebrand.aoc.Utils.readFile;

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

		final List<Point> as = findAs(lines);
		int matches1 = 0;
		int matches2 = 0;
		for (final Point a : as) {
			matches1 += findXMAS(lines, a);
			matches2 += findCrossedMass(lines, a) ? 1 : 0;
		}
		System.out.println(matches1);
		System.out.println(matches2);
	}

	private static int findXMAS(final List<String> lines, final Point p) {
		int total = 0;
		for (final Direction dir : Direction.values()) {
			if (matchesXMAS(lines, p, dir)) {
				total++;
			}
		}
		return total;
	}

	private static boolean findCrossedMass(final List<String> lines, final Point p) {
		return (matchesMAS(lines, p, Direction.NW) || matchesMAS(lines, p, Direction.SE))
				&& (matchesMAS(lines, p, Direction.NE) || matchesMAS(lines, p, Direction.SW));
	}

	private static boolean matchesXMAS(List<String> lines, Point p, Direction dir) {
		return matches(lines, p, -2 * dir.xInc(), -2 * dir.yInc(), 'X')
				&& matchesMAS(lines, p, dir);
	}

	private static boolean matchesMAS(List<String> lines, Point p, final Direction dir) {
		return matches(lines, p, -dir.xInc(), -dir.yInc(), 'M')
				&& matches(lines, p, dir.xInc(), dir.yInc(), 'S');
	}

	private static boolean matches(List<String> lines, Point p, int xInc, int yInc, char c) {
		return get(lines, p.x() + xInc, p.y() + yInc) == c;
	}

	private static char get(final List<String> lines, final int x, final int y) {
		if (y < 0 || y >= lines.size()) {
			return 0;
		}
		final String line = lines.get(y);
		if (x < 0 || x >= line.length()) {
			return 0;
		}
		return line.charAt(x);
	}

	private static List<Point> findAs(final List<String> lines) {
		final List<Point> points = new ArrayList<>();
		for (int y = 0; y < lines.size(); y++) {
			final String line = lines.get(y);
			for (int x = 0; x < line.length(); x++) {
				if (line.charAt(x) == 'A') {
					points.add(new Point(x, y));
				}
			}
		}
		return points;
	}
}

// 1974
