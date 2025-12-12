package nl.jessebrand.aoc;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Grid<T> implements Iterable<Point> {

	private final T[][] values;
	private final String separator;

	public Grid(int width, int height) {
		this(width, height, "");
	}

	public Grid(int width, int height, T initValue) {
		this(width, height, "");
		init(initValue);
	}

	public Grid(int width, int height, String separator) {
		values = (T[][]) new Object[height][width];
		this.separator = separator;
	}

	public void set(final int x, final int y, final T value) {
		values[y][x] = value;
	}

	public void set(final Point p, final T value) {
		set(p.x(), p.y(), value);
	}
	
	public int getWidth() {
		return values[0].length;
	}
	
	public int getHeight() {
		return values.length;
	}

	public void init(T initValue) {
		for (int y = 0; y < getHeight(); y++) { 
			for (int x = 0; x < getWidth(); x++) {
				set(x, y, initValue);
			}
		}
	}

	public Object[] row(final int y) {
		return values[y];
	}
	
	public T get(Point loc) {
		return get(loc.x(), loc.y());
	}
	
	public T get(final int x, final int y) {
		if (!contains(x, y)) {
			throw new IllegalStateException(String.format("x out of bounds: %d (%d - %d)", x, 0, getWidth()));
		}
		return (T) values[y][x];
	}

	/**
	 * Does not work for primitives.
	 */
	public T getOrNull(final int x, final int y) {
		return getOr(x, y, null);
	}
	
	public T getOr(final Point p, final T def) {
		return getOr(p.x(), p.y(), def);
	}

	public T getOr(final int x, final int y, final T def) {
		if (contains(x, y)) {
			return (T) values[y][x];
		}
		return def;
	}
	
	public boolean contains(final Point p) {
		return contains(p.x(), p.y());
	}

	public boolean contains(int x, int y) {
		return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				final T o = get(x, y);
				sb.append(o instanceof Boolean ? o == Boolean.TRUE ? "#" : "." : o + separator);
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	@Override
	public final Grid<T> clone() {
		return convert(o -> o);
	}

	@Override
	public final boolean equals(final Object obj) {
		if (obj == null || obj.getClass() != Grid.class) {
			return false;
		}
		Grid<T> other = (Grid<T>) obj;
		// TODO: type
		if (other.getWidth() != getWidth() || other.getHeight() != getHeight()) {
			return false;
		}
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getWidth(); y++) {
				if (!get(x, y).equals(other.get(x, y))) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return values.hashCode(); // TODO: not correct
	}
	
	public final Stream<Point> stream() {
		return IntStream.range(0, getHeight()).mapToObj(y -> {
			return IntStream.range(0, getWidth()).mapToObj(x -> new Point(x, y)).toList();
		}).flatMap(Collection::stream);
	}

	@Override
	public Iterator<Point> iterator() {
		return stream().toList().iterator();
	}

	public <T2> Grid<T2> convert(Function<T, T2> mapper) {
		final Grid<T2> result = new Grid<>(getWidth(), getHeight(), separator);
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				final T curValue = get(x, y);
				result.set(x, y, mapper.apply(curValue));
			}
		}
		return result;
	}

	public final Grid<T> flipX() {
		final Grid<T> result = new Grid<T>(getWidth(), getHeight());
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				result.set(x, y, get(getWidth() - x - 1, y));
			}
		}
		return result;
	}

	public final Grid<T> rotateClockwise() {
		final Grid<T> result = new Grid<T>(getHeight(), getWidth());
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				result.set(x, y, get(y, getHeight() - x - 1));
			}
		}
		return result;
	}
}
