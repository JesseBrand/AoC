package nl.jessebrand.aoc;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public record Path(List<Point> points) implements Iterable<Point> {
	public Path(final Point p) {
		this(Arrays.asList(p));
	}

	public Path(final Path parentPath, final Point p) {
		this(Stream.concat(parentPath.points().stream(), Stream.of(p)).toList());
	}

	public final Point first() {
		return points().get(0);
	}

	public final Point last() {
		return points().get(length());
	}

	public final int length() {
		return points().size() - 1;
	}
	
	public final Point get(final int index) {
		return points().get(index);
	}

	@Override
	public final Iterator<Point> iterator() {
		return points().iterator();
	}
	
}
