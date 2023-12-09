package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.parseLines;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D07 {
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d07");
		final List<CardSet> cardSets = parseLines(lines, D07::parseCardSet);
		cardSets.sort(new CardSorter());

		int total = 0;
		int count = 1;
		for (CardSet set : cardSets) {
			total += set.bet() * count;
			System.out.println(String.format("%s: %d * %d = %d Count = %s Rank = %d", set.cards(), count, set.bet(), set.bet() * count, Arrays.toString(countSame(set, false)), calcRankType(set)));
			count++;
		}
		System.out.println(total);
	}

	static CardSet parseCardSet(final String line) {
		final String[] split = line.split("\\ ");
		return new CardSet(split[0], Integer.parseInt(split[1]));
	}

	static record CardSet(String cards, int bet) {}

	private static class CardSorter implements Comparator<CardSet> {

		// if o1 < o2 then -1
		@Override
		public int compare(final CardSet o1, final CardSet o2) {
			int rankType1 = calcRankType(o1);
			int rankType2 = calcRankType(o2);
			if (rankType1 != rankType2) {
				return rankType1 - rankType2;
			}
			for (int i = 0; i < 5; i++) {
				int cardValue1 = getCardValue(o1.cards().charAt(i));
				int cardValue2 = getCardValue(o2.cards().charAt(i));
				if (cardValue1 != cardValue2) {
					return cardValue1 - cardValue2;
				}
			}
			return 0;
		}
		
	}

	private static int calcRankType(final CardSet set) {
		final int[] same = countSame(set, false);
		if (same.length == 1) {
			return 6;
		}
		if (same.length == 2) {
			if (same[0] == 4 || same[1] == 4) {
				return 5;
			}
			return 4;
		}
		if (same.length == 3) {
			if (same[0] == 3 || same[1] == 3 || same[2] == 3) {
				return 3;
			}
			return 2;
		}
		if (same.length == 4) {
			return 1;
		}
		return 0;
	}

	public static int getCardValue(char c) {
		return "23456789TJQKA".indexOf(c);
	}

	static int[] countSame(final CardSet set, final boolean skipJ) {
		final Map<Character, Integer> map = new HashMap<>();
		for (final char c : set.cards().toCharArray()) {
			if (skipJ && c == 'J') {
				continue;
			}
			if (map.containsKey(c)) {
				map.put(c, map.get(c) + 1);
			} else {
				map.put(c, 1);
			}
		}
		final int[] result = new int[map.size()];
		int i = 0;
		for (char key : map.keySet()) {
			result[i] = map.get(key);
			i++;
		}
		return result;
	}
}
