package nl.jessebrand.aoc;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public record Path<T>(List<T> points) implements Iterable<T> {
	public Path(final T p) {
		this(Arrays.asList(p));
	}

	public Path(final Path<T> parentPath, final T p) {
		this(Stream.concat(parentPath.points().stream(), Stream.of(p)).toList());
	}

	public final T first() {
		return points().get(0);
	}

	public final T last() {
		return points().get(length());
	}

	public final int length() {
		return points().size() - 1;
	}
	
	public final T get(final int index) {
		return points().get(index);
	}

	public final Stream<T> stream() {
		return points().stream();
	}

	@Override
	public final Iterator<T> iterator() {
		return points().iterator();
	}

	public final boolean contains(T object) {
		return stream().filter(o -> o.equals(object)).findAny().isPresent();
	}
	
}
