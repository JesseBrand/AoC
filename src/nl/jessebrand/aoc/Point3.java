package nl.jessebrand.aoc;

public record Point3(int x, int y, int z) {

	@Override
	public String toString() {
		return String.format("[%d,%d,%d]", x(), y(), z());
	}
}
