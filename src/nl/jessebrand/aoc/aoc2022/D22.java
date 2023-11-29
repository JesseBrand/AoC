package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.aoc2022.D22.Heading.EAST;
import static nl.jessebrand.aoc.aoc2022.D22.Heading.NORTH;
import static nl.jessebrand.aoc.aoc2022.D22.Heading.SOUTH;
import static nl.jessebrand.aoc.aoc2022.D22.Heading.WEST;
import static nl.jessebrand.aoc.aoc2022.D22.Tile.AIR;
import static nl.jessebrand.aoc.aoc2022.D22.Tile.WALL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;

public class D22 {
	
	private static final Instruction LEFT = new LeftInstruction();
	private static final Instruction RIGHT = new RightInstruction();

	public static void main(String[] args) throws IOException {
		initHeadings();
		final List<String> lines = readFile("2022/d22");
		final Grid<Tile> grid = parseGrid(lines.subList(0, lines.size() - 2));
		final List<Instruction> instructions = parseInstructions(lines.get(lines.size() - 1));
		out("Instructions: %s", instructions);
		
		Location location = determineStart(grid);
		out("Start: %s", location);
		for (Instruction instruction : instructions) {
			out(instruction);
			location = instruction.process(grid, location);
			out(location);
		}
		out("Ended up at %d,%d heading %s, result=%d", location.x(), location.y(), location.heading(), 1000 * (location.y() + 1) + 4 * (location.x() + 1) + location.heading().value());
	}
	
	private static List<Instruction> parseInstructions(final String string) {
		final List<Instruction> result = new ArrayList<>();
		String intVal = "";
		for (char c : string.toCharArray()) {
			if (c == 'R' || c == 'L') {
				if (intVal.length() > 0) {
					result.add(new MoveInstruction(Integer.parseInt(intVal)));
					intVal = "";
				}
				if (c == 'R') {
					result.add(RIGHT);
				} else {
					result.add(LEFT);
				}
			} else {
				intVal += c;
			}
		}
		if (intVal.length() > 0) {
			result.add(new MoveInstruction(Integer.parseInt(intVal)));
		}
		return result;
	}

	private static Location determineStart(Grid<Tile> grid) {
		for (int x = 0; x < grid.getWidth(); x++) {
			Tile tile = grid.get(x, 0);
			if (tile != AIR) {
				return new Location(x, 0, EAST);
			}
		}
		throw new IllegalStateException();
		
	}

	private static Grid<Tile> parseGrid(final List<String> lines) {
		int height = lines.size();
		int width = lines.get(0).length();
		Grid<Tile> result = new Grid<>(width, height, "");
		for (int y = 0; y < height; y++) {
			String line = lines.get(y);
			for (int x = 0; x < width; x++) {
				result.set(x, y, x >= line.length() ? AIR : Tile.parse(line.charAt(x)));
			}
		}
		return result;
	}

	static record Location(Point point, Heading heading) {
		
		Location(int x, int y, Heading heading) {
			this(new Point(x, y), heading);
		}
		
		int x() {
			return point.x();
		}
		
		int y() {
			return point.y();
		}
		
		@Override
		public String toString() {
			return String.format("%d,%d,%s", x(), y(), heading());
		}
	}
	
	static enum Tile {
		AIR(' '),
		OPEN('.'),
		WALL('#');

		private final char c;

		Tile(char c) {
			this.c = c;
		}

		static Tile parse(char charAt) {
			for (final Tile tile : values()) {
				if (tile.c == charAt) {
					return tile;
				}
			}
			throw new IllegalArgumentException("Unknown chracter: " + charAt);
		}
		
		
	}
	
	static void initHeadings() {
		Heading.EAST.setAdjacentDirections(NORTH, SOUTH);
		Heading.SOUTH.setAdjacentDirections(EAST, WEST);
		Heading.WEST.setAdjacentDirections(SOUTH, NORTH);
		Heading.NORTH.setAdjacentDirections(WEST, EAST);
	}
	
	static interface Instruction {
		Location process(Grid<Tile> grid, Location location);
	}
	
	static class MoveInstruction implements Instruction {
		
		private final int distance;

		public MoveInstruction(final int distance) {
			this.distance = distance;
		}
		
		@Override
		public Location process(final Grid<Tile> grid, final Location location) {
			Location loc = location;
			for (int i = 0; i < distance; i++) {
				int y = loc.y() + loc.heading().yInc;
				int x = loc.x() + loc.heading().xInc;
				if (y < 0 || y >= grid.getHeight() || x < 0 || x >= grid.getWidth() || grid.get(x, y) == AIR) {
					switch (loc.heading()) {
					case EAST:
						x = firstX(grid, y);
						break;
					case WEST:
						x = lastX(grid, y);
						break;
					case SOUTH:
						y = firstY(grid, x);
						break;
					case NORTH:
						y = lastY(grid, x);
						break;
					}
					out("%s overflowed to %d,%d", loc, x, y);
				}
				if (grid.get(x, y) == WALL) {
					out("%d,%d blocked, moved %d %s", x, y, i, loc.heading());
					break;
				}
				loc = new Location(x, y, loc.heading());
			}
			return loc;
		}

		private int firstX(final Grid<Tile> grid, final int y) {
			for (int x = 0; true; x++) {
				final Tile tile = grid.get(x, y);
				if (tile != AIR) {
					return x;
				}
			}
		}

		private int lastX(final Grid<Tile> grid, final int y) {
			for (int x = grid.getWidth() - 1; true; x--) {
				final Tile tile = grid.get(x, y);
				if (tile != AIR) {
					return x;
				}
			}
		}

		private int firstY(final Grid<Tile> grid, final int x) {
			for (int y = 0; true; y++) {
				final Tile tile = grid.get(x, y);
				if (tile != AIR) {
					return y;
				}
			}
		}
		
		private int lastY(final Grid<Tile> grid, final int x) {
			for (int y = grid.getHeight() - 1; true; y--) {
				final Tile tile = grid.get(x, y);
				if (tile != AIR) {
					return y;
				}
			}
		}

		@Override
		public String toString() {
			return "" + distance;
		}
	}
	
	static class LeftInstruction implements Instruction {
		@Override
		public Location process(final Grid<Tile> grid, final Location location) {
			return new Location(location.x(), location.y(), location.heading().left());
		}
		
		@Override
		public String toString() {
			return "L";
		}
	}

	static class RightInstruction implements Instruction {
		@Override
		public Location process(final Grid<Tile> grid, final Location location) {
			return new Location(location.x(), location.y(), location.heading().right());
		}
		
		@Override
		public String toString() {
			return "R";
		}
	}
	
	static enum Heading {
		EAST(0, 1, 0),
		SOUTH(1, 0, 1),
		WEST(2, -1, 0),
		NORTH(3, 0, -1);
		
		private final int value;
		private final int xInc;
		private final int yInc;
		
		private Heading left;
		private Heading right;

		Heading(int value, int xInc, int yInc) {
			this.value = value;
			this.xInc = xInc;
			this.yInc = yInc;
		}
		
		int value() {
			return value;
		}
		
		void setAdjacentDirections(final Heading left, final Heading right) {
			this.left = left;
			this.right = right;
		}
		
		Heading left() {
			return left;
		}
		
		Heading right() {
			return right;
		}
	}
}

// 103292 too high