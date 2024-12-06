package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.applyDirection;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.jessebrand.aoc.Direction;
import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;

public class D06 {
	
	private enum Square {
		UNVISITED("."),
		VISITED_N("X"),
		VISITED_E("X"),
		VISITED_S("X"),
		VISITED_W("X"),
		BLOCKED("#");
		
		private final String output;

		Square(final String output) {
			this.output = output;
		}

		@Override
		public String toString() {
			return output;
		}
	}
	
	private static final Map<Direction, Square> DIR_TO_SQUARE;
	
	static {
		final Map<Direction, Square> map = new HashMap<>();
		map.put(Direction.NORTH, Square.VISITED_N);
		map.put(Direction.EAST, Square.VISITED_E);
		map.put(Direction.SOUTH, Square.VISITED_S);
		map.put(Direction.WEST, Square.VISITED_W);
		DIR_TO_SQUARE = Collections.unmodifiableMap(map);
	}
	
	private static record Guard(Point pos, Direction dir) {}

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d06");
		final Grid<Square> blankGrid = buildGrid(lines);
		final Grid<Square> grid = blankGrid.clone();
		final Guard guardStart = findGuard(lines);
//		System.out.println(grid);
		System.out.println(guardStart);
		
		if (followRoute(grid, guardStart)) {
			throw new IllegalStateException();
		}

//		System.out.println(grid);
		
		final List<Square> visitedTypes = Arrays.asList(new Square[] {Square.VISITED_N, Square.VISITED_E, Square.VISITED_S, Square.VISITED_W});
		System.out.println(gridCount(grid, visitedTypes));

		int result2 = 0;
		for (int x = 0; x < grid.getWidth(); x++) {
			for (int y = 0; y < grid.getHeight(); y++) {
				if (visitedTypes.contains(grid.get(x, y))) {
					// try for this point.
					final Grid<Square> attemptGrid = blankGrid.clone();
					attemptGrid.set(x, y, Square.BLOCKED);
					if (followRoute(attemptGrid, guardStart)) {
						result2++;
					}
				}
			}
		}
		System.out.println(result2);
	}

	private static boolean followRoute(Grid<Square> grid, Guard guardStart) {
		Guard guard = guardStart;
		while (true) {
			final Point target = applyDirection(guard.pos(), guard.dir());
			if (!grid.contains(target)) {
				System.out.println("Left Grid at " + target);
				guard = null;
				return false;
			} else {
				final Square targetSquare = grid.get(target);
				switch (targetSquare) {
				case UNVISITED:
					guard = new Guard(target, guard.dir());
					grid.set(target, DIR_TO_SQUARE.get(guard.dir()));
					break;
				case VISITED_N, VISITED_E, VISITED_S, VISITED_W:
					if (targetSquare == DIR_TO_SQUARE.get(guard.dir())) {
						return true;
					}
					guard = new Guard(target, guard.dir());
					break;
				case BLOCKED:
					guard = new Guard(guard.pos(), rotateRight(guard.dir()));
					break;
				}
			}
		}
	}

	private static <T> int gridCount(final Grid<T> grid, final T... types) {
		return gridCount(grid, Arrays.asList(types));
	}
	
	private static <T> int gridCount(final Grid<T> grid, final List<T> types) {
		int result = 0;
		for (int x = 0; x < grid.getWidth(); x++) {
			for (int y = 0; y < grid.getHeight(); y++) {
				if (types.contains(grid.get(x, y))) {
					result++;
				}
			}
		}
		return result;
	}

	private static Direction rotateRight(Direction dir) {
		return switch(dir) {
			case NORTH -> Direction.EAST;
			case EAST -> Direction.SOUTH;
			case SOUTH -> Direction.WEST;
			case WEST -> Direction.NORTH;
		};
	}

	private static Guard findGuard(List<String> lines) {
		for (int y = 0; y < lines.size(); y++) {
			String line = lines.get(y);
			for (int x = 0; x < line.length(); x++) {
				if (line.charAt(x) == '^') {
					return new Guard(new Point(x, y), Direction.NORTH);
				}
			}
		}
		throw new IllegalStateException();
	}

	private static Grid<Square> buildGrid(List<String> lines) {
		int height = lines.size();
		int width = lines.get(0).length();
		final Grid<Square> grid = new Grid<>(width, height, "");
		for (int y = 0; y < height; y++) {
			String line = lines.get(y);
			for (int x = 0; x < width; x++) {
				grid.set(x, y, parseSquare(line.charAt(x)));
			}
		}
		return grid;
	}

	private static Square parseSquare(char c) {
		return switch (c) {
			case '.' -> Square.UNVISITED;
			case '#' -> Square.BLOCKED;
			case '^' -> Square.VISITED_N;
			default -> throw new IllegalArgumentException("Unknown character: " + c);
		};
	}
	
}
