package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.jessebrand.aoc.Point;

public class D12b {
	
	static final int WIDTH = 113;
	static final int HEIGHT = 41;
	
	static final int INC = 96;
	static final int START = 'S' - INC;
	static final int END = 'E' - INC;

	public static void main(String[] args) throws IOException {
		Grid grid = readGrid();
//        System.out.println(grid);
		Point end = null;
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				int val = grid.getHeight(x, y);
				if (val == START) {
					grid.setHeight(x, y, 1);
				}
				if (val == END) {
					end = new Point(x, y);
					grid.setHeight(x, y, 26);
				}
			}
		}
		fillHeights(grid, end);
		int result = findLowest(grid);
		System.out.println(grid.toLengthString());
		System.out.println("Lowest with height 1 : " + result);
	}
	
	private static int findLowest(Grid grid) {
		int lowest = 999;
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				int length = grid.getPathLength(x, y);
				if (grid.getHeight(x, y) == 1 && length < lowest && length != -1) {
					lowest = length;
				}
			}
		}
		return lowest;
	}

	private static void fillHeights(Grid grid, Point start) {
		System.out.println("Finding from " + start);
		Set<Point> curList = new HashSet<>();
		curList.add(start);
		
		int length = 0;
		
		while (!curList.isEmpty()) {
			System.out.println("List to evaluate (" + length + "): " + curList);
			Set<Point> nextList = new HashSet<>();
			for (Point loc : curList) {
				grid.setPathLength(loc, length);
				int curHeight = grid.getHeight(loc);
				// the one to the left
				if (loc.x() > 0 && grid.isPathLengthUnset(loc.x() - 1,  loc.y()) && grid.getHeight(loc.x() - 1, loc.y()) >= curHeight - 1) {
					nextList.add(new Point(loc.x() - 1, loc.y()));
				}
				// the one above
				if (loc.y() > 0 && grid.isPathLengthUnset(loc.x(),  loc.y() - 1) && grid.getHeight(loc.x(), loc.y() - 1) >= curHeight - 1) {
					nextList.add(new Point(loc.x(), loc.y() - 1));
				}
				// the one to the right
				if (loc.x() < WIDTH - 1 && grid.isPathLengthUnset(loc.x() + 1,  loc.y()) && grid.getHeight(loc.x() + 1, loc.y()) >= curHeight - 1) {
					nextList.add(new Point(loc.x() + 1, loc.y()));
				}
				// the one below
				if (loc.y() < HEIGHT - 1 && grid.isPathLengthUnset(loc.x(),  loc.y() + 1) && grid.getHeight(loc.x(), loc.y() + 1) >= curHeight - 1) {
					nextList.add(new Point(loc.x(), loc.y() + 1));
				}
			}
			curList = nextList;
			length++;
		}
		
		
	}

	private static Grid readGrid() throws IOException {
		final List<String> lines = readFile("2022/d12");
		Grid grid = new Grid(WIDTH, HEIGHT);
    	int y = 0;
        for (final String line : lines) {
        	for (int x = 0; x < WIDTH; x++) {
        		grid.setHeight(x, y, line.charAt(x) - INC);
        	}
        	y++;
        }
        return grid;
	}

	static class Grid {
		
		private final int[][] heights;
		private final int[][] pathLength;
		
		Grid(int width, int height) {
			heights = new int[height][width];
			pathLength = new int[height][width];
		}
		
		void setHeight(int x, int y, int value) {
			heights[y][x] = value;
			pathLength[y][x] = -1;
		}
		
		int getHeight(Point loc) {
			return getHeight(loc.x(), loc.y());
		}
		
		int getHeight(int x, int y) {
			return heights[y][x];
		}
		
		void setPathLength(Point loc, int pathLength) {
			setPathLength(loc.x(), loc.y(), pathLength);
		}
		
		void setPathLength(int x, int y, int pathLength) {
			this.pathLength[y][x] = pathLength;
		}
		
		boolean isPathLengthUnset(int x, int y) {
			return getPathLength(x, y) == -1;
		}
		
		int getPathLength(Point loc) {
			return getPathLength(loc.x(), loc.y());
		}
		
		int getPathLength(int x, int y) {
			return pathLength[y][x];
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int y = 0; y < heights.length; y++) {
				for (int x = 0; x < heights[y].length; x++) {
					int val = heights[y][x];
					sb.append((val < 10 ? " " : "") + val + "|");
				}
				sb.append("\n");
			}
			return sb.toString();
		}
		
		public String toLengthString() {
			StringBuilder sb = new StringBuilder();
			for (int y = 0; y < pathLength.length; y++) {
				for (int x = 0; x < pathLength[y].length; x++) {
					int val = pathLength[y][x];
					sb.append((val < 100 && val >= 0 ? " " : "") + (val < 10 ? " " : "") + val + "|");
				}
				sb.append("\n");
			}
			return sb.toString();
		}
	}
}
