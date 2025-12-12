package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;

public class D12True {

	public static void main(final String[] args) throws IOException {
//		solve("2025/d12ex");
		solve("2025/d12");
	}

	private static void solve(final String file) throws IOException {
		final List<String> lines = readFile(file);
//		out(lines);
		final List<Present> presents = IntStream.range(0, 6).mapToObj(i -> toPresent(i, lines)).toList();
		out(presents);
		final List<Area> areas = lines.subList(30, lines.size()).stream().map(D12True::toArea).toList();
		out(areas);
		

		out("Part 1: %d", areas.stream().filter(a -> canFit(a, presents)).count());
//		out("Part 2: %d", totalB);
		out();
	}

	private static boolean canFit(final Area a, final List<Present> presents) {
		out(a);
		final List<Present> list = buildList(a, presents);
		final Grid<Boolean> grid = new Grid<>(a.width(), a.height(), false);
//		final boolean result = canFit(grid, list);
		final boolean result = canFit(a.width(), a.height(), Collections.emptyList(), Collections.emptyList(), list);
		out(result);
		return result;
	}

	private static List<Present> buildList(final Area a, final List<Present> presents) {
		final List<Present> result = new ArrayList<>();
		for (int i = 0; i < a.expectedCount().length; i++) {
			for (int j = 0; j < a.expectedCount()[i]; j++) {
				result.add(presents.get(i));
			}
		}
		return result;
	}

	private static boolean canFit(final int width, final int height, final List<Grid<Boolean>> existing, final List<Point> locations, final List<Present> remaining) {
		final List<Present> next = remaining.subList(1, remaining.size());
		for (Present p : remaining) {
			for (Grid<Boolean> variant : p.variants()) {
				for (int x = 0; x < width - 2; x++) {
					for (int y = 0; y < height - 2; y++) {
//						out("try %d at %d,%d (%d left)", p.i(), x, y, next.size());
						if (!overlapsAny(existing, locations, variant, x, y)) {
							if (next.isEmpty()) {
								return true;
							}
							final List<Grid<Boolean>> newExisting = new ArrayList<>(existing);
							newExisting.add(variant);
							final List<Point> newLocations = new ArrayList<>(locations);
							newLocations.add(new Point(x, y));
							if (canFit(width, height, newExisting, newLocations, next)) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private static boolean overlapsAny(List<Grid<Boolean>> existing, List<Point> locations, Grid<Boolean> newGrid, int xOffset, int yOffset) {
		for (int i = 0; i < existing.size(); i++) {
			if (overlaps(existing.get(i), locations.get(i).x(), locations.get(i).y(), newGrid, xOffset, yOffset)) {
				return true;
			}
		}
		return false;
	}

	private static boolean overlaps(Grid<Boolean> grid1, int xOffset1, int yOffset1, Grid<Boolean> grid2, int xOffset2, int yOffset2) {
		for (int y = 0; y < grid2.getHeight(); y++) {
			for (int x = 0; x < grid2.getWidth(); x++) {
				if (grid1.getOr(x + xOffset2 - xOffset1, y + yOffset2 - yOffset1, false)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean canFit(final Grid<Boolean> origGrid, final List<Present> remaining) {
		final List<Present> next = remaining.subList(1, remaining.size());
		for (Grid<Boolean> variant : remaining.get(0).variants()) {
			for (int x = 0; x < origGrid.getWidth() - 2; x++) {
				for (int y = 0; y < origGrid.getHeight() - 2; y++) {
//					out("try %d at %d,%d (%d left)", p.i(), x, y, next.size());
					if (fits(origGrid, variant, x, y)) {
						final Grid<Boolean> newGrid = origGrid.clone();
//						out("Present %d fits at %d,%d", p.i, x, y);
						stamp(newGrid, variant, x, y);
//						out(newGrid);
						if (next.isEmpty()) {
							return true;
						}
						if (canFit(newGrid, next)) {
							return true;
						}
					} else {
//						out("Present %d does not fit at %d,%d", p.i, x, y);
					}
				}
			}
		}
		return false;
	}

	private static boolean fits(final Grid<Boolean> grid, final Grid<Boolean> present, final int xOffset, final int yOffset) {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (present.get(x, y) && grid.get(x + xOffset, y + yOffset)) {
					return false;
				}
			}
		}
		return true;
	}

	private static void stamp(Grid<Boolean> grid, Grid<Boolean> present, int xOffset, int yOffset) {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (present.get(x, y)) {
					grid.set(x + xOffset, y + yOffset, true);
				}
			}
		}
	}

	private static Present toPresent(int i, List<String> lines) {
		final List<String> sublist = lines.subList(i * 5 + 1, i * 5 + 4);
		final Grid<Boolean> grid = buildBooleanGrid(sublist, '#');
		return new Present(i, gatherVariants(grid));
	}

	private static Set<Grid<Boolean>> gatherVariants(Grid<Boolean> grid) {
		final Set<Grid<Boolean>> result = new HashSet<>();
		addUnique(result, grid);
		addUnique(result, grid.flipX());
		final Grid<Boolean> r90 = grid.rotateClockwise();
		addUnique(result, r90);
		addUnique(result, r90.flipX());
		final Grid<Boolean> r180 = r90.rotateClockwise();
		addUnique(result, r180);
		addUnique(result, r180.flipX());
		final Grid<Boolean> r270 = r180.rotateClockwise();
		addUnique(result, r270);
		addUnique(result, r270.flipX());
		return result;
	}

	private static void addUnique(Collection<Grid<Boolean>> result, Grid<Boolean> grid) {
		for (Grid<Boolean> e : result) {
			if (e.equals(grid)) {
				return;
			}
		}
		result.add(grid);
	}

	private static Area toArea(final String line) {
		final String[] split = line.split(":");
		final String[] splitSize = split[0].split("x");
		final int[] counts = parseIntsFromString(split[1].trim(), " ").stream().mapToInt(i -> i).toArray();
		return new Area(Integer.parseInt(splitSize[0]), Integer.parseInt(splitSize[1]), counts);
	}

	private static record Present(int i, Set<Grid<Boolean>> variants) {
		public String toString() {
//			return String.format("Present %d\n%s", i,
//					glue(" ", variants.stream().map(g -> g.row(0)).map(r -> glue("", r)).toList()));
			return String.format("Present %d (%d variants)", i, variants.size());
		}

		public int area() {
			final Grid<Boolean> grid = variants.iterator().next();
			return grid.stream().mapToInt(p -> grid.get(p) ? 1 : 0).sum();
		}
	}

	private static record Area(int width, int height, int[] expectedCount) {
		public String toString() {
			return String.format("%dx%d: %s", width, height, Arrays.toString(expectedCount));
		}
	}
}
