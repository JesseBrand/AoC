package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.findMax;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.parsePoint3s;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.jessebrand.aoc.Point3;

public class D18 {

	public static void main(String[] args) throws IOException {
		List<String> lines = readFile("2022/d18");
		List<Point3> points = parsePoint3s(lines);
		out("Points: %s", points);

		List<Bubble> bubbles = findAirBubbles(points);
		out("Found total of %d bubbles", bubbles.size());

		int result1 = 0;
		int result2 = 0;
		for (Point3 p : points) {
			int neighbours = findNeighbours(p, points);
			int bubbleNeighbours = findBubbleNeighbours(p, bubbles);
			int area1 = 6 - neighbours;
			int area2 = 6 - neighbours - bubbleNeighbours;
			out("Drop %s neighbours %d drops and %d bubbles, surface area: %d / %d", p, neighbours, bubbleNeighbours, area1, area2);
			result1 += area1;
			result2 += area2;
		}
		out("Result 1: %d", result1);
		out("Result 2: %d", result2);
	}

	private static List<Bubble> findAirBubbles(List<Point3> points) {
		int maxX = findMax(points.stream().map(p -> p.x()).toList());
		int maxY = findMax(points.stream().map(p -> p.y()).toList());
		int maxZ = findMax(points.stream().map(p -> p.z()).toList());

		out("%d drops", points.size());

		final List<Point3> potentialBubbles = new ArrayList<>();
		for (final Point3 p : points) {
			Point3 nextX = points.stream().filter(p2 -> p.y() == p2.y() && p.z() == p2.z() && p2.x() > p.x() + 1).reduce((p2, p3) -> p2.x() < p3.x() ? p2 : p3).orElse(null);
			if (nextX != null) {
				potentialBubbles.add(new Point3(p.x() + 1, p.y(), p.z()));
//				out("found points to right of %s: %s", p, nextX);
//			} else {
//				out("No points found to right of %s", p);
			}
			
			
		}
		out("Found %d potential bubbles", potentialBubbles.size());

		final List<Bubble> result = new ArrayList<>();
		
		for (final Point3 potential : potentialBubbles) {
			out("\nChecking to find bubble around %s", potential);
			final List<Point3> examinedPoints = new ArrayList<>();
			final List<Point3> bubblePoints = new ArrayList<>();
			Set<Point3> nextToExamine = new HashSet<>();
			nextToExamine.add(potential);
			boolean match = true;
			while (!nextToExamine.isEmpty()) {
				out("Examining %d points", nextToExamine.size());
				final Set<Point3> nextNextToExamine = new HashSet<>();
				for (Point3 toExamine : nextToExamine) {
					if (examinedPoints.contains(toExamine)) {
						continue;
					}
					examinedPoints.add(toExamine);
					if (points.contains(toExamine)) {
						out("There is a point here, ignoring");
						continue;
					}
					if (toExamine.x() <= 0 || toExamine.x() >= maxX
							|| toExamine.y() <= 0 || toExamine.y() >= maxY
							|| toExamine.z() <= 0 || toExamine.z() >= maxZ) {
						out("Bubble is leaking at %s", toExamine);
						match = false;
						break;
					}
					for (Bubble bubble : result) {
						if (bubble.points().contains(toExamine)) {
							out("Bubble overlaps other bubble");
							match = false;
							break;
						}
					}
					if (!match) {
						break;
					}
					out("Added as bubble point: %s", toExamine);
					bubblePoints.add(toExamine);
					nextNextToExamine.addAll(getSurrounding6(toExamine));
				}
				nextToExamine = nextNextToExamine;
			}
			if (!match) {
				out("Bubble is invalid, discarded");
			} else if (bubblePoints.isEmpty()) {
				out("Bubble has no points, discarded");
			} else {
				out("Bubble located with points %s", bubblePoints);
				result.add(new Bubble(bubblePoints));
			}
		}
		return result;
	}

	private static int findNeighbours(Point3 p, List<Point3> points) {
		int result = 0;
		for (Point3 p2 : points) {
			if (((p.x() == p2.x() - 1 || p.x() == p2.x() + 1) && p.y() == p2.y() && p.z() == p2.z())
					|| (p.x() == p2.x() && (p.y() == p2.y() - 1 || p.y() == p2.y() + 1) && p.z() == p2.z())
					|| (p.x() == p2.x() && p.y() == p2.y() && (p.z() == p2.z() - 1 || p.z() == p2.z() + 1))) {
				result++;
			}
		}
		return result;
	}
	
	private static int findBubbleNeighbours(Point3 p, List<Bubble> bubbles) {
		final List<Point3> neighbourPoints = getSurrounding6(p);
		int result = 0;
		for (Point3 bp : neighbourPoints) {
			for (Bubble bubble : bubbles) {
				if (bubble.points().contains(bp)) {
					result++;
					break;
				}
			}
		}
		return result;
	}

	private static List<Point3> getSurrounding6(Point3 p) {
		final List<Point3> neighbourPoints = Arrays.asList(
				new Point3(p.x() - 1, p.y(), p.z()),
				new Point3(p.x() + 1, p.y(), p.z()),
				new Point3(p.x(), p.y() - 1, p.z()),
				new Point3(p.x(), p.y() + 1, p.z()),
				new Point3(p.x(), p.y(), p.z() - 1),
				new Point3(p.x(), p.y(), p.z() + 1)
		);
		return neighbourPoints;
	}
	
	static record Bubble(List<Point3> points) {}
}
