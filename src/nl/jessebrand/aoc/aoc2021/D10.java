package nl.jessebrand.aoc.aoc2021;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class D10 {
	private static final int SCORE_MISSING_ROUND = 3;
	private static final int SCORE_MISSING_SQUARE = 57;
	private static final int SCORE_MISSING_CURLY = 1197;
	private static final int SCORE_MISSING_SHARP = 25137;
	
	public static void main(String...args) throws IOException {
		final List<String> lines = readFile("2021/d10");
		int result1 = 0;
		List<String> incompleteLines = new ArrayList<>();
		for (final String line : lines) {
			int score = evaluateScore(line);
			System.out.println(String.format("%s yielded score %d", line, score));
			if (score > 0) {
				result1 += score;
			} else {
				incompleteLines.add(line);
			}
		}
		System.out.println("Result 1: " + result1);
		System.out.println("Incomplete entries: " + incompleteLines.size());
		List<Long> incompleteScores = calculateIncompleteScores(incompleteLines);
		Collections.sort(incompleteScores);
		System.out.println(incompleteScores);
		System.out.println("Result 2: " + incompleteScores.get(incompleteScores.size() / 2));
	}

	private static List<Long> calculateIncompleteScores(List<String> lines) {
		List<Long> result = new ArrayList<>(lines.size());
		for (final String line : lines) {
			result.add(calculateIncompleteScore(line));
		}
		return result;
	}

	private static long calculateIncompleteScore(String line) {
		String eval = "";
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			switch (c) {
			case '(':
			case '[':
			case '{':
			case '<':
				eval += c;
				break;
			default:
				char matchingStart = getMatchingChar(c);
				if (eval.endsWith("" + matchingStart)) {
					eval = eval.substring(0, eval.length() - 1);
				} else {
					throw new IllegalStateException("Corrupt line! " + line);
				}
				break;
			}
		}
		System.out.print(eval);
		long result = 0;
		for (int i = eval.length() - 1; i >=0; i--) {
			char c = eval.charAt(i);
			result *= 5;
			result += switch(c) {
				case '(' -> 1;
				case '[' -> 2;
				case '{' -> 3;
				case '<' -> 4;
				default -> throw new IllegalArgumentException("Illegal Character: " + c);
			};
			System.out.print(" " + getMatchingChar(c));
		}
		System.out.println();
		return result;
	}

	private static int evaluateScore(String line) {
		String eval = "";
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			switch (c) {
			case '(':
			case '[':
			case '{':
			case '<':
				eval += c;
				break;
			default:
				char matchingStart = getMatchingChar(c);
				if (eval.endsWith("" + matchingStart)) {
					eval = eval.substring(0, eval.length() - 1);
				} else {
					switch(c) {
					case ')':
						return SCORE_MISSING_ROUND;
					case ']':
						return SCORE_MISSING_SQUARE;
					case '}':
						return SCORE_MISSING_CURLY;
					case '>':
						return SCORE_MISSING_SHARP;
					default:
						throw new IllegalStateException();
					}
				}
				break;
			}
		}
		return 0;
	}

	private static char getMatchingChar(char c) {
		if (c == ')') {
			return '(';
		}
		if (c == ']') {
			return '[';
		}
		if (c == '}') {
			return '{';
		}
		if (c == '>') {
			return '<';
		}
		if (c == '(') {
			return ')';
		}
		if (c == '[') {
			return ']';
		}
		if (c == '{') {
			return '}';
		}
		if (c == '<') {
			return '>';
		}
		throw new IllegalArgumentException("Unknown char: " + c);
	}

}

// too low: 193961064
// too low: 3629427662