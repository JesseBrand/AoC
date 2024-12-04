package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

		final long result1 = findChars(lines, 'X').stream().mapToLong(p -> find(lines, p, "XMAS", 0)).sum();
		System.out.println(result1);

		final long result2 = findChars(lines, 'A').stream().filter(p -> findCrossed(lines, p, "MAS", 1)).count();
		System.out.println(result2);
	}

	private static long find(final List<String> lines, final Point p, final String str, final int initPos) {
		return Arrays.stream(Direction.values()).filter(dir -> matches(lines, p, dir, str, initPos)).count();
	}

	private static boolean findCrossed(final List<String> lines, final Point p, final String str, final int initPos) {
		return (matches(lines, p, Direction.NW, str, initPos) || matches(lines, p, Direction.SE, str, initPos))
				&& (matches(lines, p, Direction.NE, str, initPos) || matches(lines, p, Direction.SW, str, initPos));
	}

	private static boolean matches(final List<String> lines, final Point p, final Direction dir, final String str, final int initPos) {
		for (int i = 0; i < str.length(); i++) {
			if (i == initPos) {
				continue;
			}
			if (!matches(lines, p, (i - initPos) * dir.xInc(), (i - initPos) * dir.yInc(), str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	private static boolean matches(final List<String> lines, final Point p, final int xInc, final int yInc, final char c) {
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

	private static List<Point> findChars(final List<String> lines, final char c) {
		final List<Point> points = new ArrayList<>();
		for (int y = 0; y < lines.size(); y++) {
			final String line = lines.get(y);
			for (int x = 0; x < line.length(); x++) {
				if (line.charAt(x) == c) {
					points.add(new Point(x, y));
				}
			}
		}
		return points;
	}
}

// 2642
// 1974
