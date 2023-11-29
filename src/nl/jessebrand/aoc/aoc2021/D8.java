package nl.jessebrand.aoc.aoc2021;

import static nl.jessebrand.aoc.Utils.order;
import static nl.jessebrand.aoc.Utils.orderAll;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.aoc2021.D8.Digit._1;
import static nl.jessebrand.aoc.aoc2021.D8.Digit._4;
import static nl.jessebrand.aoc.aoc2021.D8.Digit._7;
import static nl.jessebrand.aoc.aoc2021.D8.Segment.BOTTOM;
import static nl.jessebrand.aoc.aoc2021.D8.Segment.BOTTOM_LEFT;
import static nl.jessebrand.aoc.aoc2021.D8.Segment.BOTTOM_RIGHT;
import static nl.jessebrand.aoc.aoc2021.D8.Segment.CENTER;
import static nl.jessebrand.aoc.aoc2021.D8.Segment.TOP;
import static nl.jessebrand.aoc.aoc2021.D8.Segment.TOP_LEFT;
import static nl.jessebrand.aoc.aoc2021.D8.Segment.TOP_RIGHT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.jessebrand.aoc.Tuple;

public class D8 {
	
	public static void main(String...args) throws IOException {
		final List<String> lines = readFile("2021/d8");

		final List<Tuple<List<String>>> input = parseInput(lines);
		System.out.println(input);
		int occ1 = countSimple(input, 2);
		int occ4 = countSimple(input, 4);
		int occ7 = countSimple(input, 3);
		int occ8 = countSimple(input, 7);
		System.out.println(String.format("Answer 1: %d (%d + %d + %d + %d)", occ1 + occ4 + occ7 + occ8, occ1, occ4, occ7, occ8));
		System.out.println();
		
		int total = 0;
		for (final Tuple<List<String>> line : input) {
			List<String> order = figureOutOrder(line);
			System.out.println(order);
			int decoded = decodeCharacters(order, line); 
			System.out.println(decoded);
			total += decoded;
		}
		System.out.println("Total = " + total);
	}

	private static int decodeCharacters(List<String> order, Tuple<List<String>> line) {
		String result = "";
		System.out.println("Finding digits " + line.l2() + " in " + order);
		for (final String c : line.l2()) {
			result += order.indexOf(c);
		}
		return Integer.parseInt(result);
	}

	private static List<String> figureOutOrder(final Tuple<List<String>> line) {
		char top = 0, topLeft = 0, topRight = 0, center = 0, bottomLeft = 0, bottomRight = 0, bottom = 0;
		final List<String> digits = line.l1();
		final Map<Character, List<Segment>> possibilities = new HashMap<>();
		for (char c = 'a'; c <= 'g'; c++) {
			possibilities.put(c, new ArrayList<>(Arrays.asList(Segment.values())));
		}
		String one = findBySize(digits, 2).get(0);
		possibilities.entrySet().stream().forEach(e -> {
			if (one.indexOf(e.getKey()) >= 0) {
				retain(e, _1);
			} else {
				remove(e, _1);
			}
		});
		String seven = findBySize(digits, 3).get(0);
		possibilities.entrySet().stream().forEach(e -> {
			if (seven.indexOf(e.getKey()) >= 0) {
				retain(e, _7);
			} else {
				remove(e, _7);
			}
		});
		top = findExactOne(possibilities, TOP);
		String four = findBySize(digits, 4).get(0);
		possibilities.entrySet().stream().forEach(e -> {
			if (four.indexOf(e.getKey()) >= 0) {
				retain(e, _4);
			} else {
				remove(e, _4);
			}
		});
		List<String> zeroSixNine = findBySize(digits, 6);
//		System.out.println(String.format("possibilities: %s", possibilities));
//		System.out.println("069: " + zeroSixNine);
		possibilities.entrySet().stream().forEach(e -> {
			boolean found = false;
			for (String zsn : zeroSixNine) {
				if (zsn.indexOf(e.getKey()) == -1) {
					// TODO: klopt iets niet
					remove(e, TOP_LEFT);
					found = true;
				}
			}
			if (!found) {
				remove(e, CENTER);
			}
		});
		topLeft = findExactOne(possibilities, TOP_LEFT);
		center = findExactOne(possibilities, CENTER);
//		System.out.println(String.format("possibilities: %s", possibilities));
		List<String> twoThreeFive = findBySize(digits, 5);
//		System.out.println("235: " + twoThreeFive);
		for (String ttf : twoThreeFive) {
			boolean match = true;
			if (ttf.indexOf(topLeft) == -1) {
				// 2 or 3
//				System.out.println("Missing " + topLeft + ", must be 2 or 3: " + ttf);
				match = false;
			}
			if (match) {
				// 5
//				System.out.println("Contains " + topLeft + ", must be 5: " + ttf);
				findExact(possibilities, BOTTOM_LEFT, BOTTOM).stream().forEach(c -> {
					if (ttf.indexOf(c) > -1) {
						possibilities.get(c).remove(BOTTOM_LEFT);
					} else {
						possibilities.get(c).remove(BOTTOM);
					}
				});
				findExact(possibilities, TOP_RIGHT, BOTTOM_RIGHT).stream().forEach(c -> {
					if (ttf.indexOf(c) > -1) {
						possibilities.get(c).remove(TOP_RIGHT);
					} else {
						possibilities.get(c).remove(BOTTOM_RIGHT);
					}
				});
			}
		}
		topRight = findExactOne(possibilities, TOP_RIGHT);
		bottomLeft = findExactOne(possibilities, BOTTOM_LEFT);
		bottomRight = findExactOne(possibilities, BOTTOM_RIGHT);
		bottom = findExactOne(possibilities, BOTTOM);
		System.out.println(String.format("Line %s", digits));
		System.out.println(String.format("Possibilities: %s\nTop: %c Top-left: %c, Top-right: %c, Center: %c, Bottom-left: %c, Bottom-right: %c, bottom: %c", possibilities, top, topLeft, topRight, center, bottomLeft, bottomRight, bottom));
		return Arrays.asList(
				constructDigit(top, topLeft, topRight, bottomLeft, bottomRight, bottom), // 0
				constructDigit(topRight, bottomRight), // 1
				constructDigit(top, topRight, center, bottomLeft, bottom), // 2
				constructDigit(top, topRight, center, bottomRight, bottom), // 3
				constructDigit(topLeft, topRight, center, bottomRight), // 4
				constructDigit(top, topLeft, center, bottomRight, bottom), // 5
				constructDigit(top, topLeft, center, bottomLeft, bottomRight, bottom), // 6
				constructDigit(top, topRight, bottomRight), // 7
				constructDigit(top, topLeft, topRight, center, bottomLeft, bottomRight, bottom), // 8
				constructDigit(top, topLeft, topRight, center, bottomRight, bottom)); // 9
	}

	private static String constructDigit(char...chars) {
		return order(new String(chars));
	}

	private static char findExactOne(Map<Character, List<Segment>> possibilities, Segment...segments) {
		final List<Character> matches = findExact(possibilities, segments);
		if (matches.size() != 1) {
			throw new IllegalArgumentException("Did not find 1 but " + matches.size());
		}
		return matches.get(0);
	}

	private static List<Character> findExact(Map<Character, List<Segment>> possibilities, Segment...segments) {
		final List<Character> result = new ArrayList<>();
		for (final char c : possibilities.keySet()) {
			List<Segment> list = possibilities.get(c);
			if (list.size() != segments.length) {
				continue;
			}
			boolean match = true;
			for (Segment segment : segments) {
				if (!list.contains(segment)) {
					match = false;
					break;
				}
			}
			if (match) {
				result.add(c);
			}
		}
		return result;
	}

	private static void retain(Entry<Character, List<Segment>> e, Digit digit) {
		e.getValue().retainAll(digit.segments);
	}

	private static void remove(Entry<Character, List<Segment>> e, Digit digit) {
		e.getValue().removeAll(digit.segments);
	}

	private static void remove(Entry<Character, List<Segment>> e, Segment segment) {
		e.getValue().remove(segment);
	}

//	private static void retain(Entry<Character, List<Segment>> e, Segment segment) {
//		e.getValue().retainAll(Arrays.asList(segment));
//	}

	private static List<String> findBySize(List<String> digits, int size) {
		final List<String> result = new ArrayList<>();
		for (final String digit : digits) {
			if (digit.length() == size) {
				result.add(digit);
			}
		}
		if (result.isEmpty()) {
			throw new IllegalArgumentException("No digit with length " + size + " in " + digits);
		}
		return result;
	}

	private static int countSimple(List<Tuple<List<String>>> input, int segments) {
		int total = 0;
		for (final Tuple<List<String>> tuple : input) {
			for (String value : tuple.l2()) {
				if (value.length() == segments) {
					total++;
				}
			}
		}
		return total;
	}

	private static List<Tuple<List<String>>> parseInput(List<String> lines) {
		final List<Tuple<List<String>>> result = new ArrayList<>(lines.size());
		for (final String line : lines) {
			List<String> split = Arrays.asList(line.split(" \\| "));
			result.add(new Tuple<List<String>>(orderAll(Arrays.asList(split.get(0).split(" "))), orderAll(Arrays.asList(split.get(1).split(" ")))));
		}
		return result;
	}
	
	static enum Segment {
		TOP,
		TOP_LEFT,
		TOP_RIGHT,
		CENTER,
		BOTTOM_LEFT,
		BOTTOM_RIGHT,
		BOTTOM;
	}
	
	static enum Digit {
		_1(TOP_RIGHT, BOTTOM_RIGHT),
		_2(TOP, TOP_RIGHT, CENTER, BOTTOM_LEFT, BOTTOM),
		_3(TOP, TOP_RIGHT, CENTER, BOTTOM_RIGHT, BOTTOM),
		_4(TOP_LEFT, TOP_RIGHT, CENTER, BOTTOM_RIGHT),
		_5(TOP, TOP_LEFT, CENTER, BOTTOM_RIGHT, BOTTOM),
		_6(TOP, TOP_LEFT, CENTER, BOTTOM_LEFT, BOTTOM_RIGHT, BOTTOM),
		_7(TOP, TOP_RIGHT, BOTTOM_RIGHT),
		_8(TOP, TOP_LEFT, TOP_RIGHT, CENTER, BOTTOM_LEFT, BOTTOM_RIGHT, BOTTOM),
		_9(TOP, TOP_LEFT, TOP_RIGHT, CENTER, BOTTOM_RIGHT, BOTTOM);
		
		private Collection<Segment> segments;

		Digit(final Segment...segments) {
			this.segments = Arrays.asList(segments);
		}
	}
}

// too low: 386715