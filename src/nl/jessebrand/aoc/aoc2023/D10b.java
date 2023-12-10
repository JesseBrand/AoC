package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.jessebrand.aoc.Direction;
import nl.jessebrand.aoc.Point;

public class D10b {
	
	private static final List<Character> LEFT_OPTIONS = Arrays.asList('-', 'L', 'F', 'S');
	private static final List<Character> RIGHT_OPTIONS = Arrays.asList('-', '7', 'J', 'S');
	private static final List<Character> UP_OPTIONS = Arrays.asList('|', '7', 'F', 'S');
	private static final List<Character> DOWN_OPTIONS = Arrays.asList('|', 'L', 'J', 'S');
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d10");
//		System.out.println(lines);
		final Grid grid = parseGrid(lines);
//		System.out.println(grid);

		final Point start = findStart(grid);
		grid.setPathLength(start.x(), start.y(), 0);
		System.out.println(start);
//		System.out.println(grid.toLengthString());

		List<Point> next = findNeighbours(grid, start, grid.get(start.x(), start.y()));
//		System.out.println("next1 = " + next);
		while (!next.isEmpty()) {
			next = findNeighbours(grid, next);
//			System.out.println(grid.toLengthString());
//			System.out.println("next2 = " + next);
		}
//		System.out.println(grid.toLengthString());
		
		eraseNotPartOfLoop(grid);
		grid.update(start.x(), start.y(), 'J');
//		fillReachable(grid, new Point(0, 0));
//		System.out.println(grid);
		fillInside(grid);
		System.out.println(grid);
		int result = countPoints(grid);
		System.out.println(result);
	}

	private static void fillInside(Grid grid) {
		// determine start
		for (int y = 0; y < grid.height(); y++) {
			for (int x = 0; x < grid.width(); x++) {
				Node node = grid.get(x, y);
				char c = node.c;
				if (c != 'O' && c != '.') {
					Point start = new Point(x, y);
					NextFollow follow;
					if (c == 'F') {
						follow = new NextFollow(start, N.UP_AND_LEFT, Direction.EAST);
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
	
	private static record NextFollow(Point p, N fillDir, Direction dir) {}

	private static NextFollow paintAndFollow(Grid grid, NextFollow follow) {
		final Point p = follow.p();
		final N fillDir = follow.fillDir();
		final Direction dir = follow.dir();
		fillNeighboursInside(grid, p, fillDir);
		Point next = switch(dir) {
		case NORTH -> new Point(p.x(), p.y() - 1);
		case EAST -> new Point(p.x() + 1, p.y());
		case SOUTH -> new Point(p.x(), p.y() + 1);
		case WEST -> new Point(p.x() - 1, p.y());
		};
		final Direction newDir = switch (dir) {
			case NORTH -> switch (grid.get(next).c) {
				case '7' -> Direction.WEST;
				case '|' -> Direction.NORTH;
				case 'F' -> Direction.EAST;
				default -> throw new IllegalStateException();
			};
			case EAST -> switch (grid.get(next).c) {
				case 'J' -> Direction.NORTH;
				case '-' -> Direction.EAST;
				case '7' -> Direction.SOUTH;
				default -> throw new IllegalStateException();
			};
			case SOUTH -> switch (grid.get(next).c) {
				case 'J' -> Direction.WEST;
				case '|' -> Direction.SOUTH;
				case 'L' -> Direction.EAST;
				default -> throw new IllegalStateException();
			};
			case WEST -> switch (grid.get(next).c) {
				case 'L' -> Direction.NORTH;
				case '-' -> Direction.WEST;
				case 'F' -> Direction.SOUTH;
				default -> throw new IllegalStateException();
			};
		};
		final N newFillDir = switch (grid.get(next).c) {
			case '-' -> fillDir == N.UP || fillDir == N.UP_AND_LEFT || fillDir == N.UP_AND_RIGHT ? N.UP : N.DOWN;
			case '|' -> fillDir == N.LEFT || fillDir == N.UP_AND_LEFT || fillDir == N.DOWN_AND_LEFT ? N.LEFT : N.RIGHT;
			case '7' -> switch(dir) {
				case EAST -> fillDir == N.DOWN_AND_RIGHT || fillDir == N.DOWN_AND_LEFT || fillDir == N.DOWN ? N.DOWN_AND_LEFT : N.UP_AND_RIGHT;
				case NORTH -> fillDir == N.UP_AND_LEFT || fillDir == N.DOWN_AND_LEFT || fillDir == N.LEFT ? N.DOWN_AND_LEFT : N.UP_AND_RIGHT;
				default -> throw new IllegalStateException();
			};
			case 'L' -> switch(dir) {
				case WEST -> fillDir == N.UP_AND_LEFT || fillDir == N.UP_AND_RIGHT || fillDir == N.UP ? N.UP_AND_RIGHT : N.DOWN_AND_LEFT;
				case SOUTH -> fillDir == N.RIGHT || fillDir == N.UP_AND_RIGHT || fillDir == N.DOWN_AND_RIGHT ? N.UP_AND_RIGHT : N.DOWN_AND_LEFT;
				default -> throw new IllegalStateException();
			};
			case 'F' -> switch(dir) {
				case WEST -> fillDir == N.DOWN_AND_LEFT || fillDir == N.DOWN_AND_RIGHT || fillDir == N.DOWN ? N.DOWN_AND_RIGHT : N.UP_AND_LEFT;
				case NORTH -> fillDir == N.RIGHT || fillDir == N.DOWN_AND_RIGHT || fillDir == N.UP_AND_RIGHT ? N.DOWN_AND_RIGHT : N.UP_AND_LEFT;
				default -> throw new IllegalStateException();
			};
			case 'J' -> switch(dir) {
				case EAST -> fillDir == N.UP_AND_RIGHT || fillDir == N.UP_AND_LEFT || fillDir == N.UP ? N.UP_AND_LEFT : N.DOWN_AND_RIGHT;
				case SOUTH -> fillDir == N.LEFT || fillDir == N.UP_AND_LEFT || fillDir == N.DOWN_AND_LEFT ? N.UP_AND_LEFT : N.DOWN_AND_RIGHT;
				default -> throw new IllegalStateException();
			};
			default -> throw new IllegalStateException();
		};
		return new NextFollow(next, newFillDir, newDir);
	}

	private static void fillNeighboursInside(Grid grid, Point p, N dir) {
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
//		int length = 0;
		for (int y = 0; y < grid.height(); y++) {
			for (int x = 0; x < grid.width(); x++) {
				if (grid.get(x, y).c == '.') {
					result++;
//				} else if (grid.get(x, y).c != 'O') {
//					length++;
				}
			}
		}
//		System.out.println("length: " + length);
		return result;
	}

	private static void fillReachable(Grid grid, Point p) {
		Set<Point> outsidePoints = new HashSet<>();
		if (p.x() < 0 || p.x() >= grid.width() || p.y() < 0 || p.y() >= grid.height() || grid.get(p.x(), p.y()).c != '.') {
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
			if (x >= 1 && grid.get(x - 1, y).c == '.') {
				result.add(new Point(x - 1, y));
			}
			if (x < grid.width() - 1 && grid.get(x + 1, y).c == '.') {
				result.add(new Point(x + 1, y));
			}
			if (y >= 1 && grid.get(x, y - 1).c == '.') {
				result.add(new Point(x, y - 1));
			}
			if (y < grid.height() - 1 && grid.get(x, y + 1).c == '.') {
				result.add(new Point(x, y + 1));
			}
		}
		return result;
	}

	private static void eraseNotPartOfLoop(Grid grid) {
		for (int y = 0; y < grid.height(); y++) {
			for (int x = 0; x < grid.width(); x++) {
				Node node = grid.get(x, y);
				if (node.pathLength == -1) {
					node.setChar('.');
				}
			}
		}
	}

	private static List<Point> findNeighbours(Grid grid, List<Point> next) {
		final List<Point> result = new ArrayList<>();
		for (Point point : next) {
			Node node = grid.get(point.x(), point.y());
			result.addAll(findNeighbours(grid, point, node));
		}
		return result;
	}

	private static List<Point> findNeighbours(Grid grid, Point p, Node node) {
		int x = p.x();
		int y = p.y();
		int pathLength = node.pathLength;
		final List<Point> result = new ArrayList<>();
		// left
		if (x >= 1 && grid.get(x - 1, y).pathLength == -1 && LEFT_OPTIONS.contains(grid.get(x - 1, y).c)
				&& RIGHT_OPTIONS.contains(node.c)) {
			grid.setPathLength(x - 1, y, pathLength + 1);
			result.add(new Point(x - 1, y));
		}
		// right
		if (x < grid.width() - 1 && grid.get(x + 1, y).pathLength == -1 && RIGHT_OPTIONS.contains(grid.get(x + 1, y).c)
				&& LEFT_OPTIONS.contains(node.c)) {
			grid.setPathLength(x + 1, y, pathLength + 1);
			result.add(new Point(x + 1, y));
		}
		// up
		if (y >= 1 && grid.get(x, y - 1).pathLength == -1 && UP_OPTIONS.contains(grid.get(x, y - 1).c)
				&& DOWN_OPTIONS.contains(node.c)) {
			grid.setPathLength(x, y - 1, pathLength + 1);
			result.add(new Point(x, y - 1));
		}
		// down
		if (y < grid.height() - 1 && grid.get(x, y + 1).pathLength == -1 && DOWN_OPTIONS.contains(grid.get(x, y + 1).c)
				&& UP_OPTIONS.contains(node.c)) {
			grid.setPathLength(x, y + 1, pathLength + 1);
			result.add(new Point(x, y + 1));
		}
		return result;
	}

	private static Grid parseGrid(List<String> lines) {
		final Grid grid = new Grid(lines.get(0).length(), lines.size());
		System.out.println("Grid size = (WxH) " + lines.get(0).length() + "x" + lines.size());
		for (int y = 0; y < lines.size(); y++) {
			for (int x = 0; x < lines.get(y).length(); x++) {
				grid.set(x, y, lines.get(y).charAt(x));
			}
		}
		return grid;
	}

	private static Point findStart(Grid grid) {
		for (int y = 0; y < grid.height(); y++) {
			for (int x = 0; x < grid.width(); x++) {
				if (grid.get(x, y).c == 'S') {
					return new Point(x, y);
				}
			}
		}
		throw new IllegalStateException();
	}

	private static class Grid {
		
		private final Node[][] nodes;
		
		Grid(int width, int height) {
			nodes = new Node[height][width];
		}
		
		public void setPathLength(int x, int y, int pathLength) {
			get(x, y).setPathLength(pathLength);			
		}

		void update(int x, int y, char c) {
			nodes[y][x].setChar(c);
		}
		
		void set(int x, int y, char c) {
			if (nodes[y][x] != null) {
				throw new IllegalStateException();
			}
			nodes[y][x] = new Node(c);
		}
		
		Node get(int x, int y) {
			return nodes[y][x];
		}
		
		Node get(Point p) {
			return get(p.x(), p.y());
		}
		
		int height() {
			return nodes.length;
		}
		
		int width() {
			return nodes[0].length;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int y = 0; y < nodes.length; y++) {
				for (int x = 0; x < nodes[y].length; x++) {
					sb.append(nodes[y][x].c);
				}
				sb.append("\n");
			}
			return sb.toString();
		}
	}
	
	private static class Node {
		char c;
		int pathLength = -1;
		
		Node(char c) {
			this.c = c;
		}

		void setChar(char c) {
			this.c = c;
		}
		
		void setPathLength(int length) {
			this.pathLength = length;
		}
		
		@Override
		public String toString() {
			return "" + c;
		}
	}
	
	private enum N {
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

