package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.aoc2023.D10.findMainLoop;
import static nl.jessebrand.aoc.aoc2023.D10.findStart;
import static nl.jessebrand.aoc.aoc2023.D10.parseGrid;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.jessebrand.aoc.Direction;
import nl.jessebrand.aoc.Point;
import nl.jessebrand.aoc.aoc2023.D10.Grid;

public class D10b {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d10");
		final Grid grid = parseGrid(lines);

		final Point start = findStart(grid);
		grid.setPathLength(start, 0);
		System.out.println("Start: " + start);

		findMainLoop(grid, start);

		eraseNotPartOfLoop(grid);
		replaceStart(grid, start);
		fillInside(grid);
		System.out.println(grid);
		int result = countPoints(grid);
		System.out.println(result);
	}

	private static void replaceStart(final Grid grid, final Point start) {
		final boolean n = grid.getPathLength(start.apply(Direction.NORTH)) == 1;
		final boolean s = grid.getPathLength(start.apply(Direction.SOUTH)) == 1;
		final boolean e = grid.getPathLength(start.apply(Direction.EAST)) == 1;
		final boolean w = grid.getPathLength(start.apply(Direction.WEST)) == 1;
		final char newChar = n ? w ? 'J' : e ? 'L' : '|' : s ? w ? '7' : 'F' : '-';
		grid.update(start, newChar);
	}

	private static void fillInside(final Grid grid) {
		// determine start
		for (int y = 0; y < grid.height(); y++) {
			for (int x = 0; x < grid.width(); x++) {
				final char c = grid.getCharacter(x, y);
				if (c != 'O' && c != '.') {
					final Point start = new Point(x, y);
					NextFollow follow;
					if (c == 'F') {
						follow = new NextFollow(start, D8.NORTH_AND_WEST, Direction.EAST);
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

		final Point next = p.apply(dir);
		final Direction newDir = switch (dir) {
			case NORTH -> switch (grid.getCharacter(next)) {
				case '7' -> Direction.WEST;
				case '|' -> Direction.NORTH;
				case 'F' -> Direction.EAST;
				default -> throw new IllegalStateException();
			};
			case EAST -> switch (grid.getCharacter(next)) {
				case 'J' -> Direction.NORTH;
				case '-' -> Direction.EAST;
				case '7' -> Direction.SOUTH;
				default -> throw new IllegalStateException();
			};
			case SOUTH -> switch (grid.getCharacter(next)) {
				case 'J' -> Direction.WEST;
				case '|' -> Direction.SOUTH;
				case 'L' -> Direction.EAST;
				default -> throw new IllegalStateException();
			};
			case WEST -> switch (grid.getCharacter(next)) {
				case 'L' -> Direction.NORTH;
				case '-' -> Direction.WEST;
				case 'F' -> Direction.SOUTH;
				default -> throw new IllegalStateException();
			};
		};
		final D8 newFillDir = switch (grid.getCharacter(next)) {
			case '-' -> fillDir == D8.NORTH || fillDir == D8.NORTH_AND_WEST || fillDir == D8.NORTH_AND_EAST ? D8.NORTH : D8.SOUTH;
			case '|' -> fillDir == D8.WEST || fillDir == D8.NORTH_AND_WEST || fillDir == D8.SOUTH_AND_WEST ? D8.WEST : D8.EAST;
			case '7' -> switch(dir) {
				case EAST -> fillDir == D8.SOUTH_AND_EAST || fillDir == D8.SOUTH_AND_WEST || fillDir == D8.SOUTH ? D8.SOUTH_AND_WEST : D8.NORTH_AND_EAST;
				case NORTH -> fillDir == D8.NORTH_AND_WEST || fillDir == D8.SOUTH_AND_WEST || fillDir == D8.WEST ? D8.SOUTH_AND_WEST : D8.NORTH_AND_EAST;
				default -> throw new IllegalStateException();
			};
			case 'L' -> switch(dir) {
				case WEST -> fillDir == D8.NORTH_AND_WEST || fillDir == D8.NORTH_AND_EAST || fillDir == D8.NORTH ? D8.NORTH_AND_EAST : D8.SOUTH_AND_WEST;
				case SOUTH -> fillDir == D8.EAST || fillDir == D8.NORTH_AND_EAST || fillDir == D8.SOUTH_AND_EAST ? D8.NORTH_AND_EAST : D8.SOUTH_AND_WEST;
				default -> throw new IllegalStateException();
			};
			case 'F' -> switch(dir) {
				case WEST -> fillDir == D8.SOUTH_AND_WEST || fillDir == D8.SOUTH_AND_EAST || fillDir == D8.SOUTH ? D8.SOUTH_AND_EAST : D8.NORTH_AND_WEST;
				case NORTH -> fillDir == D8.EAST || fillDir == D8.SOUTH_AND_EAST || fillDir == D8.NORTH_AND_EAST ? D8.SOUTH_AND_EAST : D8.NORTH_AND_WEST;
				default -> throw new IllegalStateException();
			};
			case 'J' -> switch(dir) {
				case EAST -> fillDir == D8.NORTH_AND_EAST || fillDir == D8.NORTH_AND_WEST || fillDir == D8.NORTH ? D8.NORTH_AND_WEST : D8.SOUTH_AND_EAST;
				case SOUTH -> fillDir == D8.WEST || fillDir == D8.NORTH_AND_WEST || fillDir == D8.SOUTH_AND_WEST ? D8.NORTH_AND_WEST : D8.SOUTH_AND_EAST;
				default -> throw new IllegalStateException();
			};
			default -> throw new IllegalStateException();
		};
		return new NextFollow(next, newFillDir, newDir);
	}

	private static void fillNeighboursInside(final Grid grid, final Point p, final D8 dir) {
//		System.out.println(String.format("%s: Paint %s %c", p, dir, grid.getCharacter(p)));
		dir.directions().stream()
				.map(d -> p.apply(d))
				.forEach(p2 -> fillReachable(grid, p2));
	}

	private static int countPoints(final Grid grid) {
		int result = 0;
		for (int y = 0; y < grid.height(); y++) {
			for (int x = 0; x < grid.width(); x++) {
				if (grid.getCharacter(x, y) == '.') {
					result++;
				}
			}
		}
		return result;
	}

	private static void fillReachable(final Grid grid, final Point p) {
		Set<Point> outsidePoints = new HashSet<>();
		if (grid.getCharacter(p.x(), p.y()) != '.') {
			return;
		}
		outsidePoints.add(p);
		while (!outsidePoints.isEmpty()) {
			outsidePoints = findOutsideNeighbours(grid, outsidePoints);
		}
	}

	private static Set<Point> findOutsideNeighbours(final Grid grid, final Set<Point> outsidePoints) {
		final Set<Point> result = new HashSet<>();
		for (Point p : outsidePoints) {
			grid.update(p, 'O');
			for (final Direction dir : Direction.values()) {
				final Point newP = p.apply(dir);
				if (grid.getCharacter(newP) == '.') {
					result.add(newP);
				}
			}
		}
		return result;
	}

	private static void eraseNotPartOfLoop(final Grid grid) {
		for (int y = 0; y < grid.height(); y++) {
			for (int x = 0; x < grid.width(); x++) {
				if (grid.getPathLength(x, y) == -1) {
					grid.update(x, y, '.');
				}
			}
		}
	}

	private enum D8 {
		NORTH(Direction.NORTH),
		NORTH_AND_WEST(Direction.NORTH, Direction.WEST),
		NORTH_AND_EAST(Direction.NORTH, Direction.EAST),
		WEST(Direction.WEST),
		EAST(Direction.EAST),
		SOUTH(Direction.SOUTH),
		SOUTH_AND_WEST(Direction.SOUTH, Direction.WEST),
		SOUTH_AND_EAST(Direction.SOUTH, Direction.EAST);
		
		private final List<Direction> directions;

		private D8(final Direction... directions) {
			this.directions = Arrays.asList(directions);
		}
		
		public final List<Direction> directions() {
			return directions;
		}
	}

}

