package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.aoc2023.D04.parseCards;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import nl.jessebrand.aoc.aoc2023.D04.Card;

public class D04b {
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d04");
		final List<Card> cards = parseCards(lines);

		int[] cardCount = new int[cards.size() + 1];
		for (Card card : cards) {
			cardCount[card.id()] = 1;
		}
		System.out.println(String.format("Start: %s", Arrays.toString(cardCount)));
		for (Card card : cards) {
			int matches = 0;
			for (int yours : card.yourNumbers()) {
				for (int winning : card.winningNumbers()) {
					if (yours == winning) {
						matches++;
						break;
					}
				}
			}
			for (int i = card.id() + 1; i < card.id() + 1 + matches; i++) {
				cardCount[i] += cardCount[card.id()];
			}
			System.out.println(String.format("After card %s (%d matches): %s", card.id(), matches, Arrays.toString(cardCount)));
		}
		int total = 0;
		for (int i : cardCount) {
			total += i;
		}
		System.out.println(total);
	}

}
