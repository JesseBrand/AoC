package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.manhDistance;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.jessebrand.aoc.LPoint;

public class D11 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d11");
		final Grid grid = parseGrid(lines);
//		System.out.println(grid);

		System.out.println("a: " + getDistanceBetweenPoints(grid, 2));
		System.out.println("b: " + getDistanceBetweenPoints(grid, 1000000));
	}

	private static long getDistanceBetweenPoints(final Grid grid, final long spaceSize) {
		long total = 0;
		final List<LPoint> points = grid.getPoints(spaceSize);
		for (int i = 0; i < points.size(); i++) {
			final LPoint p1 = points.get(i);
			for (int j = i + 1; j < points.size(); j++) {
				final LPoint p2 = points.get(j);
				final long distance = manhDistance(p1, p2);
//				System.out.println(String.format("Shortest from [%d] %d,%d to [%d] %d,%d = %d", points.indexOf(p1) + 1, p1.x(), p1.y(), points.indexOf(p2) + 1, p2.x(), p2.y(), distance));
				total += distance;
			}
		}
		return total;
	}
	
	private static Grid parseGrid(final List<String> lines) {
		final Grid grid = new Grid();
		int y = 0;
		for (final String line : lines) {
			if (!line.contains("#")) {
//				System.out.println("skipping line");
				grid.addSpacerRow(y);
			} else {
				for (int x = 0; x < line.length(); x++) {
					char c = line.charAt(x);
					if (c == '#') {
						grid.addGalaxy(x, y);
					}
				}
			}
			y++;
		}
		for (int x = 0; x < grid.getWidth(); x++) {
			if (allXEmpty(grid, x)) {
				System.out.println("all x empty at " + x);
				grid.addColumn(x);
			}
		}
		return grid;
	}


	private static boolean allXEmpty(final Grid grid, final int x) {
		boolean found = false;
		for (final List<Integer> row : grid.rows()) {
			if (row.contains(x)) {
				found = true;
				break;
			}
		}
		return !found;
	}

	private static class Grid {
		
		private int width = 0;

		private final List<List<Integer>> grid = new ArrayList<>();
		private final List<Integer> spacerColumns = new ArrayList<>();
		private final List<Integer> spacerRows = new ArrayList<>();
		
		List<LPoint> getPoints(final long spaceSize) {
			System.out.println("Column spacers at " + spacerColumns);
			System.out.println("Row spacers at " + spacerRows);
			final List<LPoint> result = new ArrayList<>();
			long xSpace = 0;
			long ySpace = 0;
			for (int y = 0; y < rows().size(); y++) {
				if (spacerRows.contains(y)) {
					ySpace += spaceSize - 1;
				}
				xSpace = 0;
				for (int x = 0; x < width; x++) {
					if (spacerColumns.contains(x)) {
						xSpace += spaceSize - 1;
					}
					if (rows().get(y).contains(x)) {
						result.add(new LPoint(x + xSpace, y + ySpace));
					}
				}
			}
			return result;
		}

		void addColumn(final int x) {
			spacerColumns.add(x);
		}

		void addSpacerRow(final int y) {
			spacerRows.add(y);
		}

		List<List<Integer>> rows() {
			return grid;
		}

		void addGalaxy(final int x, final int y) {
//			System.out.println("added at " + x + ", " + y);
			getRow(y).add(x);
			width = Math.max(width, x + 1);
		}

		private List<Integer> getRow(final int y) {
			while (grid.size() <= y) {
				grid.add(new ArrayList<>());
			}
			return grid.get(y);
		}
		
		int getWidth() {
			return width;
		}
		
		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			for (List<Integer> row : rows()) {
				for (int x = 0; x < width; x++) {
					if (row.contains(x)) {
						sb.append('#');
					} else {
						sb.append('.');
					}
				}
				sb.append("\n");
			}
			return sb.toString();
		}
	}

}
