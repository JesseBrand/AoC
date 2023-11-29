package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.assertEquals;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.aoc2022.D22b.Heading.EAST;
import static nl.jessebrand.aoc.aoc2022.D22b.Heading.NORTH;
import static nl.jessebrand.aoc.aoc2022.D22b.Heading.SOUTH;
import static nl.jessebrand.aoc.aoc2022.D22b.Heading.WEST;
import static nl.jessebrand.aoc.aoc2022.D22b.Tile.AIR;
import static nl.jessebrand.aoc.aoc2022.D22b.Tile.WALL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;

public class D22b {

	private static final int FACE_SIZE = 50;
	
	private static final Instruction LEFT = new LeftInstruction();
	private static final Instruction RIGHT = new RightInstruction();

	public static void main(String[] args) throws IOException {
		initHeadings();
		runTests();
//		if (true) return;
		final List<String> lines = readFile("2022/d22");
		final Grid<FaceTile> grid = parseGrid(lines.subList(0, lines.size() - 2));
		final Grid<Character> debugGrid = setupDebugGrid(grid);
		final List<Instruction> instructions = parseInstructions(lines.get(lines.size() - 1));
		out("Instructions: %s", instructions);
		
		Location location = determineStart(grid);
		out("Start: %s", location);
		for (Instruction instruction : instructions) {
			out(instruction);
			location = instruction.process(grid, debugGrid, location);
			out(location);
		}
		debugGrid.set(location.x(), location.y(), 'X');
//		out(debugGrid);
		out("After %d moves ended up at %d,%d heading %s, result=%d", instructions.size(), location.x(), location.y(), location.heading(), 1000 * (location.y() + 1) + 4 * (location.x() + 1) + location.heading().value());
	}

	private static Grid<Character> setupDebugGrid(final Grid<FaceTile> grid) {
		final Grid<Character> result = new Grid<>(grid.getWidth(), grid.getHeight(), "");
		for (int y = 0; y < grid.getHeight(); y++) {
			for (int x = 0; x < grid.getWidth(); x++) {
				result.set(x, y, grid.get(x, y).type().c);
			}
		}
		return result;
	}
	
	private static void runTests() {
		assertEquals(new Location(0, 160, EAST), determineNewLocation(Face._0, 60, -1, NORTH));
		assertEquals(new Location(100, 10, EAST), determineNewLocation(Face._0, 100, 10, EAST));
		assertEquals(new Location(60, 50, SOUTH), determineNewLocation(Face._0, 60, 50, SOUTH));
//		assertEquals(new Location(0, 140, EAST), determineNewLocation(Face._0, 49, 10, WEST));
		assertEquals(new Location(0, 149, EAST), determineNewLocation(Face._0, 49, 0, WEST));

		assertEquals(new Location(10, 199, NORTH), determineNewLocation(Face._1, 110, -1, NORTH));
//		assertEquals(new Location(99, 140, WEST), determineNewLocation(Face._1, 150, 10, EAST));
		assertEquals(new Location(99, 149, WEST), determineNewLocation(Face._1, 150, 0, EAST));
		assertEquals(new Location(99, 60, WEST), determineNewLocation(Face._1, 110, 50, SOUTH));
		assertEquals(new Location(99, 10, WEST), determineNewLocation(Face._1, 99, 10, WEST));

		assertEquals(new Location(60, 49, NORTH), determineNewLocation(Face._2, 60, 49, NORTH));
		assertEquals(new Location(110, 49, NORTH), determineNewLocation(Face._2, 100, 60, EAST));
		assertEquals(new Location(60, 100, SOUTH), determineNewLocation(Face._2, 60, 100, SOUTH));
		assertEquals(new Location(10, 100, SOUTH), determineNewLocation(Face._2, 49, 60, WEST));
		
		assertEquals(new Location(60, 99, NORTH), determineNewLocation(Face._3, 60, 99, NORTH));
//		assertEquals(new Location(140, 49, WEST), determineNewLocation(Face._3, 100, 110, EAST));
		assertEquals(new Location(149, 49, WEST), determineNewLocation(Face._3, 100, 100, EAST));
		assertEquals(new Location(49, 160, WEST), determineNewLocation(Face._3, 60, 150, SOUTH));
		assertEquals(new Location(49, 110, WEST), determineNewLocation(Face._3, 49, 110, WEST));
		
		assertEquals(new Location(50, 60, EAST), determineNewLocation(Face._4, 10, 99, NORTH));
		assertEquals(new Location(50, 110, EAST), determineNewLocation(Face._4, 50, 110, EAST));
		assertEquals(new Location(10, 150, SOUTH), determineNewLocation(Face._4, 10, 150, SOUTH));
//		assertEquals(new Location(50, 40, EAST), determineNewLocation(Face._4, -1, 110, WEST));
		assertEquals(new Location(50, 49, EAST), determineNewLocation(Face._4, -1, 100, WEST));
		
		assertEquals(new Location(10, 149, NORTH), determineNewLocation(Face._5, 10, 149, NORTH));
		assertEquals(new Location(60, 149, NORTH), determineNewLocation(Face._5, 50, 160, EAST));
		assertEquals(new Location(110, 0, SOUTH), determineNewLocation(Face._5, 10, 200, SOUTH));
		assertEquals(new Location(60, 0, SOUTH), determineNewLocation(Face._5, -1, 160, WEST));
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

	private static Location determineStart(Grid<FaceTile> grid) {
		for (int x = 0; x < grid.getWidth(); x++) {
			FaceTile tile = grid.get(x, 0);
			if (tile.type() != AIR) {
				return new Location(x, 0, EAST);
			}
		}
		throw new IllegalStateException();
	}

	private static Grid<FaceTile> parseGrid(final List<String> lines) {
		int height = lines.size();
		int width = lines.get(0).length();
		Grid<FaceTile> result = new Grid<>(width, height, "");
		for (int y = 0; y < height; y++) {
			String line = lines.get(y);
			for (int x = 0; x < width; x++) {
				Tile tile = x >= line.length() ? AIR : Tile.parse(line.charAt(x));
				result.set(x, y, new FaceTile(tile == AIR ? null : Face.determineFace(x, y), tile));
			}
		}
		return result;
	}

	private static Location determineNewLocation(Face face, int x, int y, Heading heading) {
		switch (face) {
		case _0:
			switch(heading) {
			case EAST: // 1W
			case SOUTH: // 2N
				return new Location(x, y, heading);
			case WEST: // 4W
				return new Location(Face._4.startX, Face._4.endY - y, heading.invert());
			case NORTH: // 5W
				return new Location(Face._5.startX, x - Face._0.startX + Face._5.startY, heading.right());
			}
		case _1:
			switch(heading) {
			case EAST: // 3E
				return new Location(Face._3.endX, Face._3.endY - y, heading.invert());
			case SOUTH: // 2E
				return new Location(Face._2.endX, x - Face._1.startX + Face._2.startY, heading.right());
			case WEST: // 0E
				return new Location(x, y, heading);
			case NORTH: // 5S
				return new Location(x - Face._1.startX, Face._5.endY, heading);
			}
		case _2:
			switch(heading) {
			case EAST: // 1S
				return new Location(Face._1.startX + y - Face._2.startY, Face._1.endY, heading.left());
			case SOUTH: // 3N
			case NORTH: // 0S
				return new Location(x, y, heading);
			case WEST: // 4N
				return new Location(y - Face._2.startY, Face._4.startY, heading.left());
			}		
		case _3:
			switch(heading) {
			case EAST: // 1E
				return new Location(Face._1.endX, Face._1.endY - (y - Face._3.startY), heading.invert());
			case NORTH: // 2S
			case WEST: // 4E
				return new Location(x, y, heading);
			case SOUTH: // 5E
				return new Location(Face._5.endX, x - Face._3.startX + Face._5.startY, heading.right());
			}
		case _4:
			switch(heading) {
			case EAST: // 1E
			case SOUTH: // 5N
				return new Location(x, y, heading);
			case WEST: // 0W
				return new Location(Face._0.startX, Face._0.endY - (y - Face._4.startY), heading.invert());
			case NORTH: // 2W
				return new Location(Face._2.startX, Face._2.startY + x, heading.right());
			}
		case _5:
			switch(heading) {
			case EAST: // 3S
				return new Location(y - Face._5.startY + Face._3.startX, Face._3.endY, heading.left());
			case SOUTH: // 1N
				return new Location(x + Face._1.startX, 0, heading);
			case WEST: // 0N
				return new Location(y - Face._5.startY + Face._0.startX, 0, heading.left());
			case NORTH: // 4S
				return new Location(x, y, heading);
			}
		}
		throw new IllegalStateException();
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
	
	static record FaceTile(Face face, Tile type) {}
	
	static enum Face {
		_0(1, 0),
		_1(2, 0),
		_2(1, 1),
		_3(1, 2),
		_4(0, 2),
		_5(0, 3);

		private final int startX;
		private final int startY;
		private final int endX;
		private final int endY;

		Face(int xPos, int yPos) {
			startX = xPos * FACE_SIZE;
			startY = yPos * FACE_SIZE;
			endX = startX + FACE_SIZE - 1;
			endY = startY + FACE_SIZE - 1;
		}
		
		static Face determineFace(int x, int y) {
			for (Face face : values()) {
				if (x >= face.startX && x < face.startX + FACE_SIZE
						&& y >= face.startY && y < face.startY + FACE_SIZE) {
					return face;
				}
			}
			throw new IllegalStateException(String.format("Cannot locate face for %d,%d", x, y));
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
		Heading.EAST.setAdjacentDirections(NORTH, SOUTH, WEST);
		Heading.SOUTH.setAdjacentDirections(EAST, WEST, NORTH);
		Heading.WEST.setAdjacentDirections(SOUTH, NORTH, EAST);
		Heading.NORTH.setAdjacentDirections(WEST, EAST, SOUTH);
	}
	
	static interface Instruction {
		Location process(Grid<FaceTile> grid, Grid<Character> debugGrid, Location location);
	}
	
	static class MoveInstruction implements Instruction {
		
		private final int distance;

		public MoveInstruction(final int distance) {
			this.distance = distance;
		}
		
		@Override
		public Location process(final Grid<FaceTile> grid, final Grid<Character> debugGrid, final Location location) {
			Location loc = location;
			for (int i = 0; i < distance; i++) {
				debugGrid.set(loc.x(), loc.y(), loc.heading().c);
				Face face = grid.get(loc.x(), loc.y()).face();
				int y = loc.y() + loc.heading().yInc;
				int x = loc.x() + loc.heading().xInc;
				Location newLoc;
				if (y < 0 || y >= grid.getHeight() || x < 0 || x >= grid.getWidth() || grid.get(x, y).type() == AIR || face != Face.determineFace(x, y)) {
					newLoc = determineNewLocation(face, x, y, loc.heading());
//					out("new Location for (%s heading %s %d,%d) -> (heading %s %d,%d)", face, loc.heading(), x, y, newLoc.heading(), newLoc.x(), newLoc.y());
					final Face newFace = grid.get(newLoc.x(), newLoc.y()).face();
					out("new Location for (%s heading %s %d,%d) -> (%s heading %s %d,%d)", face, loc.heading(), x, y, newFace, newLoc.heading(), newLoc.x(), newLoc.y());
					if (face == newFace) {
						throw new IllegalStateException("Same face!");
					}
				} else {
					newLoc = new Location(x, y, loc.heading());
				}
				x = newLoc.x();
				y = newLoc.y();
				if (grid.get(x, y).type() == WALL) {
					out("%d,%d blocked, moved %d %s", x, y, i, loc.heading());
					break;
				}
				loc = newLoc;
			}
			return loc;
		}

		@Override
		public String toString() {
			return "" + distance;
		}
	}
	
	static class LeftInstruction implements Instruction {
		@Override
		public Location process(final Grid<FaceTile> grid, final Grid<Character> debugGrid, final Location location) {
			return new Location(location.x(), location.y(), location.heading().left());
		}
		
		@Override
		public String toString() {
			return "L";
		}
	}

	static class RightInstruction implements Instruction {
		@Override
		public Location process(final Grid<FaceTile> grid, final Grid<Character> debugGrid, final Location location) {
			return new Location(location.x(), location.y(), location.heading().right());
		}
		
		@Override
		public String toString() {
			return "R";
		}
	}
	
	static enum Heading {
		EAST(0, 1, 0, '>'),
		SOUTH(1, 0, 1, 'V'),
		WEST(2, -1, 0, '<'),
		NORTH(3, 0, -1, '^');
		
		private final int value;
		private final int xInc;
		private final int yInc;
		private final char c;
		
		private Heading left;
		private Heading right;
		private Heading inverted;

		Heading(int value, int xInc, int yInc, char c) {
			this.value = value;
			this.xInc = xInc;
			this.yInc = yInc;
			this.c = c;
		}
		
		int value() {
			return value;
		}
		
		void setAdjacentDirections(final Heading left, final Heading right, final Heading inverted) {
			this.left = left;
			this.right = right;
			this.inverted = inverted;
		}
		
		Heading left() {
			return left;
		}
		
		Heading right() {
			return right;
		}

		Heading invert() {
			return inverted;
		}
	}
}

// 193175 too high
// 156166