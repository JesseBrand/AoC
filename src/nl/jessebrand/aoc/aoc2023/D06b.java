package nl.jessebrand.aoc.aoc2023;

import java.io.IOException;
import java.util.List;

import static nl.jessebrand.aoc.Utils.glue;
import static nl.jessebrand.aoc.Utils.parseIntsFromString;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.aoc2023.D06.Race;

public class D06b {
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d06");
		System.out.println(lines);
		final Race race = parseRace(lines);
		final int total = D06.mathMethod(race);
		System.out.println(total);
	}

	private static Race parseRace(List<String> lines) {
		final List<Integer> times = parseIntsFromString(lines.get(0).substring(lines.get(0).indexOf(':') + 1));
		final List<Integer> distances = parseIntsFromString(lines.get(1).substring(lines.get(1).indexOf(':') + 1));
		return new Race(Long.parseLong(glue("", times)), Long.parseLong(glue("", distances)));
	}

}
