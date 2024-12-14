package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.parsePoint;
import static nl.jessebrand.aoc.Utils.readFile;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import nl.jessebrand.aoc.Grid;
import nl.jessebrand.aoc.Point;

public class D14 {
	
	static final int START = 4;
	static final int INC = 101;
	static final long MSPF = 250L;

	static Grid<Character> grid;

	private static record Robot(Point p, Point dir) {} 

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d14");
		final List<Robot> robots = lines.stream().map(D14::parseRobot).toList();
		
		outputPicture(robots, 101, 103, 6771);

		int width = 101, height = 103;
		out(calculateOutcome(robots, width, height, 100));

		final JFrame frame = setupVisuals(width, height);
		new Thread(new Runner(frame, robots, width, height)).start();
	}

	private static class Runner implements Runnable {
		
		private final JFrame frame;
		private final List<Robot> robots;
		private final int width;
		private final int height;
		
		private int i = START;

		public Runner(JFrame frame, List<Robot> robots, int width, int height) {
			this.frame = frame;
			this.robots = robots;
			this.width = width;
			this.height = height;
		}

		@Override
		public void run() {
			while (true) {
				i += INC;
				grid = generateGrid(robots, width, height, i);
				out(i);
				frame.repaint();
				try {
					Thread.sleep(MSPF);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	private static JFrame setupVisuals(final int width, final int height) {
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JPanel panel = new RenderPanel(width, height);
		frame.add(panel);
		frame.setVisible(true);
		frame.setBackground(Color.GRAY);
		frame.setSize(width * 2 + 20, height * 2 + 50);
		frame.setLocationRelativeTo(null);
		return frame;
	}
	
	private static class RenderPanel extends JPanel {
		private final int width;
		private final int height;

		public RenderPanel(int width, int height) {
			this.width = width;
			this.height = height;
			setSize(width, height);
		}

		@Override
		public void paint(Graphics g) {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					g.setColor(switch (grid.get(x, y)) {
					case ' ' -> Color.WHITE;
					case '*' -> Color.BLACK;
					case '0' -> Color.RED;
					default -> Color.GRAY;
					});
					g.fillRect(x * 2, y * 2, x * 2 + 1 , y * 2 + 1);
				}
			}
		}
	}

	private static Grid<Character> generateGrid(List<Robot> robots, int gridWidth, int gridHeight, int seconds) {
		Grid<Character> grid = new Grid<>(gridWidth, gridHeight);
		grid.init(' ');
		for (final Point p : processPositions(robots, gridWidth, gridHeight, seconds)) {
			grid.set(p, grid.get(p) == ' ' ? '*' : '0');
		}
		return grid;
	}
	
	private static void outputPicture(List<Robot> robots, int gridWidth, int gridHeight, int seconds) {
		out("Iteration %d:\n%s", seconds, generateGrid(robots, gridWidth, gridHeight, seconds));
	}

	private static int calculateOutcome(List<Robot> robots, int gridWidth, int gridHeight, int seconds) {
		int[] quadrants = new int[4];
		for (final Point p : processPositions(robots, gridWidth, gridHeight, seconds)) {
			int quadrant = calcQuadrant(p.x(), p.y(), gridWidth, gridHeight);
			if (quadrant != -1) {
				quadrants[quadrant]++;
			}
		}
//		out(Arrays.toString(quadrants));
		return quadrants[0] * quadrants[1] * quadrants[2] * quadrants[3];
	}

	private static List<Point> processPositions(List<Robot> robots, int gridWidth, int gridHeight, int seconds) {
		final List<Point> result = new ArrayList<>(robots.size());
		for (final Robot robot : robots) {
			int x = (robot.p().x() + robot.dir().x() * seconds) % gridWidth;
			if (x < 0) {
				x += gridWidth;
			}
			int y = (robot.p().y() + robot.dir().y() * seconds) % gridHeight;
			if (y < 0) {
				y += gridHeight;
			}
			result.add(new Point(x, y));
		}
		return result;
	}

	private static int calcQuadrant(int x, int y, int gridWidth, int gridHeight) {
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

	private static Robot parseRobot(String line) {
		String[] split = line.split(" ");
		final Point p = parsePoint(split[0].split("=")[1]);
		final Point dir = parsePoint(split[1].split("=")[1]);
		return new Robot(p, dir);
	}
}
