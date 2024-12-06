package nl.jessebrand.aoc;

public final class Grid<T> {

	private final Object[][] values;
	private final String separator;

	public Grid(int width, int height, String separator) {
		values = new Object[height][width];
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
				sb.append(get(x, y) + separator);
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	@Override
	public final Grid<T> clone() {
		final Grid<T> result = new Grid<T>(getWidth(), getHeight(), separator);
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				result.set(x, y, get(x, y));
			}
		}
		return result;
	}
}
