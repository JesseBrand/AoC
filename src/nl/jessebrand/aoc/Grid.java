package nl.jessebrand.aoc;

public class Grid<T> {

	private final Object[][] values;
	private final String separator;

	public Grid(int width, int height, String separator) {
		values = new Object[height][width];
		this.separator = separator;
	}

	public void set(int x, int y, T value) {
		values[y][x] = value;
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
	
	public T get(int x, int y) {
		if (x < 0 || x >= getWidth()) {
			throw new IllegalStateException(String.format("x out of bounds: %d (%d - %d)", x, 0, getWidth()));
		}
		if (y < 0 || y >= getHeight()) {
			throw new IllegalStateException(String.format("y out of bounds: %d (%d - %d)", y, 0, getHeight()));
		}
		return (T) values[y][x];
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
}