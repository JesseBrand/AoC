package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.parseLongsFromString;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.aoc2023.D05.parseConverters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.jessebrand.aoc.aoc2023.D05.Converter;

public class D05b {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d05");
		final List<SeedRange> seedRanges = parseSeeds(parseLongsFromString(lines.get(0).substring("seeds: ".length())));
		final List<Converter> converters = parseConverters(lines);
		
		long lowest = Long.MAX_VALUE;
		for (final SeedRange seedRange : seedRanges) {
			System.out.println("SeedRange " + seedRange);
			for (long seedValue = seedRange.rangeStart(); seedValue < seedRange.rangeStart() + seedRange.rangeLength();) {
				long currentValue = seedValue;
				System.out.println("Seed " + currentValue);
				long remainingRangeLength = Long.MAX_VALUE;
				for (final Converter conv : converters) {
					remainingRangeLength = Math.min(remainingRangeLength, conv.getRemainingRangeLength(currentValue));
					currentValue = conv.convert(currentValue);
				}
				System.out.println(" Lowest remaining range = " + remainingRangeLength);
				seedValue += remainingRangeLength;
				if (currentValue < lowest) {
					lowest = currentValue;
				}
			}
			System.out.println();
		}
		System.out.println("Lowest = " + lowest);
	}

	private static List<SeedRange> parseSeeds(List<Long> longs) {
		final List<SeedRange> result = new ArrayList<>();
		for (int i = 0; i < longs.size(); i += 2) {
			result.add(new SeedRange(longs.get(i), longs.get(i + 1)));
		}
		return result;
	}

	private record SeedRange(long rangeStart, long rangeLength) {}
}
