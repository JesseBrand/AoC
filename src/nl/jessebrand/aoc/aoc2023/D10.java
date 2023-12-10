package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.jessebrand.aoc.Direction;
import nl.jessebrand.aoc.Point;

public class D10 {

	private static final List<Character> LEFT_OPTIONS = Arrays.asList('-', 'L', 'F', 'S');
	private static final List<Character> RIGHT_OPTIONS = Arrays.asList('-', '7', 'J', 'S');
	private static final List<Character> UP_OPTIONS = Arrays.asList('|', '7', 'F', 'S');
	private static final List<Character> DOWN_OPTIONS = Arrays.asList('|', 'L', 'J', 'S');
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d10");
		System.out.println(lines);
		final Grid grid = parseGrid(lines);
		System.out.println(grid);

		final Point start = findStart(grid);
		grid.setPathLength(start, 0);
		System.out.println("Start: " + start);

		findMainLoop(grid, start);
		
		final int highest = findHighest(grid);
		System.out.println(highest);
	}
	
	static void findMainLoop(final Grid grid, final Point start) {
		List<Point> next = findNeighbours(grid, start);
		while (!next.isEmpty()) {
			next = findNeighbours(grid, next);
		}
	}

	private static int findHighest(final Grid grid) {
		int highest = 0;
		for (int y = 0; y < grid.height(); y++) {
			for (int x = 0; x < grid.width(); x++) {
				highest = Math.max(highest, grid.getPathLength(x, y));
			}
		}
		return highest;
	}

	private static List<Point> findNeighbours(final Grid grid, final List<Point> points) {
		final List<Point> result = new ArrayList<>();
		for (final Point point : points) {
			result.addAll(findNeighbours(grid, point));
		}
		return result;
	}

	private static List<Point> findNeighbours(final Grid grid, final Point p) {
		final char c = grid.getCharacter(p);
		final int pathLength = grid.getPathLength(p);
		final List<Point> result = new ArrayList<>();
		// left
		final Point w = p.apply(Direction.WEST);
		if (grid.getPathLength(w) == -1 && LEFT_OPTIONS.contains(grid.getCharacter(w))
				&& RIGHT_OPTIONS.contains(c)) {
			grid.setPathLength(w, pathLength + 1);
			result.add(w);
		}
		// right
		final Point e = p.apply(Direction.EAST);
		if (grid.getPathLength(e) == -1 && RIGHT_OPTIONS.contains(grid.getCharacter(e))
				&& LEFT_OPTIONS.contains(c)) {
			grid.setPathLength(e, pathLength + 1);
			result.add(e);
		}
		// up
		final Point n = p.apply(Direction.NORTH);
		if (grid.getPathLength(n) == -1 && UP_OPTIONS.contains(grid.getCharacter(n))
				&& DOWN_OPTIONS.contains(c)) {
			grid.setPathLength(n, pathLength + 1);
			result.add(n);
		}
		// down
		final Point s = p.apply(Direction.SOUTH);
		if (grid.getPathLength(s) == -1 && DOWN_OPTIONS.contains(grid.getCharacter(s))
				&& UP_OPTIONS.contains(c)) {
			grid.setPathLength(s, pathLength + 1);
			result.add(s);
		}
		return result;
	}

	static Grid parseGrid(final List<String> lines) {
		final Grid grid = new Grid(lines.get(0).length(), lines.size());
		System.out.println("Grid size = (WxH) " + lines.get(0).length() + "x" + lines.size());
		for (int y = 0; y < lines.size(); y++) {
			for (int x = 0; x < lines.get(y).length(); x++) {
				grid.set(x, y, lines.get(y).charAt(x));
			}
		}
		return grid;
	}

	static Point findStart(final Grid grid) {
		for (int y = 0; y < grid.height(); y++) {
			for (int x = 0; x < grid.width(); x++) {
				if (grid.get(x, y).c() == 'S') {
					return new Point(x, y);
				}
			}
		}
		throw new IllegalStateException();
	}

	static final class Grid {
		
		private final Node[][] nodes;
		
		Grid(final int width, final int height) {
			nodes = new Node[height][width];
		}
		
		void setPathLength(final Point p, final int pathLength) {
			get(p).setPathLength(pathLength);			
		}

		void update(final int x, final int y, final char c) {
			nodes[y][x].setChar(c);
		}

		void update(final Point p, final char c) {
			update(p.x(), p.y(), c);
		}
		
		void set(final int x, final int y, final char c) {
			if (nodes[y][x] != null) {
				throw new IllegalStateException();
			}
			nodes[y][x] = new Node(c);
		}
		
		private Node get(final int x, final int y) {
			return nodes[y][x];
		}
		
		private Node safeGet(final int x, final int y) {
			if (x < 0 || x >= width() || y < 0 || y >= height()) {
				return null;
			}
			return get(x, y);
		}
		
		char getCharacter(final int x, final int y) {
			final Node n = safeGet(x, y);
			return n == null ? (char) 0 : n.c();
		}
		
		char getCharacter(final Point p) {
			return getCharacter(p.x(), p.y());
		}
		
		int getPathLength(final int x, final int y) {
			final Node n = safeGet(x, y);
			return n == null ? -1 : n.pathLength();
		}

		int getPathLength(final Point p) {
			return getPathLength(p.x(), p.y());
		}
		
		Node get(final Point p) {
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
					sb.append(mapChar(nodes[y][x].c()));
				}
				sb.append("\n");
			}
			return sb.toString();
		}

		private static char mapChar(char c) {
			return switch (c) {
				case '-' -> '─';
				case '|' -> '│';
				case 'F' -> '┌';
				case 'J' -> '┘';
				case 'L' -> '└';
				case '7' -> '┐';
				case 'I' -> '▓';
				default -> c;
			};
		}

		private final class Node {
			private char c;
			private int pathLength = -1;
			
			Node(char c) {
				this.c = c;
			}
			
			void setPathLength(final int length) {
				this.pathLength = length;
			}
			
			void setChar(final char c) {
				this.c = c;
			}
			
			char c() {
				return c;
			}
			
			int pathLength() {
				return pathLength;
			}
			
			@Override
			public String toString() {
				return "" + c;
			}
		}
	}
	
}
