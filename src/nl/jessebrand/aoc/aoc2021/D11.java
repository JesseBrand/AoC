package nl.jessebrand.aoc.aoc2021;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;

public class D11 {
	
	public static void main(String...args) throws IOException {
		final List<String> lines = readFile("2021/d11");
		final Grid<Octopus> grid = buildGrid(lines);
		System.out.println(grid);
		
		final int width = grid.getWidth();
		final int height = grid.getHeight();
		int totalFlashes = 0;
		boolean keepgoing = true;
		for (int i = 0; keepgoing; i++) {
			Set<Point> newFlashed = new HashSet<>();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (grid.get(x, y).increase()) {
						newFlashed.add(new Point(x, y));
						totalFlashes++;
					}
				}
			}
//			out("After step %d increase : %d total flashes", i, totalFlashes);
			while (!newFlashed.isEmpty()) {
				Set<Point> nextFlashed = new HashSet<>();
				for (final Point flash : newFlashed) {
					List<Point> localChecks = new ArrayList<>();
					if (flash.x() > 0 && flash.y() > 0) { localChecks.add(new Point(flash.x() - 1, flash.y() - 1)); }
					if (flash.y() > 0) { localChecks.add(new Point(flash.x(), flash.y() - 1)); }
					if (flash.x() < width - 1 && flash.y() > 0) { localChecks.add(new Point(flash.x() + 1, flash.y() - 1)); }
					if (flash.x() > 0 ) { localChecks.add(new Point(flash.x() - 1, flash.y())); }
					if (flash.x() < width - 1) { localChecks.add(new Point(flash.x() + 1, flash.y())); }
					if (flash.x() > 0 && flash.y() < height - 1) { localChecks.add(new Point(flash.x() - 1, flash.y() + 1)); }
					if (flash.y() < height - 1) { localChecks.add(new Point(flash.x(), flash.y() + 1)); }
					if (flash.x() < width - 1 && flash.y() < height - 1) { localChecks.add(new Point(flash.x() + 1, flash.y() + 1)); }
					for (Point p : localChecks) {
						if (grid.get(p).increase()) {
							nextFlashed.add(p);
							totalFlashes++;
						}
					}
//					out("After cycle: %d", totalFlashes);
				}
				newFlashed = nextFlashed;
			}
			boolean all = true;
			for (int y = 0; y < grid.getHeight(); y++) {
				for (int x = 0; x < grid.getWidth(); x++) {
					all = all && grid.get(x, y).flashed;
				}
			}
			if (all) {
				out("All flashed during %d", i + 1);
				keepgoing = false;
			}
			for (int y = 0; y < grid.getHeight(); y++) {
				for (int x = 0; x < grid.getWidth(); x++) {
					grid.get(x, y).reset();
				}
			}
			if (i == 99) {
				out("After step %d: %d total flashes", i + 1, totalFlashes);
			}
		}
		
	}
	
	private static Grid<Octopus> buildGrid(List<String> lines) {
		final int height = lines.size();
		final int width = lines.get(0).length();
		Grid<Octopus> result = new Grid<>(width, height, "");
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				result.set(x, y, new Octopus(Integer.parseInt(lines.get(y).substring(x, x + 1))));
			}
		}
		return result;
	}

	static class Octopus {
		
		int energy;
		boolean flashed = false;
		
		Octopus(int energy) {
			this.energy = energy;
		}
		
		boolean increase() {
			energy++;
			if (energy > 9 && !flashed) {
				flashed = true;
				return true;
			}
			return false;
		}
		
		void reset() {
			if (flashed) {
				flashed = false;
				energy = 0;
			}
		}
		
		@Override
		public String toString() {
			return "" + energy;
		}
	}
	
}

// 444 too low
