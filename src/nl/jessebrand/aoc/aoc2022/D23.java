package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.aoc2022.D23.Direction.EAST;
import static nl.jessebrand.aoc.aoc2022.D23.Direction.NORTH;
import static nl.jessebrand.aoc.aoc2022.D23.Direction.SOUTH;
import static nl.jessebrand.aoc.aoc2022.D23.Direction.WEST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.jessebrand.aoc.Point;

public class D23 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2022/d23");
		
		List<Elf> elves = parseElves(lines);
		out("Elves: %s", elves);
		
		List<Direction> directions = new ArrayList<>();
		directions.add(NORTH);
		directions.add(SOUTH);
		directions.add(WEST);
		directions.add(EAST);

		out("Start");
		outputState(elves);

		int roundCount = 0;
		while (true) {
			final Map<Point, List<Elf>> potentialPoints = new HashMap<>();
			for (Elf elf : elves) {
				if (!hasAdjacent(elf, elves)) {
					continue;
				}
				Point targetPoint = findTargetPoint(elf, elves, directions);
				if (targetPoint != null) {
					elf.setTargetPoint(targetPoint);
					if (!potentialPoints.containsKey(targetPoint)) {
						potentialPoints.put(targetPoint, new ArrayList<Elf>());
					}
					potentialPoints.get(targetPoint).add(elf);
//					out("Added %s to target point %s", elf, targetPoint);
				}
			}
			if (potentialPoints.isEmpty()) {
				out("Stable after %d rounds", roundCount);
				outputState(elves);
				break;
			}
			int movesDone = 0;
			for (Elf elf : elves) {
				final Point targetPoint = elf.getTargetPoint();
				elf.setTargetPoint(null);
				if (targetPoint != null) {
					final List<Elf> elvesToPoint = potentialPoints.get(targetPoint);
					if (elvesToPoint == null) {
						throw new IllegalStateException(String.format("No elves for point %s targeted by %s", targetPoint, elf));
					}
					if (elvesToPoint.size() > 1) {
//						out("%s not moving, would move to %s but multiple have that target: %s", elf, targetPoint, elvesToPoint);
						continue;
					}
					elf.setLocation(targetPoint);
					movesDone++;
				}
			}
			out("Round %d: %d moves done", roundCount + 1, movesDone);
			outputState(elves);
			shiftDirections(directions);
//			if (roundCount == 9) {
//				break;
//			}
			roundCount++;
		}
	}
	
	
	private static void outputState(List<Elf> elves) {
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		for (final Elf elf : elves) {
			minX = Math.min(minX, elf.x());
			maxX = Math.max(maxX, elf.x());
			minY = Math.min(minY, elf.y());
			maxY = Math.max(maxY, elf.y());
		}
		int width = maxX - minX + 1;
		int height = maxY - minY + 1;
		out("Field from x = %d to %d ; y = %d to %d ; total size = %d (%dx%d); elves = %d ; empty tiles = %d", minX, maxX, minY, maxY, width * height, width, height, elves.size(), width * height - elves.size());
//		for (int y = minY; y <= maxY; y++) {
//			for (int x = minX; x <= maxX; x++) {
//				System.out.print(isEmpty(new Point(x, y), elves) ? '.' : '#');
//			}
//			System.out.println();
//		}
	}


	private static Point findTargetPoint(final Elf elf, final List<Elf> elves, final List<Direction> directions) {
//		out("Checking directions %s", directions);
		Point targetPoint = null;
		for (Direction direction : directions) {
			if (targetPoint == null) {
				targetPoint = findEmptyPoint(direction, elf.getLocation(), elves);
			}
		}
		return targetPoint;
	}


	private static Point findEmptyPoint(Direction direction, Point location, List<Elf> elves) {
		switch (direction) {
		case NORTH:
			return isEmpty(location.add(-1, -1), elves) && isEmpty(location.add(0, -1), elves) && isEmpty(location.add(1, -1), elves) ? location.add(0, -1) : null;
		case EAST:
			return isEmpty(location.add(1, -1), elves) && isEmpty(location.add(1, 0), elves) && isEmpty(location.add(1, 1), elves) ? location.add(1, 0) : null;
		case SOUTH:
			return isEmpty(location.add(-1, 1), elves) && isEmpty(location.add(0, 1), elves) && isEmpty(location.add(1, 1), elves) ? location.add(0, 1) : null;
		case WEST:
			return isEmpty(location.add(-1, -1), elves) && isEmpty(location.add(-1, 0), elves) && isEmpty(location.add(-1, 1), elves) ? location.add(-1, 0) : null;
		}
		throw new IllegalStateException();
	}


	private static boolean isEmpty(Point point, List<Elf> elves) {
		for (Elf elf : elves) {
			if (elf.getLocation().equals(point)) {
				return false;
			}
		}
		return true;
	}


	private static boolean hasAdjacent(Elf elf, List<Elf> elves) {
		for (Elf e2 : elves) {
			if (elf != e2
					&& e2.x() >= elf.x() - 1 && e2.x() <= elf.x() + 1
					&& e2.y() >= elf.y() - 1 && e2.y() <= elf.y() + 1) {
				return true;
			}
		}
		return false;
	}


	private static List<Elf> parseElves(final List<String> lines) {
		List<Elf> elves = new ArrayList<>();
		for (int y = 0; y < lines.size(); y++) {
			final String line = lines.get(y);
			for (int x = 0; x < line.length(); x++) {
				if (line.charAt(x) == '#') {
					elves.add(new Elf(x, y));
				}
			}
		}
		return elves;
	}


	private static void shiftDirections(List<Direction> directions) {
		Direction dir = directions.remove(0);
		directions.add(dir);
	}

	static class Elf {

		private Point location;
		private Point targetPoint;

		Elf(int x, int y) {
			this(new Point(x, y));
		}
	
		Elf(Point location) {
			this.location = location;
		}

		public Point getLocation() {
			return location;
		}
		
		int x() {
			return location.x();
		}

		int y() {
			return location.y();
		}

		void setLocation(Point loc) {
			this.location = loc;
		}

		Point getTargetPoint() {
			return targetPoint;
		}

		void setTargetPoint(Point targetPoint) {
			this.targetPoint = targetPoint;
		}
		
		@Override
		public String toString() {
			return String.format("Elf[%d,%d]", x(), y());
		}
	}

	static enum Direction {
		NORTH,
		EAST,
		SOUTH,
		WEST;
	}
}

// 3646 too low
// 4138 too high
// 4056