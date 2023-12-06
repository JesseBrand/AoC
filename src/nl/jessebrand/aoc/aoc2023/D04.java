package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.parseIntsFromString;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class D04 {
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d04");
		final List<Card> cards = parseCards(lines);

		int total = 0;
		for (final Card card : cards) {
			int matches = 0;
			for (final int yours : card.yourNumbers()) {
				if (card.winningNumbers().contains(yours)) {
					matches++;
				}
			}
			total += Math.pow(2, matches - 1);
			System.out.println(String.format("Card %s has %d matches: %d score", card.id(), matches, (int) Math.pow(2, matches - 1)));
		}
		System.out.println(total);
	}

	static record Card(int id, List<Integer> yourNumbers, List<Integer> winningNumbers) {}

	static List<Card> parseCards(final List<String> lines) {
		final List<Card> result = new ArrayList<>();
		for (final String line : lines) {
			result.add(parseCard(line));
		}
		return result;
	}

	private static Card parseCard(final String line) {
		String tmp = line.substring("Card ".length());
		final int i = tmp.indexOf(":");
		final int id = Integer.parseInt(tmp.substring(0, i).trim());
		tmp = tmp.substring(i + 1);
		final String[] split = tmp.split("\\|");
		final List<Integer> yours = parseIntsFromString(split[0]);
		final List<Integer> winning = parseIntsFromString(split[1]);
		return new Card(id, yours, winning);
	}

}
