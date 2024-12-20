package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.*;
import static nl.jessebrand.aoc.Utils.findGridPoint;
import static nl.jessebrand.aoc.Utils.getNeighbours;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.Utils.solveAStar;
import static nl.jessebrand.aoc.Utils.visualizeAStar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Path;
import nl.jessebrand.aoc.Point;

public class D20 {

	public static void main(String[] args) throws IOException {
		out("E1 (calc): %d", calcAllCheats("2024/d20ex", 2, 2).size()); // 44
		out("E2 (calc): %d", calcAllCheats("2024/d20ex", 20, 50).size()); // 285
		
		out("1 (calc): %d", calcAllCheats("2024/d20", 2, 100).size()); // 1502
		out("2 (calc): %d", calcAllCheats("2024/d20", 20, 100).size()); // 1028136
	}

	private static List<Point> calcAllCheats(final String filename, final int shortcutLength, final int minLength) throws IOException {
		final Grid<Character> charGrid = buildCharGrid(readFile(filename));
		final Grid<Boolean> grid = charGrid.convert(c -> c == '#');
		final Point start = findGridPoint(charGrid, 'S');
		final Point end = findGridPoint(charGrid, 'E');
		return calcAllCheats(grid, start, end, shortcutLength, minLength);
	}

	private static List<Point> calcAllCheats(final Grid<Boolean> grid, final Point start, final Point end, final int maxShortcutLength, final int minLength) {
		final List<Point> cheats = new ArrayList<>();
		final Path origPath = solveAStar(grid, start, end);
		final int origLength = origPath.length();
		for (int i = 0; i < origPath.length() + 1; i++) {
			final Point p1 = origPath.get(i);
			for (int j = i + 1; j < origPath.length() + 1; j++) {
				final Point p2 = origPath.get(j);
				int shortcutLength = manhDistance(p1, p2);
				if (shortcutLength <= maxShortcutLength) {
					int newLength = origLength - j + i + shortcutLength;
					if (newLength <= origLength - minLength) {
						cheats.add(p1);
						out("Found cheat for %d at %s-%s", origLength - newLength, p1, p2);
					}
				}
			}
		}
		return cheats;
	}

	private static List<Point> findAllCheats(final String filename, final int minLength) throws IOException {
		final Grid<Character> charGrid = buildCharGrid(readFile(filename));
		final Grid<Boolean> grid = charGrid.convert(c -> c == '#');
		final Point start = findGridPoint(charGrid, 'S');
		final Point end = findGridPoint(charGrid, 'E');
		visualizeAStar(filename, grid, solveAStar(grid, start, end), 8);
		return findAllCheats(grid, start, end, minLength);
	}

	private static List<Point> findAllCheats(final Grid<Boolean> grid, final Point start, final Point end, final int minLength) {
		final List<Point> cheats = new ArrayList<>();
		final int origLength = solveAStar(grid, start, end).length();
		out(origLength);
		for (final Point p : grid) {
			if (pointQualifiesForCheat(grid, p)) {
				grid.set(p, false);
				final int newLength = solveAStar(grid, start, end).length();
				grid.set(p, true);
				if (newLength <= origLength - minLength) {
					cheats.add(p);
//					out("Found cheat for %d at %s", origLength - newLength, p);
				}
			}
		}
		return cheats;
	}

	private static boolean pointQualifiesForCheat(final Grid<Boolean> grid, final Point p) {
		// where is now wall and at least 2 open neighbours
		return grid.get(p) && getNeighbours(p).stream().filter(p2 -> !grid.getOr(p2, true)).count() > 1;
	}
}

// 1502

// 1072965 too high
// 1028136
