package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		
		int highest = findHighest(grid);
		System.out.println(highest);
	}
	
	private static int findHighest(Grid grid) {
		int highest = 0;
		for (int y = 0; y < grid.height(); y++) {
			for (int x = 0; x < grid.width(); x++) {
				highest = Math.max(highest, grid.get(x, y).pathLength);
			}
		}
		return highest;
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
				&& (node.c == 'S' || RIGHT_OPTIONS.contains(node.c))) {
			grid.setPathLength(x - 1, y, pathLength + 1);
			result.add(new Point(x - 1, y));
		}
		// right
		if (x < grid.width() - 1 && grid.get(x + 1, y).pathLength == -1 && RIGHT_OPTIONS.contains(grid.get(x + 1, y).c)
				&& (node.c == 'S' || LEFT_OPTIONS.contains(node.c))) {
			grid.setPathLength(x + 1, y, pathLength + 1);
			result.add(new Point(x + 1, y));
		}
		// up
		if (y >= 1 && grid.get(x, y - 1).pathLength == -1 && UP_OPTIONS.contains(grid.get(x, y - 1).c)
				&& (node.c == 'S' || DOWN_OPTIONS.contains(node.c))) {
			grid.setPathLength(x, y - 1, pathLength + 1);
			result.add(new Point(x, y - 1));
		}
		// down
		if (y < grid.height() - 1 && grid.get(x, y + 1).pathLength == -1 && DOWN_OPTIONS.contains(grid.get(x, y + 1).c)
				&& (node.c == 'S' || UP_OPTIONS.contains(node.c))) {
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
				System.out.println(x + ", " + y);
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

		void set(int x, int y, char c) {
			nodes[y][x] = new Node(c);
		}
		
		Node get(int x, int y) {
			return nodes[y][x];
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
		final char c;
		int pathLength = -1;
		
		Node(char c) {
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

}
