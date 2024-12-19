package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.applyDirection;
import static nl.jessebrand.aoc.Utils.buildCharGrid;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import nl.jessebrand.aoc.Direction;
import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;
import nl.jessebrand.aoc.Utils;

public class D12 {

	private static final char C0 = (char) 0;

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d12");
		final Grid<Character> grid = buildCharGrid(lines);

		final Map<Integer, Collection<Point>> areas = groupRegions(grid);
		long total1 = 0;
		long total2 = 0;
		for (int i : areas.keySet()) {
			final Collection<Point> list = areas.get(i);
			long perimeter1 = list.stream().map(Utils::getNeighbours).flatMap(Collection::stream).filter(p -> !list.contains(p)).count();
			long perimeter2 = countSides(list);
			out("%d(%c): %d, %d/%d (%s)", i, grid.get(list.iterator().next()), list.size(), perimeter1, perimeter2, list);
			total1 += areas.get(i).size() * perimeter1;
			total2 += areas.get(i).size() * perimeter2;
		}
		out("1: %d", total1);
		out("2: %d", total2);
	}
	
	private static record DirToConsider(Point p, Direction dir) {}

	private static long countSides(final Collection<Point> points) {
		final List<DirToConsider> considerList = new LinkedList<>();
		for (final Point p : points) {
			for (Direction dir : Direction.values()) {
				if (points.contains(applyDirection(p, dir))) {
					continue;
				}
				considerList.add(new DirToConsider(p, dir));
			}
		}
		int result = 0;
		while (!considerList.isEmpty()) {
			final DirToConsider tc = considerList.get(0);
			DirToConsider tcp = tc;
			final Direction dir = tcp.dir() == Direction.NORTH || tcp.dir() == Direction.SOUTH ? Direction.EAST : Direction.SOUTH;
			while (considerList.contains(tcp)) {
				considerList.remove(tcp);
				tcp = new DirToConsider(applyDirection(tcp.p(), dir), tcp.dir());
			}
//			out("%s from %s to %s", tc.dir(), tc.p(), applyDirectionInverse(tcp.p(), dir));
			result++;
		}
		return result;
	}

	private static Map<Integer, Collection<Point>> groupRegions(final Grid<Character> grid) {
		final Map<Integer, Collection<Point>> areas = new LinkedHashMap<>();
		int nextIndex = 0;
		for (int y = 0; y < grid.getHeight(); y++) {
			for (int x = 0; x < grid.getWidth(); x++) {
				final Point p = new Point(x, y);
				final char c = grid.get(p);
				int i = findInMap(p, areas);
//				out("%c[%d,%d]: %d", c, x, y, i);
				boolean isNew = false;
				if (i == -1) {
					i = nextIndex;
					areas.put(i, new TreeSet<>());
					areas.get(i).add(p);
					isNew = true;
					nextIndex++;
				}
				final Point right = new Point(x + 1, y);
				if (grid.getOr(right, C0) == c) {
					final int rightI = findInMap(right, areas);
					if (i != rightI) {
						if (rightI != -1) {
							// right already in a region (from above)
							areas.get(rightI).addAll(areas.get(i));
							areas.remove(i);
//							out("(%c) merged %d into %d", c, i, rightI);
							i = rightI;
							if (isNew) {
								nextIndex--;
							}
						} else if (grid.contains(right)) {
							areas.get(i).add(right);
						}
					}
				}
				final Point below = new Point(x, y + 1);
				if (grid.getOr(below, C0) == c) {
					areas.get(i).add(below);
				}
				
			}
		}
		return areas;
	}

	private static int findInMap(final Point p, final Map<Integer, Collection<Point>> result) {
		return result.entrySet().stream().filter(e -> e.getValue().contains(p)).map(e -> e.getKey()).findFirst().orElse(-1);
	}
}
