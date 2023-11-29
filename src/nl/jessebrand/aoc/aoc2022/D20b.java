package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.glue;
import static nl.jessebrand.aoc.Utils.multiply;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFileInts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class D20b {
	
//	private static final int MULTIPLIER = 1;
//	private static final int REPEATS = 1;
	private static final long MULTIPLIER = 811589153L;
	private static final int REPEATS = 10;

	public static void main(String[] args) throws IOException {
		final List<Long> values = multiply(readFileInts("2022/d20"), MULTIPLIER);
		final int size = values.size();
		final Container container = new Container(values);
		out(container + "\n");
		for (int i = 0; i < REPEATS; i++) {
			for (int entry = 0; entry < size; entry++) {
				long value = values.get(entry);
				int curPos = container.getLocation(entry);
				out("%d/%d: Handling %d at index %d", entry + 1, size, value, curPos);
				int newPos;
				if (value == 0) {
	//				out(container + "\n");
					continue;
				}
				if (value < 0 ) {
					newPos = bound(curPos + value, size - 1);
				} else { // > 0
					newPos = bound(curPos + value, size - 1);
				}
				container.move(curPos, newPos);
//				out(container + "\n");
			}
		}
		int offset = container.indexOf(0);
		out("offset = %d ; index +1000 (%d) = %d ; index +2000 (%d) = %d ; index +3000 (%d) = %d ; result = %d",
				offset,
				offset + 1000,
				container.get(offset + 1000),
				offset + 2000,
				container.get(offset + 2000),
				offset + 3000,
				container.get(offset + 3000),
				container.get(offset + 1000) + container.get(offset + 2000) + container.get(offset + 3000));
	}

	private static int bound(final long i, final int size) {
		int r = (int) (i % size);
		while (r < 0) {
			r += size;
		}
//		if (r == 0) {
//			r = size;
//		}
		return r;
	}

	private static List<Integer> buildIndices(int size) {
		final List<Integer> result = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			result.add(i);
		}
		return result;
	}
	
	static class Container {
		
		private List<Long> values;
		private List<Integer> indices;
		private final int size;

		public Container(List<Long> values) {
			this.values = new ArrayList<>(values);
			this.size = values.size();
			this.indices = buildIndices(size);
		}

		public int getLocation(int entry) {
			for (int i = 0; i < indices.size(); i++) {
				if (indices.get(i) == entry) {
					return i;
				}
			}
			throw new IllegalStateException(String.format("Value %d not present in %s", entry, this));
		}

		public int indexOf(int val) {
			int location = -1;
			for (int i = 0; i < values.size(); i++) {
				if (values.get(i) == val) {
					location = i;
					break;
				}
			}
			if (location == -1) {
				throw new IllegalStateException(String.format("Value %d not present in %s", val, this));
			}
			return location;
		}

		public long get(int index) {
			return values.get(index % size);
		}
		
		public void move(int fromIndex, int toIndex) {
			if (fromIndex == toIndex) {
				return;
			}
			if (fromIndex < toIndex) {
				long origValue = values.get(fromIndex);
				int origIndex = indices.get(fromIndex);
				for (int i = fromIndex; i < toIndex; i++) {
					values.set(i, values.get((i + 1) % size));
					indices.set(i, indices.get((i+ 1)  % size));
				}
				values.set(toIndex % size, origValue);
				indices.set(toIndex % size, origIndex);
			}
			if (toIndex < fromIndex) {
				long origValue = values.get(fromIndex);
				int origIndex = indices.get(fromIndex);
				for (int i = fromIndex; i > toIndex; i--) {
					values.set(i, values.get((i - 1) % size));
					indices.set(i, indices.get((i - 1) % size));
				}
				values.set(toIndex % size, origValue);
				indices.set(toIndex % size, origIndex);
			}
		}
		
		@Override
		public String toString() {
//			return String.format("IND: [%s]\nVAL: [%s]", glue(",", indices), glue(",", values));
			return String.format("[%s]", glue(",", values));
		}
		
	}

}

// 1226113800 too low
// 1949525836 too low
// 6871725358451