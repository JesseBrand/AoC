package nl.jessebrand.aoc;

public record LPoint(long x, long y) {
	
	public LPoint add(long x, long y) {
		return new LPoint(x() + x, y() + y);
	}

	public LPoint add(LPoint p2) {
		return add(p2.x(), p2.y());
	}
	
	@Override
	public String toString() {
		return String.format("Point[%d,%d]", x(), y());
	}

	public LPoint apply(final Direction dir) {
		return new LPoint(x() + dir.getXInc(), y() + dir.getYInc());
	}
}
