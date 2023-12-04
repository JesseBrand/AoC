package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class D04b {
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d04");
		final List<Card> cards = parseCards(lines);
		System.out.println(cards);
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
	
	private record Card(int id, List<Integer> yourNumbers, List<Integer> winningNumbers) {}

	private static List<Card> parseCards(List<String> lines) {
		List<Card> result = new ArrayList<>();
		for (final String line : lines) {
			result.add(parseCard(line));
		}
		return result;
	}

	private static Card parseCard(String line) {
		String tmp = line.substring("Card ".length());
		int i = tmp.indexOf(":");
		int id = Integer.parseInt(tmp.substring(0, i).trim());
		tmp = tmp.substring(i + 1);
		String[] split = tmp.split("\\|");
		String[] splitLeft = split[0].split("\\ ");
		String[] splitRight = split[1].split("\\ ");
		final List<Integer> yours = new ArrayList<>();
		for (String left : splitLeft) {
			if (!left.isBlank()) {
				yours.add(Integer.parseInt(left.trim()));
			}
		}
		final List<Integer> winning = new ArrayList<>();
		for (String right : splitRight) {
			if (!right.isBlank()) {
				winning.add(Integer.parseInt(right.trim()));
			}
		}
		return new Card(id, yours, winning);
	}

}
