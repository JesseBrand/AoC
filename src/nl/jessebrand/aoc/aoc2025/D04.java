package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.buildCharGrid;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;

public class D04 {

	public static void main(final String[] args) throws IOException {
		solve("2025/d04ex");
		solve("2025/d04");
	}

	private static void solve(final String file) throws IOException {
		final List<String> lines = readFile(file);
//		out(lines);
		Grid<Character> grid = buildCharGrid(lines);
		out(grid);
		int totalA = gatherMoveable(grid).size();
		
		out("Part 1: %d", totalA);

		int totalB = 0;
		List<Point> list;
		while (true) {
			list = gatherMoveable(grid);
			if (list.isEmpty()) {
				break;
			}
			for (final Point p : list) {
				grid.set(p, '.');
			}
			totalB += list.size();
		}
		out("Part 2: %d", totalB);
	}

	private static List<Point> gatherMoveable(Grid<Character> grid) {
		final List<Point> result = new ArrayList<>();
		for (int y = 0; y < grid.getHeight(); y++) {
			for (int x = 0; x < grid.getWidth(); x++) {
				if (isMoveableRoll(grid, x, y)) {
					result.add(new Point(x, y));
				}
			}
		}
		return result;
	}

	private static boolean isMoveableRoll(Grid<Character> grid, int x, int y) {
		if (grid.get(x, y) != '@') {
			return false;
		}
		int result =
				(isRoll(grid, x - 1, y - 1) ? 1 : 0)
				+ (isRoll(grid, x, y - 1) ? 1 : 0)
				+ (isRoll(grid, x + 1, y - 1) ? 1 : 0)
				+ (isRoll(grid, x - 1, y) ? 1 : 0)
				+ (isRoll(grid, x + 1, y) ? 1 : 0)
				+ (isRoll(grid, x - 1, y + 1) ? 1 : 0)
				+ (isRoll(grid, x, y + 1) ? 1 : 0)
				+ (isRoll(grid, x + 1, y + 1) ? 1 : 0);
		return result < 4;
	}

	private static boolean isRoll(Grid<Character> grid, int x, int y) {
		return x >= 0 && y >= 0 && x < grid.getWidth() && y < grid.getHeight() && grid.get(x, y) == '@'; 
	}

}
