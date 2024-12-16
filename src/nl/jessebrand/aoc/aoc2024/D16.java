package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.applyDirection;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nl.jessebrand.aoc.Direction;
import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;

public class D16 {
	
	private record Path(int value, List<Point> points) implements Comparable<Path> {
		
		Path(final int value, final Point p) {
			this(value, new ArrayList<>());
			points().add(p);
		}

		Path(final int value, final Path prev, final Point p) {
			this(value, new ArrayList<>(prev.points()));
			points().add(p);
		}

		@Override
		public int compareTo(Path o) {
			return Integer.compare(value(), o.value());
		}
	}
	
	private static class GridSquare {
		private final boolean wall;
		private final Map<Direction, List<Path>> paths = new HashMap<>();

		private GridSquare(final boolean wall) {
			this.wall = wall;
		}
		
		public boolean isWall() {
			return wall;
		}
		
		public List<Path> getStoredPaths(final Direction dir) {
			return paths.get(dir);
		}

		public void clearPaths(Direction dir) {
			if (paths.containsKey(dir)) {
				paths.get(dir).clear();
			}
		}
		
		public void addPath(final Direction dir, final Path path) {
			if (!paths.containsKey(dir)) {
				paths.put(dir, new ArrayList<Path>());
			}
			paths.get(dir).add(path);
		}
		
		public List<Path> allPaths() {
			return paths.values().stream().flatMap(Collection::stream).sorted().toList();
		}
		
		@Override
		public String toString() {
			return String.format("%s[wall=%b, paths=%s]", getClass().getSimpleName(), isWall(), paths);
		}
	}
	
	private static record Input(Grid<GridSquare> grid, Point start, Point end) {}
	
	private static record Attempt(Point p, Direction dir) {}

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d16");
		final Input input = buildGrid(lines);
		final List<Path> solutions = findSolution(input, Direction.EAST, 1, 1000);
		out("1: %d", solutions.get(0).value());
		out("2: %d", solutions.stream().map(Path::points).flatMap(Collection::stream).collect(Collectors.toSet()).size());
	}
	
	private static List<Path> findSolution(final Input input, final Direction startDir, final int incMove, final int incTurn) {
		final Point start = input.start();
		final Grid<GridSquare> grid = input.grid();
		grid.get(input.start()).addPath(startDir, new Path(0, start));
		
		final List<Attempt> attempts = new LinkedList<>();
		attempts.add(new Attempt(start, startDir));

		while (!attempts.isEmpty()) {
			final Attempt attempt = attempts.remove(0);
//			out(attempt);
			final Point p = attempt.p();
			final Direction dir = attempt.dir();
			final List<Path> curPaths = grid.get(p).getStoredPaths(dir); // all same length

			attempts.addAll(attemptDir(grid, p, curPaths, dir, incMove));
			attempts.addAll(attemptDir(grid, p, curPaths, dir.rotateLeft(), incMove + incTurn));
			attempts.addAll(attemptDir(grid, p, curPaths, dir.rotateRight(), incMove + incTurn));
		}
		out(grid.get(input.end()).allPaths());
		final List<Path> paths = new ArrayList<>(grid.get(input.end()).allPaths());
		int lowest = paths.get(0).value();
		return paths.stream().filter(p -> p.value() == lowest).toList();
	}

	private static List<Attempt> attemptDir(final Grid<GridSquare> grid, final Point p, final List<Path> curPaths, final Direction dir, final int valueInc) {
		final List<Attempt> attempts = new ArrayList<>();

		final int curLength = curPaths.get(0).value();
		final Point newP = applyDirection(p, dir);
		
		if (!grid.get(newP).isWall()) {
			final int newVal = curLength + valueInc;
			final List<Path> storedPaths = grid.get(newP).getStoredPaths(dir);
			if (storedPaths == null || newVal <= storedPaths.get(0).value()) {
				if (storedPaths == null || newVal < storedPaths.get(0).value()) {
					grid.get(newP).clearPaths(dir);
					attempts.add(new Attempt(newP, dir));
				}
				for (final Path curPath : curPaths) {
					grid.get(newP).addPath(dir, new Path(newVal, curPath, newP));
				}
			}
		}
		return attempts;
	}

	private static Input buildGrid(final List<String> lines) {
		final int height = lines.size();
		final int width = lines.get(0).length();
		Point start = null;
		Point end = null;
		final Grid<GridSquare> grid = new Grid<>(width, height);
		for (int y = 0; y < height; y++) {
			final String line = lines.get(y);
			for (int x = 0; x < width; x++) {
				char c = line.charAt(x);
				grid.set(x, y, new GridSquare(c == '#'));
				if (c == 'S') {
					start = new Point(x, y);
				}
				if (c == 'E') {
					end = new Point(x, y);
				}
			}
		}
		return new Input(grid, start, end);
	}
		
}

// 7036
