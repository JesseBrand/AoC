package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.manhDistance;
import static nl.jessebrand.aoc.Utils.parsePoint;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.jessebrand.aoc.Point;

public class D15b2 {

	private static final int MIN = 0;
	private static final int MAX = 4000000;

	public static void main(String[] args) throws IOException {
		List<String> lines = readFile("2022/d15");
		final List<Tuple> sensors = constructSensors(lines);
		System.out.println(sensors);
		for (final Tuple sensor : sensors) {
			System.out.println(sensor + " : " + manhDistance(sensor.sensor(), sensor.beacon()));
		}
		Point result2 = findEmpty(sensors);
		System.out.println("Empty points detected at " + result2);
		System.out.println("Frequency = " + ((long) result2.x() * MAX + result2.y()));
	}
	
	private static Point findEmpty(List<Tuple> sensors) {
		Line[] lines = new Line[MAX+1];
		for (int l = 0; l <= MAX; l++) {
			lines[l] = new Line(l);
		}
		for (final Tuple sensor : sensors) {
			int distance = manhDistance(sensor.sensor(), sensor.beacon());
			for (int i = 0; i <= distance; i++) {
				int y = sensor.sensor().y() - distance + i;
				if (y < 0) {
					i -= y;
					y = sensor.sensor().y() - distance + i;
				}
				lines[y].addBlock(sensor, sensor.sensor().x() - i, sensor.sensor().x() + i);
			}
			for (int i = distance - 1; i >= 0; i--) {
				int y = sensor.sensor().y() + distance - i;
				if (y > MAX) {
					break;
				}
				lines[y].addBlock(sensor, sensor.sensor().x() - i, sensor.sensor().x() + i); 
			}
//			break;
		}
		return findEmptyPoint(lines);
	}

	private static Point findEmptyPoint(Line[] lines) {
		for (int i = 0; i < lines.length; i++) {
			Point result = findEmptyPoint(lines[i]);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	private static Point findEmptyPoint(Line line) {
		int x = MIN;
		while (x <= MAX) {
			boolean match = false;
			for (Block block : line.blocks()) {
				if (x >= block.minX() && x <= block.maxX()) {
					x = block.maxX() + 1;
					match = true;
					break;
				}
			}
			if (!match) {
				return new Point(x, line.y);
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
		return new Tuple(parsePoint(splitLeft[0], splitLeft[1]), parsePoint(splitRight[0], splitRight[1]));
		
	}

	static record Tuple(Point sensor, Point beacon) {}
	
	static class Line {
		private int y;
		
		private List<Block> blocks = new ArrayList<>();

		Line(int y) {
			this.y = y;
			
		}
		
		public List<Block> blocks() {
			return blocks;
		}

		public void addBlock(Tuple sensor, int minX, int maxX) {
			if (minX > maxX) {
				throw new IllegalArgumentException(minX + " > " + maxX);
			}
			Block block = new Block(minX, maxX);
			blocks.add(block);
//			System.out.println(this + " blocked at " + block + " by " + sensor);
		}

		@Override
		public String toString() {
			return "[Line with y=" + y + "]";
		}
	}
	
	static record Block(int minX, int maxX) {}
}

// 698766328 too low