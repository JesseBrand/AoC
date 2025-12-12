package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;

public class D12 {

	public static void main(final String[] args) throws IOException {
		solve("2025/d12");
	}

	private static void solve(final String file) throws IOException {
		final List<String> lines = readFile(file);
		final List<Present> presents = IntStream.range(0, 6).mapToObj(i -> toPresent(i, lines)).toList();
		out(presents);
		final List<Area> areas = lines.subList(30, lines.size()).stream().map(D12::toArea).toList();
		out(areas);

		out("Part 1: %d", areas.stream().filter(a -> canFit(a, presents)).count());
	}

	private static boolean canFit(final Area a, final List<Present> presents) {
		final List<Present> list = buildList(a, presents);
		final boolean result = trySimple(a.width() * a.height(), list);
		out("%s: %b", a, result);
		return result;
	}

	private static boolean trySimple(int areaSize, List<Present> list) {
		final int sum = list.stream().mapToInt(p -> p.area()).sum();
		return sum <= areaSize;
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

	private static Present toPresent(int i, List<String> lines) {
		final List<String> sublist = lines.subList(i * 5 + 1, i * 5 + 4);
		final Grid<Boolean> grid = buildBooleanGrid(sublist, '#');
		return new Present(i, grid);
	}

	private static Area toArea(final String line) {
		final String[] split = line.split(":");
		final String[] splitSize = split[0].split("x");
		final int[] counts = parseIntsFromString(split[1].trim(), " ").stream().mapToInt(i -> i).toArray();
		return new Area(Integer.parseInt(splitSize[0]), Integer.parseInt(splitSize[1]), counts);
	}

	private static record Present(int i, Grid<Boolean> grid) {
		public String toString() {
			return String.format("Present %d\n%s", i, grid);
		}

		public int area() {
			return grid.stream().mapToInt(p -> grid.get(p) ? 1 : 0).sum();
		}
	}

	private static record Area(int width, int height, int[] expectedCount) {
		public String toString() {
			return String.format("%dx%d: %s", width, height, Arrays.toString(expectedCount));
		}
	}
}
