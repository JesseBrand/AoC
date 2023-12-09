package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.countTotal;
import static nl.jessebrand.aoc.Utils.parseLines;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import nl.jessebrand.aoc.aoc2023.D04.Card;

public class D04b {
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d04");
		final List<Card> cards = parseLines(lines, D04::parseCard);

		final int[] cardCount = new int[cards.size() + 1];
		for (final Card card : cards) {
			cardCount[card.id()] = 1;
		}
		System.out.println(String.format("Start: %s", Arrays.toString(cardCount)));
		for (final Card card : cards) {
			int matches = 0;
			for (int yours : card.yourNumbers()) {
				if (card.winningNumbers().contains(yours)) {
					matches++;
				}
			}
			for (int i = card.id() + 1; i < card.id() + 1 + matches; i++) {
				cardCount[i] += cardCount[card.id()];
			}
			System.out.println(String.format("After card %s (%d matches) * %d: %s", card.id(), matches, cardCount[card.id()], Arrays.toString(cardCount)));
		}
		System.out.println(countTotal(cardCount));
	}

}
