package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.manhDistance;
import static nl.jessebrand.aoc.Utils.toPoint;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.jessebrand.aoc.Point;

public class D15b {

	public static void main(String[] args) throws IOException {
		List<String> lines = readFile("2022/d15");
		final List<Tuple> sensors = constructSensors(lines);
		System.out.println(sensors);
		for (final Tuple sensor : sensors) {
			System.out.println(sensor + " : " + manhDistance(sensor.sensor(), sensor.beacon()));
		}
		Point empty = findEmpty(sensors);
		System.out.println("Empty opint detected at " + empty);
	}
	
	private static Point findEmpty(List<Tuple> sensors) {
		int min = 0;
		int max = 4000000;
		System.out.println(String.format("%d-%d", min, max));
		for (int y = min; y <= max; y++) {
			if (y > 0 && y % 100 == 0) {
				System.out.println(String.format("y %d-%d: Nothing found", y - 99, y));
			}
			for (int x = min; x <= max; x++) {
				Tuple sensor = isInSensorRange(x, y, sensors);
				if (sensor == null) {
					return new Point(x, y);
				}
				x = sensor.sensor().x() + (sensor.sensor().x() - x) + 1;
			}
		}
		throw new IllegalStateException("No empty point found in search space");
	}

	private static Tuple isInSensorRange(int x, int y, List<Tuple> sensors) {
		for (Tuple sensor : sensors) {
			if (manhDistance(sensor.sensor(), x, y) <= manhDistance(sensor.sensor(), sensor.beacon())) {
				return sensor;
			}
		}
		return null;
	}

	private static List<Tuple> constructSensors(List<String> lines) {
		final List<Tuple> result = new ArrayList<>();
		for (final String line : lines) {
			result.add(constructTuple(line));
		}
		return result;
	}

	private static Tuple constructTuple(final String line) {
		String[] split1 = line.substring("Sensor at x=".length()).split(": closest beacon is at x=");
		String[] splitLeft = split1[0].split(", y=");
		String[] splitRight = split1[1].split(", y=");
		return new Tuple(toPoint(splitLeft[0], splitLeft[1]), toPoint(splitRight[0], splitRight[1]));
		
	}

	static record Tuple(Point sensor, Point beacon) {}
}

// 4512265 = too low
// 4777076 = wrong (low?)
// 5525847 <----
// 5525848 = too high
// 5861600 = too high

// -1246356 -  5952873
