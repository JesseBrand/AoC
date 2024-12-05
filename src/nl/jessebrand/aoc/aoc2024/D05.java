package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.parseIntsFromStrings;
import static nl.jessebrand.aoc.Utils.parsePoints;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.jessebrand.aoc.Point;

public class D05 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d05");
		int emptyLine = -1;
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).isEmpty()) {
				emptyLine = i;
				break;
			}
		}
		if (emptyLine == -1) {
			throw new IllegalStateException();
		}
		final List<Point> tuples = parsePoints(lines.subList(0,  emptyLine), "\\|");
		final List<List<Integer>> updates = parseIntsFromStrings(lines.subList(emptyLine + 1, lines.size()), ",");
		
		int total = 0;
		final List<List<Integer>> incorrectUpdates = new ArrayList<>();
		for (List<Integer> update : updates) {
			if (isCorrectOrder(update, tuples)) {
				total += getMiddleNumber(update);
			} else {
				incorrectUpdates.add(update);
			}
		}
		System.out.println(total);
		
		int total2 = 0;
		for (final List<Integer> update : incorrectUpdates) {
			total2 += getMiddleNumber(orderUpdate(update, tuples));
		}
		System.out.println(total2);
	}

	private static List<Integer> orderUpdate(List<Integer> update, List<Point> tuples) {
		boolean correct = false;
		while (!correct) {
			correct = true;
			for (Point t : tuples) {
				int i0 = update.indexOf(t.x());
				int i1 = update.indexOf(t.y());
				if (i0 == -1 || i1 == -1 || i0 < i1) {
					continue;
				}
				int tmp = update.get(i0);
				update.set(i0, update.get(i1));
				update.set(i1, tmp);
				correct = false;
			}
		}
		return update;
	}

	private static boolean isCorrectOrder(List<Integer> update, List<Point> tuples) {
		for (final Point t : tuples) {
			if (update.contains(t.x()) && update.contains(t.y()) && update.indexOf(t.x()) > update.indexOf(t.y()) ) {
				return false;
			}
		}
		return true;
	}

	private static int getMiddleNumber(List<Integer> update) {
		if (update.size() % 2 == 0) {
			throw new IllegalArgumentException();
		}
		return update.get(update.size() / 2);
	}
}
