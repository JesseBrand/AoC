package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.parsePoint;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.Utils.visualize;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.List;

import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;

public class D14 {
	
	private static final int SCALE = 3;

	static final int START = 4;
	static final int INC = 101;
	static final long MSPF = 250L;

	private static Grid<Character> grid;
	private static List<Robot> robots;

	private static record Robot(Point p, Point dir) {} 

	public static void main(String[] args) throws IOException {
		out(calculateQuadrantOutcome(readFile("2024/d14ex").stream().map(D14::parseRobot).toList(), 11, 7, 100));
		
		final List<String> lines = readFile("2024/d14");
		robots = lines.stream().map(D14::parseRobot).toList();
		
		outputPicture(robots, 101, 103, 6771);

		final int width = 101, height = 103;
		grid = new Grid<>(width, height, ' ');
		visualize("2024 D14", width * SCALE, height * SCALE, D14::render, D14::processFrame, MSPF);
	}

	private static void processFrame(final int frameIndex) {
		final int i = START + frameIndex * INC;
		grid = generateGrid(robots, grid.getWidth(), grid.getHeight(), i);
		out(i);
	}

	public static void render(final Graphics g) {
		for (int y = 0; y < grid.getHeight(); y++) {
			for (int x = 0; x < grid.getWidth(); x++) {
				g.setColor(switch (grid.get(x, y)) {
				case ' ' -> Color.WHITE;
				case '*' -> Color.BLACK;
				case '0' -> Color.RED;
				default -> Color.GRAY;
				});
				g.fillRect(x * SCALE, y * SCALE, x * SCALE + 1 , y * SCALE + 1);
			}
		}
	}

	private static Grid<Character> generateGrid(final List<Robot> robots, final int gridWidth, final int gridHeight, final int seconds) {
		final Grid<Character> grid = new Grid<>(gridWidth, gridHeight, ' ');
		robots.stream().map(r -> processPosition(r, gridWidth, gridHeight, seconds)).forEach(p -> grid.set(p, grid.get(p) == ' ' ? '*' : '0'));
		return grid;
	}
	
	private static void outputPicture(final List<Robot> robots, final int gridWidth, final int gridHeight, final int seconds) {
		out("Iteration %d:\n%s", seconds, generateGrid(robots, gridWidth, gridHeight, seconds));
	}

	private static int calculateQuadrantOutcome(final List<Robot> robots, final int gridWidth, final int gridHeight, final int seconds) {
		final int[] quadrants = new int[4];
		robots.stream().map(r -> processPosition(r, gridWidth, gridHeight, seconds)).mapToInt(p -> calcQuadrant(p.x(), p.y(), gridWidth, gridHeight)).filter(q -> q != -1).forEach(q -> quadrants[q]++);
//		out(Arrays.toString(quadrants));
		return quadrants[0] * quadrants[1] * quadrants[2] * quadrants[3];
	}

	private static Point processPosition(final Robot robot, final int gridWidth, final int gridHeight, final int seconds) {
		int x = (robot.p().x() + robot.dir().x() * seconds) % gridWidth;
		if (x < 0) {
			x += gridWidth;
		}
		int y = (robot.p().y() + robot.dir().y() * seconds) % gridHeight;
		if (y < 0) {
			y += gridHeight;
		}
		return new Point(x, y);
	}

	private static int calcQuadrant(final int x, final int y, final int gridWidth, final int gridHeight) {
//		out("%d,%d", x, y);
		if (x < gridWidth / 2 && y < gridHeight / 2) {
			return 0;
		}
		if (x > gridWidth / 2 && y < gridHeight / 2) {
			return 1;
		}
		if (x < gridWidth / 2 && y > gridHeight / 2) {
			return 2;
		}
		if (x > gridWidth / 2 && y > gridHeight / 2) {
			return 3;
		}
		return -1;
	}

	private static Robot parseRobot(final String line) {
		final String[] split = line.split(" ");
		final Point p = parsePoint(split[0].split("=")[1]);
		final Point dir = parsePoint(split[1].split("=")[1]);
		return new Robot(p, dir);
	}
}
