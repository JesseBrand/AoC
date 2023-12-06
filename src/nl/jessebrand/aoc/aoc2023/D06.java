package nl.jessebrand.aoc.aoc2023;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nl.jessebrand.aoc.Utils.parseIntsFromString;
import static nl.jessebrand.aoc.Utils.quadraticFormula;
import static nl.jessebrand.aoc.Utils.readFile;

public class D06 {
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d06");
		System.out.println(lines);
		List<Race> races = parseRaces(lines);
		System.out.println(races);

		int total = 1;
		for (Race race : races) {
			total *= mathMethod(race);
		}
		System.out.println(total);
	}

	static int countMethod(final Race race) {
		int raceCount = 0;
		for (int i = 0; i < race.time(); i++) {
			long totalTime = i * (race.time() - i);
			if (totalTime > race.distance()) {
				raceCount++;
			}
		}
		System.out.println(raceCount);
		return raceCount;
	}

	static int mathMethod(final Race race) {
//		x * (time - x) - distance = 0
//		a = 1; b = -time; c = distance
		final double[] formulaResult = quadraticFormula(1, -race.time(), race.distance());
		final int result = (int) Math.ceil(formulaResult[1]) - (int) Math.floor(formulaResult[0]) - 1;;
		System.out.println(String.format("%s -> %d-%d -> %d",
				Arrays.toString(formulaResult),
				(int) Math.floor(formulaResult[0]), (int) Math.ceil(formulaResult[1]), result));
		return result;
	}

	private static List<Race> parseRaces(List<String> lines) {
		List<Integer> times = parseIntsFromString(lines.get(0).substring(lines.get(0).indexOf(':') + 1));
		List<Integer> distances = parseIntsFromString(lines.get(1).substring(lines.get(1).indexOf(':') + 1));
		List<Race> races = new ArrayList<>();
		for (int i = 0; i < times.size(); i++) {
			races.add(new Race(times.get(i), distances.get(i)));
		}
		return races;
	}

	static record Race(long time, long distance) {}

}
