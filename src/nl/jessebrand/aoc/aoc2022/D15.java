package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.manhDistance;
import static nl.jessebrand.aoc.Utils.parsePoint;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.jessebrand.aoc.Point;

public class D15 {
	private static final int ROW = 2000000;

	public static void main(String[] args) throws IOException {
		List<String> lines = readFile("2022/d15");
		final List<Tuple> sensors = constructSensors(lines);
		System.out.println(sensors);
		for (final Tuple sensor : sensors) {
			System.out.println(sensor + " : " + manhDistance(sensor.sensor(), sensor.beacon()));
		}
		int result1 = calculateOccupiedAtLine(sensors, ROW);
		System.out.println(result1 + " occupied at line " + ROW);
	}
	
	private static int calculateOccupiedAtLine(List<Tuple> sensors, int y) {
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		for (Tuple sensor : sensors) {
			int dist = manhDistance(sensor.sensor(), sensor.beacon());
			minX = Math.min(minX, sensor.sensor().x() - dist);
			maxX = Math.max(maxX, sensor.sensor().x() + dist);
		}
		System.out.println(String.format("%d-%d", minX, maxX));
		int occupied = 0;
		for (int x = minX; x <= maxX; x++) {
			if (isInSensorRange(x, y, sensors)) {
				occupied++;
			}
		}
//		draw(minX, maxX, y - 2, y + 2, sensors);
		return occupied;
	}

	private static void draw(int minX, int maxX, int minY, int maxY, List<Tuple> sensors) {
		for (int y = minY; y <= maxY; y++) {
			final StringBuilder sb = new StringBuilder();
			for (int x = minX; x <= maxX; x++) {
				boolean matched = false;
				for (Tuple sensor : sensors) {
					if (sensor.sensor().x() == x && sensor.sensor().y() == y) {
						sb.append('S');
						matched = true;
						break;
					}
					if (sensor.beacon().x() == x && sensor.beacon().y() == y) {
						sb.append('B');
						matched = true;
						break;
					}
				}
				if (matched) {
					continue;
				}
				if (isInSensorRange(x, y, sensors)) {
					sb.append('#');
				} else {
					sb.append('.');
				}
			}
			System.out.println(sb.toString());
		}
	}
	
	private static Tuple lastSensor = null;
	private static int startX = 0;

	private static boolean isInSensorRange(int x, int y, List<Tuple> sensors) {
		for (Tuple sensor : sensors) {
			if (manhDistance(sensor.sensor(), x, y) <= manhDistance(sensor.sensor(), sensor.beacon())) {
				if (lastSensor != sensor) {
					if (lastSensor != null) {
						outputSensor(x - 1, y);
					}
					lastSensor = sensor;
					startX = x;
				}
//				System.out.println(String.format("%d, %d is occupied because of %s (%d <= %d)", x, y, sensor, manhDistance(sensor.sensor(), x, y), manhDistance(sensor.sensor(), sensor.beacon())));
				return true;
			}
		}
		if (lastSensor != null) {
			outputSensor(x - 1, y);
			lastSensor = null;
		}
		return false;
	}

	private static void outputSensor(int endX, int y) {
		System.out.println(String.format("X %d - %d Y %d are occupied because of %s", startX, endX, y, lastSensor));
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
		return new Tuple(parsePoint(splitLeft[0], splitLeft[1]), parsePoint(splitRight[0], splitRight[1]));
		
	}

	static record Tuple(Point sensor, Point beacon) {}
}

// 4512265 = too low
// 4777076 = wrong (low?)
// 5525847 <----
// 5525848 = too high
// 5861600 = too high

// -1246356 -  5952873
