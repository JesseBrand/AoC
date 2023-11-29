package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.parsePoints;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.jessebrand.aoc.Point;
import nl.jessebrand.aoc.PointList;
import nl.jessebrand.aoc.aoc2022.D14.Tile;

public class D14b {

	public static void main(String[] args) throws IOException {
		final List<String> input = readFile("2022/d14");
		final List<PointList> rocks = parseRocks(input);
		System.out.println(rocks);
		final Grid grid = createGrid(rocks);
		System.out.println(grid);
		final int result = simulate(grid);
		System.out.println(grid);
		System.out.println("Sand flowing blocked after " + result + " steps");
	}

	private static int simulate(final Grid grid) {
		int count = 0;
		int maxY = grid.tiles.length;
		while (true) {
			System.out.println("Simulating Sand #" + (count + 1));
			int x = 500;
			int y = 0;
			while (true) {
				if (y == maxY - 1) {
					grid.set(x, y, Tile.SAND);
					count++;
					break;
				} else if (grid.get(x, y + 1) == Tile.AIR) {
					y++;
				} else if (grid.get(x - 1, y + 1) == Tile.AIR) {
					x--;y++;
				} else if (grid.get(x + 1, y + 1) == Tile.AIR) {
					x++;y++;
				} else {
					grid.set(x, y, Tile.SAND);
					count++;
					if (x == 500 && y == 0) {
						return count;
					}
					break;
				}
			}
		}
	}

	private static List<PointList> parseRocks(List<String> input) {
		final List<PointList> result = new ArrayList<>();
		for (final String s : input) {
			String[] split = s.split(" -> ");
			result.add(new PointList(Arrays.asList(parsePoints(split))));
		}
		return result;
	}
	
	private static Grid createGrid(List<PointList> rocks) {
		int minY = 0, maxY=0;
		for (final PointList rock : rocks) {
			for (Point p : rock.points()) {
				minY = Math.min(minY,  p.y());
				maxY = Math.max(maxY,  p.y());
			}
		}
		System.out.println(String.format("Y %d-%d", minY, maxY));
		Grid grid = new Grid(maxY * 3, maxY - minY + 2, 500 - (maxY * 3 / 2));
		for (final PointList rock : rocks) {
			for (int i = 0; i < rock.points().size() - 1; i++) {
				drawRock(grid, rock.points().get(i), rock.points().get(i + 1));
			}
		}
		return grid;
	}
	
	private static void drawRock(Grid grid, Point p1, Point p2) {
		int x1 = p1.x();
		int x2 = p2.x();
		int y1 = p1.y();
		int y2 = p2.y();
		if (x1 < x2) { // left to right
			drawRockHorizontal(grid, x1, x2, y1);
		}
		if (x2 < x1) { // right to left
			drawRockHorizontal(grid, x2, x1, y1);
		}
		if (y1 < y2) { // top to bottom
			drawRockVertical(grid, x1, y1, y2);
		}
		if (y2 < y1) { // bottom to top
			drawRockVertical(grid, x1, y2, y1);
		}
	}

	private static void drawRockHorizontal(Grid grid, int x1, int x2, int y) {
		for (int x = x1; x <= x2; x++) {
			grid.set(x, y, Tile.ROCK);
		}
	}

	private static void drawRockVertical(Grid grid, int x, int y1, int y2) {
		for (int y = y1; y <= y2; y++) {
			grid.set(x, y, Tile.ROCK);
		}
	}

	static class Grid {
		
		private final Tile[][] tiles;
		private int xInc;
		
		Grid(int width, int height, int xInc) {
			this.xInc = xInc;
			tiles = new Tile[height][width];
			for (int y = 0; y < tiles.length; y++) {
				for (int x = 0; x < tiles[y].length; x++) {
					tiles[y][x] = Tile.AIR;
				}
			}
		}
		
		Tile get(int x, int y) {
			return tiles[y][x-xInc];
		}
		
		void set(int x, int y, Tile tile) {
			tiles[y][x-xInc] = tile;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			for (int y = 0; y < tiles.length; y++) {
				for (int x = 0; x < tiles[y].length; x++) {
					if (x + xInc == 500 && y == 0) {
						sb.append('+');
					} else {
						sb.append(tiles[y][x].getCharacter());
					}
				}
				sb.append('\n');
			}
			return sb.toString();
		}
	}
	
}
