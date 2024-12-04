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
		private Direction rotDir;

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

		public Direction rotDir() {
			return rotDir;
		}
	};

	static {
		Direction.NW.rotDir = Direction.NE;
		Direction.N.rotDir  = Direction.E;
		Direction.NE.rotDir = Direction.SE;
		Direction.E.rotDir  = Direction.S;
		Direction.SE.rotDir = Direction.SW;
		Direction.S.rotDir  = Direction.W;
		Direction.SW.rotDir = Direction.NW;
		Direction.W.rotDir  = Direction.N;
	}

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d04");

		final List<Point> as = findAs(lines);
//		System.out.println(as);
		int matches1 = 0;
		int matches2 = 0;
		for (final Point a : as) {
			matches1 += findXMas(lines, a);
			matches2 += findMas(lines, a);
		}
		System.out.println(matches1);
		System.out.println(matches2);
	}

	private static int findXMas(final List<String> lines, final Point p) {
		int total = 0;
		for (final Direction dir : Direction.values()) {
			if (matchesXMAS(lines, p, dir.xInc(), dir.yInc())) {
				total++;
			}
		}
//		if (total > 0) {
//			System.out.println(total + " matches at " + p);
//		}
		return total;
	}

	private static int findMas(final List<String> lines, final Point p) {
		int total = 0;
		for (final Direction dir : Direction.values()) {
			if (matchesMAS(lines, p, dir.xInc(), dir.yInc())
					&& matchesMAS(lines, p, dir.rotDir().xInc(), dir.rotDir().yInc())) {
				total++;
			}
		}
		if (total > 0) {
			System.out.println(total + " matches at " + p);
		}
		return total;
	}

	private static boolean matchesXMAS(List<String> lines, Point p, int xInc, int yInc) {
		return matches(lines, p, -2 * xInc, -2 * yInc, 'X')
				&& matchesMAS(lines, p, xInc, yInc);
	}

	private static boolean matchesMAS(List<String> lines, Point p, int xInc, int yInc) {
		return matches(lines, p, -xInc, -yInc, 'M')
				&& matches(lines, p, xInc, yInc, 'S');
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

// 1025 too low
// 2001 too high