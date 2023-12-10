package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.aoc2023.D10.findMainLoop;
import static nl.jessebrand.aoc.aoc2023.D10.findStart;
import static nl.jessebrand.aoc.aoc2023.D10.parseGrid;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.jessebrand.aoc.Direction;
import nl.jessebrand.aoc.Point;
import nl.jessebrand.aoc.aoc2023.D10.Grid;
import nl.jessebrand.aoc.aoc2023.D10.Node;

public class D10b {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d10");
		final Grid grid = parseGrid(lines);
//		System.out.println(grid);

		final Point start = findStart(grid);
		grid.setPathLength(start.x(), start.y(), 0);
		System.out.println("Start: " + start);

		findMainLoop(grid, start);
		
		eraseNotPartOfLoop(grid);
		grid.update(start.x(), start.y(), 'J');
		fillInside(grid);
		System.out.println(grid);
		int result = countPoints(grid);
		System.out.println(result);
	}

	private static void fillInside(Grid grid) {
		// determine start
		for (int y = 0; y < grid.height(); y++) {
			for (int x = 0; x < grid.width(); x++) {
				final char c = grid.get(x, y).c();
				if (c != 'O' && c != '.') {
					Point start = new Point(x, y);
					NextFollow follow;
					if (c == 'F') {
						follow = new NextFollow(start, D8.UP_AND_LEFT, Direction.EAST);
					} else {
						throw new IllegalStateException(String.format("Expected F but found %c at %d,%d", c, x, y));
					}
					follow = paintAndFollow(grid, follow);
					while (!follow.p().equals(start)) {
						follow = paintAndFollow(grid, follow);
					}
					return;
				}
			}
		}
	}
	
	private static record NextFollow(Point p, D8 fillDir, Direction dir) {}

	private static NextFollow paintAndFollow(final Grid grid, final NextFollow follow) {
		final Point p = follow.p();
		final D8 fillDir = follow.fillDir();
		final Direction dir = follow.dir();
		fillNeighboursInside(grid, p, fillDir);
		final Point next = switch(dir) {
			case NORTH -> new Point(p.x(), p.y() - 1);
			case EAST -> new Point(p.x() + 1, p.y());
			case SOUTH -> new Point(p.x(), p.y() + 1);
			case WEST -> new Point(p.x() - 1, p.y());
		};
		final Direction newDir = switch (dir) {
			case NORTH -> switch (grid.get(next).c()) {
				case '7' -> Direction.WEST;
				case '|' -> Direction.NORTH;
				case 'F' -> Direction.EAST;
				default -> throw new IllegalStateException();
			};
			case EAST -> switch (grid.get(next).c()) {
				case 'J' -> Direction.NORTH;
				case '-' -> Direction.EAST;
				case '7' -> Direction.SOUTH;
				default -> throw new IllegalStateException();
			};
			case SOUTH -> switch (grid.get(next).c()) {
				case 'J' -> Direction.WEST;
				case '|' -> Direction.SOUTH;
				case 'L' -> Direction.EAST;
				default -> throw new IllegalStateException();
			};
			case WEST -> switch (grid.get(next).c()) {
				case 'L' -> Direction.NORTH;
				case '-' -> Direction.WEST;
				case 'F' -> Direction.SOUTH;
				default -> throw new IllegalStateException();
			};
		};
		final D8 newFillDir = switch (grid.get(next).c()) {
			case '-' -> fillDir == D8.UP || fillDir == D8.UP_AND_LEFT || fillDir == D8.UP_AND_RIGHT ? D8.UP : D8.DOWN;
			case '|' -> fillDir == D8.LEFT || fillDir == D8.UP_AND_LEFT || fillDir == D8.DOWN_AND_LEFT ? D8.LEFT : D8.RIGHT;
			case '7' -> switch(dir) {
				case EAST -> fillDir == D8.DOWN_AND_RIGHT || fillDir == D8.DOWN_AND_LEFT || fillDir == D8.DOWN ? D8.DOWN_AND_LEFT : D8.UP_AND_RIGHT;
				case NORTH -> fillDir == D8.UP_AND_LEFT || fillDir == D8.DOWN_AND_LEFT || fillDir == D8.LEFT ? D8.DOWN_AND_LEFT : D8.UP_AND_RIGHT;
				default -> throw new IllegalStateException();
			};
			case 'L' -> switch(dir) {
				case WEST -> fillDir == D8.UP_AND_LEFT || fillDir == D8.UP_AND_RIGHT || fillDir == D8.UP ? D8.UP_AND_RIGHT : D8.DOWN_AND_LEFT;
				case SOUTH -> fillDir == D8.RIGHT || fillDir == D8.UP_AND_RIGHT || fillDir == D8.DOWN_AND_RIGHT ? D8.UP_AND_RIGHT : D8.DOWN_AND_LEFT;
				default -> throw new IllegalStateException();
			};
			case 'F' -> switch(dir) {
				case WEST -> fillDir == D8.DOWN_AND_LEFT || fillDir == D8.DOWN_AND_RIGHT || fillDir == D8.DOWN ? D8.DOWN_AND_RIGHT : D8.UP_AND_LEFT;
				case NORTH -> fillDir == D8.RIGHT || fillDir == D8.DOWN_AND_RIGHT || fillDir == D8.UP_AND_RIGHT ? D8.DOWN_AND_RIGHT : D8.UP_AND_LEFT;
				default -> throw new IllegalStateException();
			};
			case 'J' -> switch(dir) {
				case EAST -> fillDir == D8.UP_AND_RIGHT || fillDir == D8.UP_AND_LEFT || fillDir == D8.UP ? D8.UP_AND_LEFT : D8.DOWN_AND_RIGHT;
				case SOUTH -> fillDir == D8.LEFT || fillDir == D8.UP_AND_LEFT || fillDir == D8.DOWN_AND_LEFT ? D8.UP_AND_LEFT : D8.DOWN_AND_RIGHT;
				default -> throw new IllegalStateException();
			};
			default -> throw new IllegalStateException();
		};
		return new NextFollow(next, newFillDir, newDir);
	}

	private static void fillNeighboursInside(Grid grid, Point p, D8 dir) {
			int x = p.x();
			int y = p.y();
//			System.out.println(String.format("Painting %s of %s %s", dir, p, grid.get(p).c));
			switch (dir) {
			case UP:
				fillReachable(grid, new Point(x, y - 1));
				break;
			case UP_AND_LEFT:
				fillReachable(grid, new Point(x, y - 1));
				fillReachable(grid, new Point(x - 1, y));
				break;
			case UP_AND_RIGHT:
				fillReachable(grid, new Point(x, y - 1));
				fillReachable(grid, new Point(x + 1, y));
				break;
			case DOWN:
				fillReachable(grid, new Point(x, y + 1));
				break;
			case DOWN_AND_LEFT:
				fillReachable(grid, new Point(x, y + 1));
				fillReachable(grid, new Point(x - 1, y));
				break;
			case DOWN_AND_RIGHT:
				fillReachable(grid, new Point(x, y + 1));
				fillReachable(grid, new Point(x + 1, y));
				break;
			case LEFT:
				fillReachable(grid, new Point(x - 1, y));
				break;
			case RIGHT:
				fillReachable(grid, new Point(x + 1, y));
				break;
		}
	}

	private static int countPoints(Grid grid) {
		int result = 0;
		for (int y = 0; y < grid.height(); y++) {
			for (int x = 0; x < grid.width(); x++) {
				if (grid.get(x, y).c() == '.') {
					result++;
				}
			}
		}
		return result;
	}

	private static void fillReachable(Grid grid, Point p) {
		Set<Point> outsidePoints = new HashSet<>();
		if (p.x() < 0 || p.x() >= grid.width() || p.y() < 0 || p.y() >= grid.height() || grid.get(p.x(), p.y()).c() != '.') {
			return;
		}
		outsidePoints.add(p);
		while (!outsidePoints.isEmpty()) {
			outsidePoints = findOutsideNeighbours(grid, outsidePoints);
		}
	}

	private static Set<Point> findOutsideNeighbours(Grid grid, Set<Point> outsidePoints) {
		Set<Point> result = new HashSet<>();
		for (Point p : outsidePoints) {
			int x = p.x();
			int y = p.y();
			grid.update(x, y, 'O');
			if (x >= 1 && grid.get(x - 1, y).c() == '.') {
				result.add(new Point(x - 1, y));
			}
			if (x < grid.width() - 1 && grid.get(x + 1, y).c() == '.') {
				result.add(new Point(x + 1, y));
			}
			if (y >= 1 && grid.get(x, y - 1).c() == '.') {
				result.add(new Point(x, y - 1));
			}
			if (y < grid.height() - 1 && grid.get(x, y + 1).c() == '.') {
				result.add(new Point(x, y + 1));
			}
		}
		return result;
	}

	private static void eraseNotPartOfLoop(Grid grid) {
		for (int y = 0; y < grid.height(); y++) {
			for (int x = 0; x < grid.width(); x++) {
				Node node = grid.get(x, y);
				if (node.pathLength() == -1) {
					node.setChar('.');
				}
			}
		}
	}
	
	private enum D8 {
		UP,
		UP_AND_LEFT,
		UP_AND_RIGHT,
		LEFT,
		RIGHT,
		DOWN,
		DOWN_AND_LEFT,
		DOWN_AND_RIGHT
	}

}

