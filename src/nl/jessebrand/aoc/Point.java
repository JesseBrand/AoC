package nl.jessebrand.aoc;

public record Point(int x, int y) implements HasLocation, Comparable<Point> {
	
	public Point add(final int x, final int y) {
		return new Point(x() + x, y() + y);
	}

	public Point add(final Point p2) {
		return add(p2.x(), p2.y());
	}
	
	@Override
	public String toString() {
		return String.format("[%d,%d]", x(), y());
	}

	@Override
	public int compareTo(final Point p2) {
		if (y() != p2.y()) {
			return Integer.compare(y(), p2.y());
		}
		return Integer.compare(x(), p2.x());
	}
}
