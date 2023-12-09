package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.parseLines;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.aoc2023.D07.countSame;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import nl.jessebrand.aoc.aoc2023.D07.CardSet;

public class D07b {
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d07");
		final List<CardSet> cardSets = parseLines(lines, D07::parseCardSet);
		cardSets.sort(new CardSorter());

		int total = 0;
		int count = 1;
		for (CardSet set : cardSets) {
			total += set.bet() * count;
//			System.out.println(String.format("%s: %d * %d = %d", set.cards(), count, set.bet(), set.bet() * count));
			System.out.println(String.format("%s: %d * %d = %d Count = %s Rank = %d", set.cards(), count, set.bet(), set.bet() * count, Arrays.toString(countSame(set, true)), calcRankType(set)));
			count++;
		}
		System.out.println(total);
	}
	
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
		final int[] same = countSame(set, true);
		if (same.length == 1 || same.length == 0) {
			return 6; // 5 of kind
		}
		if (same.length == 2) {
			if (same[0] == 1 || same[1] == 1) {
				// KKKK1
				// KKJJ1
				return 5; // 4 of kind
			}
			return 4; // full house
		}
		
		if (same.length == 3) {
			if (same[0] == 3 || same[1] == 3 || same[2] == 3
					|| same[0]+same[1]+same[2] < 5) {
				return 3; // 3 of kind
			}
			return 2; // two pairs
		}
		if (same.length == 4) {
			return 1; // one pair
		}
		return 0;
	}

	public static int getCardValue(final char c) {
		return "J23456789TQKA".indexOf(c);
	}
}
