package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D07b {
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d07");
		final List<CardSet> cardSets = parseCardSets(lines);
		cardSets.sort(new CardSorter());

		int total = 0;
		int count = 1;
		for (CardSet set : cardSets) {
			total += set.bet() * count;
//			System.out.println(String.format("%s: %d * %d = %d", set.cards(), count, set.bet(), set.bet() * count));
			System.out.println(String.format("%s: %d * %d = %d Count = %s Rank = %d", set.cards(), count, set.bet(), set.bet() * count, Arrays.toString(countSame(set)), calcRankType(set)));
			count++;
		}
		System.out.println(total);
	}
	
	private static List<CardSet> parseCardSets(List<String> lines) {
		final List<CardSet> result = new ArrayList<>();
		for (String line : lines) {
			result.add(parseCardSet(line));
		}
		return result;
	}

	private static CardSet parseCardSet(String line) {
		String[] split = line.split("\\ ");
		return new CardSet(split[0], Integer.parseInt(split[1]));
	}

	private static record CardSet(String cards, int bet) {}

	private static class CardSorter implements Comparator<CardSet> {

		// if o1 < o2 then -1
		@Override
		public int compare(CardSet o1, CardSet o2) {
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

	private static int calcRankType(CardSet set) {
		int[] same = countSame(set);
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

	public static int getCardValue(char c) {
		switch (c) {
		case 'A': return 13;
		case 'K': return 12;
		case 'Q': return 11;
		case 'T': return 10;
		case '9': return 9;
		case '8': return 8;
		case '7': return 7;
		case '6': return 6;
		case '5': return 5;
		case '4': return 4;
		case '3': return 3;
		case '2': return 2;
		case 'J': return 1;
		default: throw new IllegalStateException("Missing "+ c);
		}
	}

	private static int[] countSame(CardSet set) {
		Map<Character, Integer> map = new HashMap<>();
		for (char c : set.cards().toCharArray()) {
			if (c == 'J') {
				continue;
			}
			if (map.containsKey(c)) {
				map.put(c, map.get(c) + 1);
			} else {
				map.put(c, 1);
			}
		}
		int[] result = new int[map.size()];
		int i = 0;
		for (char key : map.keySet()) {
			result[i] = map.get(key);
			i++;
		}
		return result;
	}
}

