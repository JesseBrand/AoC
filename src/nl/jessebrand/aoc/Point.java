package nl.jessebrand.aoc;

public record Point(int x, int y) implements HasLocation {
	
	public Point add(int x, int y) {
		return new Point(x() + x, y() + y);
	}

	public Point add(Point p2) {
		return add(p2.x(), p2.y());
	}
	
	@Override
	public String toString() {
		return String.format("Point[%d,%d]", x(), y());
	}
}
