package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.buildCharGrid;
import static nl.jessebrand.aoc.Utils.get8Neighbours;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
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
		final Grid<Character> grid = buildCharGrid(lines);
		out(grid);

		out("Part 1: %d", gatherMoveable(grid).size());

		int totalB = 0;
		while (true) {
			final List<Point> list = gatherMoveable(grid);
			if (list.isEmpty()) {
				break;
			}
			list.stream().forEach(p -> grid.set(p, '.'));
			totalB += list.size();
		}
//		out(grid);
		out("Part 2: %d", totalB);
	}

	private static List<Point> gatherMoveable(final Grid<Character> grid) {
		return grid.stream().filter(p -> isMoveableRoll(grid, p)).toList();
	}

	private static boolean isMoveableRoll(final Grid<Character> grid, final Point p) {
		if (grid.get(p) != '@') {
			return false;
		}
		final int result = (int) get8Neighbours(p).stream().filter(n -> grid.getOr(n, '.') == '@').count();
		return result < 4;
	}
}

// 1435
// 8623
