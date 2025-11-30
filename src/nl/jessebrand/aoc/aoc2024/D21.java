package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.applyDirection;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.Utils.repeatChar;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
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
		INVALID('X', 0,3),
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

	private static record CacheEntry(String s, int depth) {
		public String toString() {
			return String.format("[%s %d]", s, depth);
		}
	}
	
	private record Route(Point from, Point to) {
		public int x() {
			return to.x() - from.x();
		}
		
		public int y() {
			return to.y() - from.y();
		}
		
		public String toString() {
			return String.format("[(%d,%d)[%c] -> (%d,%d)[%c]]", from.x(), from.y(), getChar(from), to.x(), to.y(), getChar(to));
		}
	}
	

	private static final Map<Route, String> MAPPING_ROUTES = new HashMap<>();
	private static final Map<CacheEntry, Integer> CACHE = new HashMap<>();

	public static void main(String[] args) throws IOException {
		
//		debug("v<<A>>^AvA^Av<<A>>^AAv<A<A>>^AAvAA^<A>Av<A>^AA<A>Av<A<A>>^AAAvA^<A>A", 3);
//		debug("<v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A", 3);
//		out("ex1: %d", solveFile("2024/d21ex", 0));
		out("ex1: %d", solveFile("2024/d21ex", 2)); // 126384
//		out("1: %d", solveFile("2024/d21", 2)); // 169390
//		out("2: %d", solveFile("2024/d21", 25));
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

	private static int solveFile(final String filename, final int dirKeypads) throws IOException {
		int total = 0;
		final List<String> lines = readFile(filename);
		for (final String line : lines) {
			out("Line %s", line);
			int result = solve(line, dirKeypads);
//			out("Line %d it 1: %s", lid, r);
//			for (int i = 0; i < dirKeypads; i++) {
//				r = solve(r);
////				out("Line %d it %d: %s", lid, i + 2, r);
//			}
			int num = Integer.parseInt(line.substring(0, 3));
			total += result * num;
			out("Line %s: %d * %d = %d", line, result, num, num * result);
		}
		return total;
	}

	private static int solve(final String in, final int depth) {
		return solve(new CacheEntry(in, depth));
	}

	private static int solve(final CacheEntry entry) {
		if (!CACHE.containsKey(entry)) {
			Point p = getPoint('A');
			int total = 0;
			for (char c : (entry.s() + 'A').toCharArray()) {
				Point newP = getPoint(c);
				final String flatRoute = findFlatRoute(p, newP, entry.depth());
				if (entry.depth() > 0) {
					total += solve(flatRoute, entry.depth() - 1) + 1;
				} else {
					total += flatRoute.length();
				}
				p = newP;
			}
			CACHE.put(entry, total);
//			out("Cached %s: %d", entry, total);
		}
		out("solve for %s = %s", entry, CACHE.get(entry));
		return CACHE.get(entry);
	}

	private static String solveFlat(final String in) {
		final StringBuilder sb = new StringBuilder();
		Point p = getPoint('A');
		for (char c : in.toCharArray()) {
			Point newP = getPoint(c);
			final String route = findFlatRoute(new Route(p, newP));
			sb.append(route);
			sb.append(Button.ACTIVATE.c());
			p = newP;
		}
		return sb.toString();
	}

	private static String findFlatRoute(Point from, Point to, int depth) {
		if (from == to) {
			return "";
		}
		final Route route = new Route(from, to);
		if (!MAPPING_ROUTES.containsKey(route)) {
			MAPPING_ROUTES.put(route, findShortestRoute(route));
			out("Shortest route for %s = %s", route, MAPPING_ROUTES.get(route));
		}
		return MAPPING_ROUTES.get(route);
	}
	private static String findShortestRoute(final Route route) {
		final String chars = findFlatRoute(route);
		final Set<String> permuted = permute(chars);
		final Map<String, String> potentialRoutes = permuted.stream().filter(s -> isValidRoute(route, s)).collect(Collectors.toMap(s -> s, s -> solveFlat(solveFlat(s + "A"))));
		
		return potentialRoutes.entrySet().stream().reduce((e0, e1) -> {
//			out("%s: %s (%d) vs %s: %s (%d)", e0.getKey(), e0.getValue(), e0.getValue().length(), e1.getKey(), e1.getValue(), e1.getValue().length());
			return e0.getValue().length() > e1.getValue().length() ? e1 : e0;
		}).get().getKey();
//		return potentialRoutes.entrySet().stream().reduce((e0, e1) -> e0.getValue().length() > e1.getValue().length() ? e1 : e0).get().getKey();
	}

	private static Set<String> permute(String chars) {
		final Set<String> set = new TreeSet<>();
	    if (chars.length() == 1) {
	    	set.add(chars);
		} else {
			for (int i = 0; i < chars.length(); i++) {
				final String pre = chars.substring(0, i);
				final String post = chars.substring(i + 1);
				final String remaining = pre + post;
				for (String permutation : permute(remaining)) {
					set.add(chars.charAt(i) + permutation);
				}
			}
		}
		return set;
	}

	private static String findFlatRoute(final Route route) {
		final StringBuilder sb = new StringBuilder();
		// right, then up/down, then left
		sb.append(repeatChar(Button.RIGHT.c(), Math.max(0, route.x())));
		sb.append(repeatChar(Button.UP.c(),    Math.max(0, -route.y())));
		sb.append(repeatChar(Button.DOWN.c(),  Math.max(0, route.y())));
		sb.append(repeatChar(Button.LEFT.c(),  Math.max(0, -route.x())));
//		out("From %s to %s: %s", from, to, sb.toString());
		return sb.toString();
	}

	private static Point getPoint(final char c) {
		return Stream.of(Button.values()).filter(b -> b.c() == c).findFirst().get().p();
	}

	private static char getChar(final Point p) {
		return Stream.of(Button.values()).filter(b -> b.p().equals(p)).findFirst().get().c();
	}

	private static boolean isValidRoute(final Route route, final String instructions) {
		Point p = route.from();
		for (char c : instructions.toCharArray()) {
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
			default:
				break;
			}
			if (p.equals(Button.INVALID.p())) {
				return false;
			}
		}
		return true;
	}
}


// v<<A>>^AvA^Av<<A>>^AAv<A<A>>^AAvAA^<A>Av<A>^AA<A>Av<A<A>>^AAAvA^<A>A
// <v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A

// 181454 too high
