package nl.jessebrand.aoc;

public record Triple<T>(T l1, T l2, T l3) {
	@Override
	public String toString() {
		return String.format("%s-%s-%s", l1, l2, l3);
	}
}
