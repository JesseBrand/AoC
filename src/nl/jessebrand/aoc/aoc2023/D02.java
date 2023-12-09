package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.parseLines;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class D02 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d02");
		final List<Game> games = parseLines(lines, D02::parseGame);
//		System.out.println(games);
		
		int resultA = 0;
		int resultB = 0;
		for (Game game : games) {
			boolean valid = true;
			int minRed = 0;
			int minGreen = 0;
			int minBlue = 0;
			for (Attempts attempt : game.attempts()) {
				if (attempt.red() > 12 || attempt.green() > 13 || attempt.blue() > 14) {
					valid = false;
				}
				minRed = Math.max(minRed, attempt.red());
				minGreen = Math.max(minGreen, attempt.green());
				minBlue = Math.max(minBlue, attempt.blue());
			}
			if (valid) {
				resultA += game.id();
			}
			int power = minRed * minGreen * minBlue;
			resultB += power;
		}

		System.out.println("a: " + resultA);
		System.out.println("b: " + resultB);
	}

	private static Game parseGame(String line) {
		String tmp = line.substring("Game ".length());
		final int loc = tmp.indexOf(":");
		final int id = Integer.parseInt(tmp.substring(0, loc));
		tmp = tmp.substring(loc + 2);
		final String[] attemptsStr = tmp.split("\\;");
		final List<Attempts> attempts = new ArrayList<>();
		for (String tryStr : attemptsStr) {
			int red = 0;
			int green = 0;
			int blue = 0;
			String[] colors = tryStr.trim().split("\\,");
			for (String color : colors) {
				color = color.trim();
				int value = Integer.parseInt(color.substring(0, color.indexOf(" ")));
				String type = color.substring(color.indexOf(" ") + 1).trim();
				if (type.equals("red")) {
					red = value;
				} else if (type.equals("green")) {
					green = value;
				} else if (type.equals("blue")) {
					blue = value;
				} else {
					throw new IllegalArgumentException("Unknown type: \"" + type + "\"");
				}
			}
			attempts.add(new Attempts(red, green, blue));
		}
		return new Game(id, Collections.unmodifiableList(attempts));
	}


	private record Game(int id, List<Attempts> attempts) {}
	
	private record Attempts(int red, int green, int blue) {}
	
}
