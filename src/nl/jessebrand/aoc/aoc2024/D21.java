package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.applyDirection;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.Utils.repeatChar;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import nl.jessebrand.aoc.Direction;
import nl.jessebrand.aoc.Point;

public class D21 {

	private static enum Button {
		_7(7, 0, 0),
		_8(8, 1, 0),
		_9(9, 2, 0),
		_4(4, 0, 1),
		_5(5, 1, 1),
		_6(6, 2, 1),
		_1(1, 0, 2),
		_2(2, 1, 2),
		_3(3, 2, 2),
		UP('^', 1, 3),
		_0(0, 1, 3),
		ACTIVATE('A', 2, 3),
		LEFT('<', 0, 4),
		DOWN('v', 1, 4),
		RIGHT('>', 2, 4);

		private final char c;
		private final int x;
		private final int y;
		private final Point p;

		Button(final char c, final int x, final int y) {
			this.c = c;
			this.x = x;
			this.y = y;
			this.p = new Point(x, y);
		}

		Button(final int c, final int x, final int y) {
			this(("" + c).charAt(0), x, y);
		}

		char c() {
			return c;
		}
		
		public Point p() {
			return p;
		}
	}

	public static void main(String[] args) throws IOException {
		
		debug("v<<A>>^AvA^Av<<A>>^AAv<A<A>>^AAvAA^<A>Av<A>^AA<A>Av<A<A>>^AAAvA^<A>A", 3);
		debug("<v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A", 3);
		solveFile("2024/d21ex");
//		solveFile("2024/d21");
	}
	
	private static void debug(String string, int depth) {
		final StringBuilder sb = new StringBuilder();
		Point p = getPoint('A');
		for (char c : string.toCharArray()) {
			switch(c) {
			case '<':
				p = applyDirection(p, Direction.WEST);
				break;
			case '>':
				p = applyDirection(p, Direction.EAST);
				break;
			case '^':
				p = applyDirection(p, Direction.NORTH);
				break;
			case 'v':
				p = applyDirection(p, Direction.SOUTH);
				break;
			case 'A':
				sb.append(getChar(p));
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
		out(sb.toString());
		if (depth > 1) {
			debug(sb.toString(), depth - 1);
		}
	}

	private static void solveFile(final String filename) throws IOException {
		int total = 0;
		final List<String> lines = readFile(filename);
		for (int i = 0; i < lines.size(); i++) {
			final String in = lines.get(i);
			out("Line %d: %s", i, in);
			final String r1 = solve(in);
			out("Line %d it 1: %s", i, r1);
			final String r2 = solve(r1);
			out("Line %d it 2: %s", i, r2);
			final String r3 = solve(r2);
			out("Line %d it 3: %s", i, r3);
			int num = Integer.parseInt(in.substring(0, 3));
			total += r3.length() * num;
			out("Line %d: %d * %d = %d", i, r3.length(), num, num * r3.length());
		}
		out("1: %d", total);
	}

	private static String solve(final String in) {
		final StringBuilder sb = new StringBuilder();
		Point p = getPoint('A');
		for (char c : in.toCharArray()) {
			Point newP = getPoint(c);
			sb.append(findRoute(p, newP));
			sb.append(Button.ACTIVATE.c());
			p = newP;
		}
		return sb.toString();
	}

	private static String findRoute(Point from, Point to) {
		if (from == to) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();
		// right, then up/down, then left
		sb.append(repeatChar(Button.RIGHT.c(), Math.max(0, to.x() - from.x())));
		sb.append(repeatChar(Button.UP.c(),    Math.max(0, from.y() - to.y())));
		sb.append(repeatChar(Button.DOWN.c(),  Math.max(0, to.y() - from.y())));
		sb.append(repeatChar(Button.LEFT.c(),  Math.max(0, from.x() - to.x())));
//		out("From %s to %s: %s", from, to, sb.toString());
		return sb.toString();
	}

	private static Point getPoint(final char c) {
		return Stream.of(Button.values()).filter(b -> b.c() == c).findFirst().get().p();
	}

	private static char getChar(final Point p) {
		return Stream.of(Button.values()).filter(b -> b.p().equals(p)).findFirst().get().c();
	}
}


// v<<A>>^AvA^Av<<A>>^AAv<A<A>>^AAvAA^<A>Av<A>^AA<A>Av<A<A>>^AAAvA^<A>A
// <v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A

// 181454 too high