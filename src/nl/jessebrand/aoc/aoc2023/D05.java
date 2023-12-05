package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.assertTrue;
import static nl.jessebrand.aoc.Utils.parseLongsFromString;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class D05 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d05");
		System.out.println(lines);
		
		final List<Long> seeds = parseLongsFromString(lines.get(0).substring("seeds: ".length()));
		System.out.println(seeds);
		final List<Converter> converters = parseConverters(lines);
		
		long lowest = Long.MAX_VALUE;
		for (final long seed : seeds) {
			long currentValue = seed;
			System.out.println("Seed " + currentValue);
			for (final Converter conv : converters) {
				currentValue = conv.convert(currentValue);
				System.out.println("Converter " + conv.name() + " converted to value " + currentValue);
			}
			if (currentValue < lowest) {
				lowest = currentValue;
			}
		}
		System.out.println("Lowest = " + lowest);
	}

	static List<Converter> parseConverters(List<String> lines) {
		final List<Converter> converters = new ArrayList<>();
		Converter converter = null;
		for (int i = 2; i < lines.size(); i++) {
			String line = lines.get(i);
			if (converter == null) {
				String name = line.substring(0, line.indexOf(" map"));
				converter = new Converter(name);
				converters.add(converter);
			} else if (line.isBlank()) {
				converter = null;
			} else {
				converter.addRange(new ConversionRange(parseLongsFromString(line)));
			}
		}
		return converters;
	}

	static class Converter {
		
		private List<ConversionRange> ranges = new ArrayList<>();
		private String name;
		
		Converter(String name) {
			this.name = name;
		}
		
		String name() {
			return name;
		}
		
		void addRange(final ConversionRange range) {
			ranges.add(range);
		}
		
		long convert(long sourceValue) {
			for (ConversionRange range : ranges) {
				if (sourceValue >= range.sourceStart() && sourceValue < range.sourceStart() + range.rangeLength()) {
					return range.destinationStart() + sourceValue - range.sourceStart();
				}
			}
			return sourceValue;
		}
		
		long getRemainingRangeLength(long seedValue) {
			for (ConversionRange range : ranges) {
				if (seedValue >= range.sourceStart() && seedValue < range.sourceStart() + range.rangeLength()) {
					System.out.println(String.format(" %d in range %d - %d. Range left: %d", seedValue, range.sourceStart(), range.sourceStart() + range.rangeLength(), range.rangeLength() - (seedValue - range.sourceStart())));
					return range.rangeLength() - (seedValue - range.sourceStart());
				}
			}
			long lowestNext = Long.MAX_VALUE;
			for (ConversionRange range : ranges) {
				if (range.sourceStart() > seedValue) {
					lowestNext = Math.min(lowestNext, range.sourceStart());
				}
			}
			System.out.println(" Not in range, next range starts at: " + lowestNext);
			return lowestNext;
		}
	}
	
	static record ConversionRange(long destinationStart, long sourceStart, long rangeLength) {

		public ConversionRange(List<Long> longs) {
			this(longs.get(0), longs.get(1), longs.get(2));
			assertTrue(longs.size() == 3);
		}
	}
}
