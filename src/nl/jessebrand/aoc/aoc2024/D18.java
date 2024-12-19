package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.getNeighbours;
import static nl.jessebrand.aoc.Utils.manhDistance;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.parsePoints;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.Utils.visualize;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;

public class D18 {

	private static final int SCALE = 10;
	
	private static class AStarComparator implements Comparator<Path> {

		private final Point target;

		public AStarComparator(final Point target) {
			this.target = target;
		}
		
		@Override
		public int compare(final Path p1, final Path p2) {
			int mh1 = p1.length() + manhDistance(p1.last(), target);
			int mh2 = p2.length() + manhDistance(p2.last(), target);
			if (mh1 != mh2) {
				return Integer.compare(mh1, mh2);
			}
			return p1.last().compareTo(p2.last());
		}
		
	}

	public static record Path(List<Point> points) implements Iterable<Point> {
		public Path(final Point p) {
			this(Arrays.asList(p));
		}

		public Path(final Path parentPath, final Point p) {
			this(Stream.concat(parentPath.points().stream(), Stream.of(p)).toList());
		}

		public final Point first() {
			return points().get(0);
		}

		public final Point last() {
			return points().get(length() - 1);
		}

		public final int length() {
			return points().size();
		}

		@Override
		public final Iterator<Point> iterator() {
			return points().iterator();
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		solve("2024/d18ex", 7, 12);
		solve("2024/d18", 71, 1024);
	}
	
	private static void solve(final String file, final int gridSize, final int limit) throws IOException {
		final List<String> lines = readFile(file);
		final Grid<Boolean> grid = new Grid<>(gridSize, gridSize, false);
		final List<Point> points = parsePoints(lines, ",");
		solve1(grid, points, limit, file);
		solve2(grid, points, file);
	}

	private static void solve1(final Grid<Boolean> grid, final List<Point> points, final int limit, final String title) {
		final Point start = new Point(0, 0);
		final Point end = new Point(grid.getWidth() - 1, grid.getHeight() - 1);
		points.stream().limit(limit).forEach(p -> grid.set(p, true));
//		out(grid);
		final Path path = solveAStar(grid, start, end);
		out("1: %d", path.length() - 1);
		final Renderer renderer = new Renderer(grid, path, SCALE);
		visualize(title, grid.getWidth() * SCALE, grid.getHeight() * SCALE, renderer::render);
	}

	private static void solve2(final Grid<Boolean> grid, final List<Point> points, final String title) {
		final Point start = new Point(0, 0);
		final Point end = new Point(grid.getWidth() - 1, grid.getHeight() - 1);
		for (final Point p : points) {
			grid.set(p, true);
			final Path path = solveAStar(grid, start, end);
			if (path == null) {
				out("2: %s", p);
				return;
			}
		}
		throw new IllegalStateException();
	}
	
	private static class Renderer {

		private Grid<Boolean> grid;
		private Path path;
		private int scale;

		public Renderer(Grid<Boolean> grid, Path path, int scale) {
			this.grid = grid;
			this.path = path;
			this.scale = scale;
		}

		public void render(Graphics g) {
			for (final Point p : grid) {
				if (grid.get(p)) {
					g.setColor(Color.BLACK);
				} else {
					g.setColor(Color.WHITE);
				}
				g.fillRect(p.x() * scale, p.y() * scale, scale, scale);
			}
			for (final Point p : path) {
				g.setColor(Color.YELLOW);
				g.fillOval(p.x() * scale, p.y() * scale, scale, scale);
				g.setColor(Color.BLACK);
				g.drawOval(p.x() * scale, p.y() * scale, scale, scale);
			}
		}
		
	}

	private static Path solveAStar(Grid<Boolean> grid, Point start, Point end) {
		final Set<Path> next = new TreeSet<>(new AStarComparator(end));
		final Map<Point, Path> register = new HashMap<>();
		final Path startPath = new Path(start);
		next.add(startPath);
		register.put(start, startPath);
		while (!next.isEmpty()) {
			final Path evalPath = next.iterator().next();
			final Point evalPoint = evalPath.last();
//			out("eval %s", evalPoint);
			if (evalPoint.equals(end)) {
				return evalPath;
			}
			getNeighbours(evalPoint).stream().filter(grid::contains).filter(p -> !grid.get(p)).filter(p -> !register.containsKey(p)).forEach(p -> {
				final Path nextPath = new Path(evalPath, p);
				next.add(nextPath);
				register.put(p, nextPath);
			});
			next.remove(evalPath);
		}
		return null;
	}
}

// 340 too high