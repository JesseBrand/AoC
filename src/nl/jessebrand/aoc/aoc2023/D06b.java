package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.glue;
import static nl.jessebrand.aoc.Utils.parseIntsFromString;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.List;

public class D06b {
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d06");
		System.out.println(lines);
		Race race = parseRace(lines);

		long total = 1;
		int raceCount = 0;
		for (long i = 0; i < race.time(); i++) {
			long totalTime = i * (race.time() - i);
			if (totalTime > race.distance()) {
				raceCount++;
			}
		}
		System.out.println(raceCount);
		total *= raceCount;
		System.out.println(total);
	}
	
	private static Race parseRace(List<String> lines) {
		final List<Integer> times = parseIntsFromString(lines.get(0).substring(lines.get(0).indexOf(':') + 1));
		final List<Integer> distances = parseIntsFromString(lines.get(1).substring(lines.get(1).indexOf(':') + 1));
		return new Race(Long.parseLong(glue("", times)), Long.parseLong(glue("", distances)));
	}

	private record Race(long time, long distance) {}

}
