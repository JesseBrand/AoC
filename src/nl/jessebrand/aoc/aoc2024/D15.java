package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.applyDirection;
import static nl.jessebrand.aoc.Utils.applyDirectionInverse;
import static nl.jessebrand.aoc.Utils.buildCharGrid;
import static nl.jessebrand.aoc.Utils.findGridPoint;
import static nl.jessebrand.aoc.Utils.findGridPoints;
import static nl.jessebrand.aoc.Utils.glue;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import nl.jessebrand.aoc.Direction;
import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;

public class D15 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d15");
		List<String> gridLines = null;
		String instructions = null;
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).isEmpty()) {
				gridLines = lines.subList(0, i);
				instructions = glue("", lines.subList(i + 1, lines.size()));
				break;
			}
		}

		final Grid<Character> grid1 = buildCharGrid(gridLines);
		final Grid<Character> grid2 = convertToLargeGrid(grid1);

//		out(grid1);
//		instructions.chars().mapToObj(D15::toDir).forEach(d -> performMove(grid1, d));
//		out(grid1);
//		out(findGridPoints(grid1, 'O').stream().mapToInt(D15::calcCoordinates).sum());
		
		out(grid2);
		instructions.chars().mapToObj(D15::toDir).forEach(d -> performMove(grid2, d));
		out(grid2);
		out(findGridPoints(grid2, '[').stream().mapToInt(D15::calcCoordinates).sum());
	}

	private static Grid<Character> convertToLargeGrid(Grid<Character> grid1) {
		final Grid<Character> result = new Grid<>(grid1.getWidth() * 2, grid1.getHeight());
		for (int y = 0; y < grid1.getHeight(); y++) {
			for (int x = 0; x < grid1.getWidth(); x++) {
				char[] mapped = map(grid1.get(x, y));
				result.set(x * 2, y, mapped[0]);
				result.set(x * 2 + 1, y, mapped[1]);
			}
		}
		return result;
	}

	private static char[] map(char c) {
		return switch(c) {
			case '@' -> new char[] {'@', '.'};
			case '#' -> new char[] {'#', '#'};
			case '.' -> new char[] {'.', '.'};
			case 'O' -> new char[] {'[', ']'};
			default -> throw new IllegalArgumentException();
		};
	}

	private static void performMove(final Grid<Character> grid, Direction dir) {
		out("\n%s%s, %s", grid, dir, findGridPoint(grid, '@'));
		if (dir == Direction.EAST || dir == Direction.WEST) {
			performEWMove(grid, dir);
		} else {
			performNSMove(grid, dir);
		}
	}
		
	private static void performNSMove(final Grid<Character> grid, final Direction dir) {
		final Point robotP = findGridPoint(grid, '@');
		final LinkedHashMap<Integer, Set<Integer>> byLine = new LinkedHashMap<>(); // map of y of lines vs x coordinates to shift from there
		int curY = robotP.y();
		byLine.put(curY, new HashSet<>(Arrays.asList(robotP.x())));
		while (true) {
			final int nextY = curY + dir.getYInc();
			out("%d->%d", curY, nextY);
			final Set<Integer> curLineList = byLine.get(curY);
			if (curLineList.isEmpty()) {
				// ok move
				break;
			}
			final Set<Integer> nextLineList = new LinkedHashSet<>();
			for (int x : curLineList) {
				char c = grid.get(x, nextY);
				out("%d,%d = %c", x, nextY, c);
				if (c == '#') {
					// can't move
					return;
				}
				if (c == 'O') {
					nextLineList.add(x);
				}
				if (c == '[') {
					nextLineList.add(x);
					nextLineList.add(x + 1);
				}
				if (c == ']') {
					nextLineList.add(x);
					nextLineList.add(x - 1);
				}
			}
			byLine.put(nextY, nextLineList);
			curY = nextY;
		}
		out(byLine);
		final List<Entry<Integer, Set<Integer>>> list = new ArrayList<>(byLine.entrySet());
		Collections.reverse(list);
		for (Entry<Integer, Set<Integer>> entry : list) {
			curY = entry.getKey();
			int nextY = curY + dir.getYInc();
			for (int x : entry.getValue()) {
				grid.set(x, nextY, grid.get(x, curY));
				grid.set(x, curY, '.');
			}
		}
	}

	private static void performEWMove(final Grid<Character> grid, Direction dir) {
		final Point robotP = findGridPoint(grid, '@');
		Point search = robotP;
		while (true) {
			search = applyDirection(search, dir);
			if (grid.get(search) == '.') {
				break;
			}
			if (grid.get(search) == '#') {
				return;
			}
		}
//		out("%s %s: free spot at %s", robotP, dir, search);
		while (!search.equals(robotP)) {
			final Point newPoint = applyDirectionInverse(search, dir);
//			out("Move %s(%c) to %s", newPoint, grid.get(newPoint), search);
			grid.set(search, grid.get(newPoint));
			search = newPoint;
		}
		grid.set(robotP, '.');
	}

	private static Direction toDir(int c) {
		return switch(c) {
			case '<' -> Direction.WEST;
			case '^' -> Direction.NORTH;
			case '>' -> Direction.EAST;
			case 'v' -> Direction.SOUTH;
			default -> throw new IllegalArgumentException();
		};
	}

	private static int calcCoordinates(final Point p) {
		return p.y() * 100 + p.x();
	}

}

// 1516281
// 1527969