package nl.jessebrand.aoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    public static String glue(String sep, List<?> values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                sb.append(sep);
            }
            sb.append(values.get(i).toString());
        }
        return sb.toString();
    }

    public static String glue(String sep, long[] values) {
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < values.length; i++) {
    		if (i > 0) {
    			sb.append(sep);
    		}
    		sb.append(values[i]);
    	}
    	return sb.toString();
    }

    public static String repeatChar(char c, int amount) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
    
    public static Point parsePoint(final String s) {
    	return parsePoint(s, ",");
    }

    public static Point parsePoint(final String s, final String separator) {
    	String[] split = s.split("" + separator);
    	if (split.length != 2) {
    		throw new IllegalStateException("Not a point: " + s);
    	}
    	return toPoint(split[0], split[1]);
    }

    public static Point toPoint(String x, String y) {
    	return new Point(Integer.parseInt(x), Integer.parseInt(y));
    }
    
    public static Point[] parsePoints(final String[] ss) {
    	return parsePoints(ss, ',');
    }

	public static Point[] parsePoints(final String[] ss, final char separator) {
		Point[] points = new Point[ss.length];
		for (int i = 0; i < ss.length; i++) {
			points[i] = parsePoint(ss[i]);
		}
		return points;
    }
	
	public static List<Point> parsePoints(final List<String> ss, final String separator) {
		return ss.stream().map(s -> parsePoint(s, separator)).toList();
	}
  
    public static Point3 parsePoint3(String s) {
    	String[] split = s.split(",");
    	if (split.length != 3) {
    		throw new IllegalStateException("Not a point3: " + s);
    	}
    	return parsePoint3(split[0], split[1], split[2]);
    }

    public static Point3 parsePoint3(String x, String y, String z) {
    	return new Point3(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z));
    }

    public static List<Point3> parsePoint3s(List<String> ss) {
    	return parsePoint3s(ss.toArray(new String[0]));
    }
    
    public static List<Point3> parsePoint3s(String[] ss) {
    	List<Point3> points = new ArrayList<>(ss.length);
    	for (int i = 0; i < ss.length; i++) {
    		points.add(parsePoint3(ss[i]));
    	}
    	return points;
    }
    
    public static List<String> readFile(String filename) throws IOException {
	    BufferedReader reader = Files.newBufferedReader(Paths.get("resources/" + filename + ".txt"));
	    final List<String> result = new ArrayList<>();
    	String line = null;
        while((line = reader.readLine()) != null) {
        	result.add(line);
        }
        return result;
    }

    public static List<Integer> readFileCSInts(String filename) throws IOException {
    	return Arrays.stream(readFile(filename).get(0).split(",")).map(s -> Integer.parseInt(s)).toList();
    }

    public static List<Integer> readFileInts(String filename) throws IOException {
    	return readFile(filename).stream().map(s -> Integer.parseInt(s)).toList();
    }

	public static int calculateMinX(List<Point>... lists) {
		int result = Integer.MAX_VALUE;
		for (List<Point> list : lists) {
			for (Point p : list) {
				result = Math.min(result, p.x());
			}
		}
		return result;
	}

	public static int calculateMaxX(List<Point>... lists) {
		int result = Integer.MIN_VALUE;
		for (List<Point> list : lists) {
			for (Point p : list) {
				result = Math.max(result, p.x());
			}
		}
		return result;
	}

	public static int findMin(final List<Integer> values) {
		int result = Integer.MAX_VALUE;
		for (int i : values) {
			result = Math.min(result, i);
		}
		return result;
	}

	public static int findMax(final List<Integer> values) {
		int result = 0;
		for (int i : values) {
			result = Math.max(result, i);
		}
		return result;
	}

	public static int manhDistance(Point point1, Point point2) {
		return manhDistance(point1.x(), point1.y(), point2.x(), point2.y());
	}

	public static int manhDistance(Point point1, int x2, int y2) {
		return manhDistance(point1.x(), point1.y(), x2, y2);
	}

	public static int manhDistance(int x1, int y1, int x2, int y2) {
		return Math.max(x1, x2) - Math.min(x1, x2) + Math.max(y1, y2) - Math.min(y1, y2);
	}

	public static long manhDistance(LPoint point1, LPoint point2) {
		return manhDistance(point1.x(), point1.y(), point2.x(), point2.y());
	}

	public static long manhDistance(LPoint point1, long x2, long y2) {
		return manhDistance(point1.x(), point1.y(), x2, y2);
	}

	public static long manhDistance(long x1, long y1, long x2, long y2) {
		return Math.max(x1, x2) - Math.min(x1, x2) + Math.max(y1, y2) - Math.min(y1, y2);
	}
	
	public static int countTotal(int[] values) {
		return Arrays.stream(values).reduce((a, b) -> a + b).getAsInt();
	}

	public static long countTotal(long[] values) {
		return Arrays.stream(values).reduce((a, b) -> a + b).getAsLong();
	}

	public static int countTotal(List<Integer> values) {
		return values.stream().reduce((a, b) -> a + b).get();
	}

	public static Integer nthTriNumber(int i) {
		return i * (i + 1) / 2;
	}

	public static List<String> orderAll(List<String> list) {
		List<String> result = new ArrayList<>(list.size());
		for (final String s : list) {
			result.add(order(s));
		}
		return result;
	}

	public static String order(String s) {
		final char[] chars = s.toCharArray();
		Arrays.sort(chars);
		return new String(chars);
	}

	public static <T> List<T> toGridValues(Grid<T> grid, List<Point> points) {
		List<T> result = new ArrayList<>(points.size());
		for (final Point point : points) {
			result.add(grid.get(point.x(), point.y()));
		}
		return result;
	}
	
	public static void out(final String format, final Object...args) {
		System.out.println(String.format(format, args));
	}

	public static void out(final Object o) {
		System.out.println(o);
	}

	public static <T> Set<T> set(final Set<T>... sets) {
		return set(Arrays.asList(sets));
	}

	public static <T> Set<T> set(final Collection<Set<T>> sets) {
		return sets.stream().flatMap(Collection::stream).collect(Collectors.toSet());
	}

	public static <T> Set<T> set(final Stream<Collection<T>> stream) {
		return stream.flatMap(Collection::stream).collect(Collectors.toSet());
	}

	public static List<Integer> multiply(List<Integer> ints, int multiplier) {
		return ints.stream().map(i -> i * multiplier).toList();
	}

	public static List<Long> multiply(List<Integer> ints, long multiplier) {
		return ints.stream().map(i -> (long) i * multiplier).toList();
	}
	
	public static <T> void assertEquals(long expected, long value) {
		if (expected != value) {
			throw new AssertionError(String.format("Expected %s but was %s", expected, value));
		}
	}
	
	public static <T> void assertEquals(T expected, T value) {
		if (!expected.equals(value)) {
			throw new AssertionError(String.format("Expected %s but was %s", expected, value));
		}
	}

	public static void assertTrue(boolean condition) {
		if (!condition) {
			throw new AssertionError();
		}
	}

	public static <T extends HasLocation> T findAnyAt(final Collection<T> list, final Point point) {
		return findAnyAt(list, point.x(), point.y());
	}

	public static <T extends HasLocation> T findAnyAt(final Collection<T> list, final int x, final int y) {
		for (final T item : list) {
			if (item.x() == x && item.y() == y) {
				return item;
			}
		}
		return null;
	}

	public static <T extends HasLocation> List<T> findAllAt(final Collection<T> list, final int x, final int y) {
		final List<T> result = new ArrayList<>();
		for (final T item : list) {
			if (item.x() == x && item.y() == y) {
				result.add(item);
			}
		}
		return result;
	}

	public static String substring(final List<String> lines, final int y, final int startX, final int endX) {
		if (y < 0 || y > lines.size()) {
			return "";
		}
		return lines.get(y).substring(startX, endX);
	}

	public static char charAtSafe(final List<String> lines, final int x, final int y) {
		return charAtSafe(lines, x, y, '.');
	}

	public static char charAtSafe(final List<String> lines, final int x, final int y, final char def) {
		if (y < 0 || y >= lines.size()) {
			return def;
		}
		if (x < 0 || x >= lines.get(y).length()) {
			return def;
		}
		return lines.get(y).charAt(x);
	}

	/**
	 * Standard parse using whitespace as separator.
	 */
	public static List<Integer> parseIntsFromString(final String string) {
		return parseIntsFromString(string, "\s+");
	}

	public static List<Integer> parseIntsFromString(final String string, final String separator) {
		final List<Integer> result = new ArrayList<>();
		for (String s : string.trim().split(separator)) {
			result.add(Integer.parseInt(s.trim()));
		}
		return result;
	}

	public static List<Long> parseLongsFromString(final String string, final String separator) {
		final List<Long> result = new ArrayList<>();
		for (String s : string.trim().split(separator)) {
			result.add(Long.parseLong(s.trim()));
		}
		return result;
	}

	public static List<List<Integer>> parseIntsFromStrings(final List<String> strings) {
		return parseIntsFromStrings(strings, ",");
	}

	public static List<List<Integer>> parseIntsFromStrings(final List<String> strings, final String separator) {
		return strings.stream().map(l -> parseIntsFromString(l, separator)).toList();
	}

	public static List<Long> parseLongsFromString(final String string) {
		final List<Long> result = new ArrayList<>();
		for (String s : string.trim().split("\s+")) {
			result.add(Long.parseLong(s.trim()));
		}
		return result;
	}

	public static double[] quadraticFormula(int a, long b, long c) {
		double val1 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);
		double val2 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
		return new double[] {val1, val2};
	}

	public static long calcCommonMultiple(final List<Integer> values) {
		long attempt = findMax(values);
		final List<Integer> others = new ArrayList<>(values);
		others.remove((Integer) (int) attempt);
		while (true) {
			boolean found = true;
			for (int val : others) {
				if (attempt % val != 0) {
					found = false;
					break;
				}
			}
			if (found) {
				return attempt;
			}
			attempt += values.get(0);
		}
	}

	public static boolean allEquals(final List<Integer> range, final int expected) {
		for (final int i : range) {
			if (i != expected) {
				return false;
			}
		}
		return true;
	}

	public static <T> List<T> parseLines(final List<String> lines, final Function<String, T> function) {
		return lines.stream().map(function).collect(Collectors.toList());
	}

	public static List<List<Integer>> parseColumnsAsInts(final List<String> lines) {
		final List<List<Integer>> lineArr = parseLines(lines, Utils::parseIntsFromString);
		final int length = lineArr.get(0).size();
		final List<List<Integer>> result = new ArrayList<>(length);
		for (int i = 0; i < length; i++) {
			result.add(new ArrayList<>());
		}
		for (List<Integer> line : lineArr) {
			for (int i = 0; i < length; i++) {
				result.get(i).add(line.get(i));
			}
		}
		return result;		
	}

	public static int allManhDistances(final List<Point> points) {
		int total = 0;
		for (int i = 0; i < points.size(); i++) {
			final Point p1 = points.get(i);
			for (int j = i + 1; j < points.size(); j++) {
				final Point p2 = points.get(j);
				final int distance = manhDistance(p1, p2);
//				System.out.println(String.format("Shortest from [%d] %d,%d to [%d] %d,%d = %d", points.indexOf(p1) + 1, p1.x(), p1.y(), points.indexOf(p2) + 1, p2.x(), p2.y(), distance));
				total += distance;
			}
		}
		return total;
	}

	public static long allManhDistancesL(final List<LPoint> points) {
		long total = 0;
		for (int i = 0; i < points.size(); i++) {
			final LPoint p1 = points.get(i);
			for (int j = i + 1; j < points.size(); j++) {
				final LPoint p2 = points.get(j);
				final long distance = manhDistance(p1, p2);
//				System.out.println(String.format("Shortest from [%d] %d,%d to [%d] %d,%d = %d", points.indexOf(p1) + 1, p1.x(), p1.y(), points.indexOf(p2) + 1, p2.x(), p2.y(), distance));
				total += distance;
			}
		}
		return total;
	}

	public static <T> long countOccurances(final List<Integer> list, final int i) {
		return list.stream().filter(t -> t == i).count();
	}

	public static <T> long countOccurances(final List<T> list, final T o) {
		return list.stream().filter(t -> t.equals(o)).count();
	}

	public static Grid<Character> buildCharGrid(final List<String> lines) {
		final Grid<Character> grid = new Grid<>(lines.get(0).length(), lines.size(), "");
		for (int y = 0; y < lines.size(); y++) {
			String line = lines.get(y);
			for (int x = 0; x < line.length(); x++) {
				grid.set(x, y, line.charAt(x));
			}
		}
		return grid;
	}

	public static Grid<Integer> buildIntGrid(final List<String> lines) {
		final Grid<Integer> grid = new Grid<>(lines.get(0).length(), lines.size(), "");
		for (int y = 0; y < lines.size(); y++) {
			String line = lines.get(y);
			for (int x = 0; x < line.length(); x++) {
				grid.set(x, y, Integer.parseInt(line.substring(x, x + 1)));
			}
		}
		return grid;
	}

	public static <T> List<Point> findGridPoints(final Grid<T> grid, final T o) {
		final List<Point> result = new ArrayList<>();
		for (int x = 0; x < grid.getWidth(); x++) {
			for (int y = 0; y < grid.getHeight(); y++) {
				if (grid.get(x, y) == o) {
					result.add(new Point(x, y));
				}
			}
		}
		return result;
	}
	
	public static Point applyDirection(final Point p, final Direction dir) {
		return new Point(p.x() + dir.getXInc(), p.y() + dir.getYInc());
	}


	public static <T> Set<T> findUniqueGridEntries(final Grid<T> grid) {
		final Set<T> result = new HashSet<>();
		for (int x = 0; x < grid.getWidth(); x++) {
			for (int y = 0; y < grid.getHeight(); y++) {
				result.add(grid.get(x, y));
			}
		}
		return result;
	}

	public static List<Point> getNeighbours(final Point p) {
		return Arrays.asList(
				new Point(p.x(), p.y() - 1),
				new Point(p.x() + 1, p.y()),
				new Point(p.x(), p.y() + 1),
				new Point(p.x() - 1, p.y()));
	}
}
