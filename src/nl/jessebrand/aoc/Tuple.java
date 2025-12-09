package nl.jessebrand.aoc;

public record Tuple<T>(T l1, T l2) {
	@Override
	public final String toString() {
		return String.format("%s-%s", l1, l2);
	}
}
