package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.*;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.jessebrand.aoc.HasLocation;
import nl.jessebrand.aoc.Point;

public class D24 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2022/d24");
		final int width = lines.get(0).length() - 2;
		final int height = lines.size() - 2;
		final Point start = new Point(0, -1);
		final Point end = new Point(width - 1, height);
		final List<Blizzard> blizzards = parseBlizzards(lines);
		out("Blizzards(%d): %s", blizzards.size(), blizzards);
		Set<Point> startPoints = new HashSet<>();
		startPoints.add(start);
		int turn = 0;
		boolean found = false;
		outputDebug(0, 5, 0, 5, blizzards, startPoints);
		while (!startPoints.isEmpty() && !found) {
//			out("Turn %d: Evaluating %d moves", turn, startPoints.size());
			out("Turn %d: Evaluating %d moves: %s", turn, startPoints.size(), startPoints);
			Set<Point> nextPoints = new HashSet<>();

			advanceBlizzards(blizzards, width, height);
			
//			out("Blizzards at %s", blizzards);
			
			for (Point startPoint : startPoints) {
//				if (noBlizzardsAt(startPoint, blizzards)) {
//					nextPoints.add(startPoint);
//				}
				final List<Point> adjacent = buildAdjacentPoints(startPoint, width, height, end);
				for (Point p : adjacent) {
					if (p.equals(end)) {
						found = true;
						break;
					}
					if (noBlizzardsAt(p, blizzards)) {
//						out("%s added %s", startPoint, p);
						nextPoints.add(p);
					} else {
//						out("Blizzard at %s", p);
					}
				}
			}
//			out("After %d turn%s result: %s", turn + 1, turn == 0 ? "s" : "", nextPoints);
//			System.out.println();
			
			startPoints = nextPoints;
			turn++;
		}
		if (found) {
			out("Found route to target after %d turns", turn);
		}
	}

	private static void outputDebug(int xFrom, int xTo, int yFrom, int yTo, final List<Blizzard> blizzards, final Set<Point> playerPoints) {
		for (int y = yFrom; y < yTo; y++) {
			for (int x = xFrom; x < xTo; x++) {
				if (findAnyAt(playerPoints, x, y) != null) {
					System.out.print("P");
				} else {
					Blizzard blizzard = findAnyAt(blizzards, x, y);
					if (blizzard != null) {
						System.out.print(blizzard.direction.c);
					} else { 
						System.out.print(".");
					}
				}
			}
			System.out.println();
		}
	}

	private static List<Blizzard> parseBlizzards(List<String> lines) {
		final List<Blizzard> result = new ArrayList<>();
		for (int y = 0; y < lines.size() - 2; y++) {
			String line = lines.get(y + 1);
			for (int x = 0; x < line.length() - 2; x++) {
				char c = line.charAt(x + 1);
				if (c != '.') {
					result.add(new Blizzard(Direction.parseDirection(c), x, y));
				}
//				System.out.print(c);
			}
//			System.out.println();
		}
		return result;
	}

	
	
	private static void advanceBlizzards(final List<Blizzard> blizzards, final int width, final int height) {
		for (final Blizzard blizzard : blizzards) {
			switch (blizzard.direction) {
			case SOUTH:
				blizzard.updatePosition(blizzard.x, (blizzard.y + 1) % height);
				break;
			case NORTH:
				blizzard.updatePosition(blizzard.x, (blizzard.y - 1 + height) % height);
				break;
			case EAST:
				blizzard.updatePosition((blizzard.x + 1) % width, blizzard.y);
				break;
			case WEST:
				blizzard.updatePosition((blizzard.x - 1 + width) % width, blizzard.y);
				break;
			}
		}
	}

	/**
	 * @return list of [current, left, right, up, down, possible end]
	 */
	private static List<Point> buildAdjacentPoints(final Point point, final int width, final int height, Point end) {
		final List<Point> result = new ArrayList<>();
		result.add(point); // wait
		if (point.x() > 0) {
			result.add(new Point(point.x() - 1, point.y())); // left
		}
		if (point.y() > 0) {
			result.add(new Point(point.x(), point.y() - 1)); // up
		}
		if (point.x() < width - 1 && point.y() >= 0) {
			result.add(new Point(point.x() + 1, point.y())); // right
		}
		if (point.y() < height - 1) {
			result.add(new Point(point.x(), point.y() + 1)); // down
		}
		if (point.x() == end.x() && point.y() + 1 == end.y()) {
			result.add(end); // end
		}
		return result;
	}


	private static boolean noBlizzardsAt(final Point point, final List<Blizzard> blizzards) {
		return findAnyAt(blizzards, point) == null;
	}

	static enum Direction {
		NORTH('^'),
		EAST('>'),
		SOUTH('v'),
		WEST('<');

		private final char c;

		Direction(final char c) {
			this.c = c;
		}

		static Direction parseDirection(final char c) {
			for (final Direction dir : values()) {
				if (dir.c == c) {
					return dir;
				}
			}
			throw new IllegalArgumentException();
		}
	}
	
	static class Blizzard implements HasLocation {
		
		private final Direction direction;
		private int x;
		private int y;

		Blizzard(final Direction direction, final int x, final int y) {
			this.direction = direction;
			this.x = x;
			this.y = y;
		}
		
		public void updatePosition(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public int x() {
			return x;
		}
		
		@Override
		public int y() {
			return y;
		}

		@Override
		public String toString() {
			return String.format("B%c[%d,%d]", direction.c, x, y);
		}
	}
	
}
