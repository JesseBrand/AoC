package nl.jessebrand.aoc.aoc2021;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.jessebrand.aoc.Grid;

public class D04 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2021/d4");
		
		final List<Grid<BingoCell>> cards = readBingoCards(lines);
		final List<Integer> drawnNumbers = readDrawnNumbers(lines);
		final List<Grid<BingoCell>> wins = new ArrayList<>();
		System.out.println(drawnNumbers);
		for (final int number : drawnNumbers) {
			for (final Grid<BingoCell> card : cards) {
				if (markAndCheckBingo(card, number) && !wins.contains(card)) {
					wins.add(card);
					System.out.println("\n" + card + "Card has bingo after number " + number + "!");
					final int unmarkedSum = calculateUnmarkedSum(card);
					System.out.println(String.format("Result = %d (%d * %d)", (unmarkedSum * number), unmarkedSum, number));
				}
			}
		}
	}

	private static int calculateUnmarkedSum(Grid<BingoCell> card) {
		int total = 0;
		for (int y = 0; y < card.getHeight(); y++) {
			for (int x = 0; x < card.getWidth(); x++) {
				BingoCell cell = card.get(x, y);
				if (!cell.isChecked()) {
					total += cell.getNumber();
				}
			}
		}
		return total;
	}

	private static boolean markAndCheckBingo(Grid<BingoCell> card, int number) {
		for (int y = 0; y < card.getHeight(); y++) {
			for (int x = 0; x < card.getWidth(); x++) {
				BingoCell cell = card.get(x, y);
				if (cell.getNumber() == number) {
					cell.check();
					if (isBingo(card)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean isBingo(Grid<BingoCell> card) {
		for (int y = 0; y < card.getHeight(); y++) {
			boolean bingo = true;
			for (int x = 0; x < card.getWidth(); x++) {
				if (!card.get(x, y).isChecked()) {
					bingo = false;
					break;
				}
			}
			if (bingo) {
				return true;
			}
		}
		for (int x = 0; x < card.getHeight(); x++) {
			boolean bingo = true;
			for (int y = 0; y < card.getWidth(); y++) {
				if (!card.get(x, y).isChecked()) {
					bingo = false;
					break;
				}
			}
			if (bingo) {
				return true;
			}
		}
		return false;
	}

	private static List<Grid<BingoCell>> readBingoCards(final List<String> lines) {
		final List<Grid<BingoCell>> result = new ArrayList<>();
		int i = 2;
		while (i < lines.size()) {
			Grid<BingoCell> grid = new Grid<>(5, 5, " ");
			for (int l = 0; l < 5; l++) {
				readLine(grid, lines.get(i + l), l);
			}
			result.add(grid);
			i += 6;
		}
		return result;
	}

	private static void readLine(Grid<BingoCell> grid, final String line, int y) {
		String[] split = line.trim().split(" +");
		for (int i = 0; i < split.length; i++) {
			grid.set(i, y, new BingoCell(Integer.parseInt(split[i])));
		}
	}

	private static List<Integer> readDrawnNumbers(List<String> lines) {
		return Arrays.asList(lines.get(0).split(",")).stream().map(s -> Integer.parseInt(s)).toList();
	}
	
	private static class BingoCell {
		
		private final int number;
		private boolean checked;

		BingoCell(int number) {
			this.number = number;
		}
		
		int getNumber() {
			return number;
		}
		
		boolean isChecked() {
			return checked;
		}
		
		void check() {
			this.checked = true;
		}
		
		@Override
		public String toString() {
			return checked ? "[" + number + "]" : "" + number;
		}
	}
	
}

// 2829456 too high