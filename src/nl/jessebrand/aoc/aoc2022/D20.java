package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.glue;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFileInts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class D20 {

	public static void main(String[] args) throws IOException {
		final List<Integer> values = readFileInts("2022/d20");
		final Container container = new Container(values);
		out(container + "\n");
		for (int entry = 0; entry < values.size(); entry++) {
			int val = values.get(entry);
			int loc = container.getLocation(entry);
			out("%d/%d: Handling %d at index %d", entry + 1, values.size(), val, loc);
			int toShift = 0;
			if (val < 0) {
				for (int i = 0; i < -val; i++) {
					toShift += container.switchLeft(loc - i);
				}
			} else {
				for (int i = 0; i < val; i++) {
					toShift += container.switchRight(loc + i);
				}
			}
			if (toShift != 0) {
//				out(container);
//				out("Shifting by %d", toShift);
				container.shift(toShift);
			}
//			out(container + "\n");
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

	private static List<Integer> buildIndices(int size) {
		final List<Integer> result = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			result.add(i);
		}
		return result;
	}
	
	static class Container {
		
		private List<Integer> values;
		private List<Integer> indices;
		private final int size;

		public Container(List<Integer> values) {
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
		
		public int get(int index) {
			return values.get(index % size);
		}

		/**
		 * Return how much to switch the entire row afterwards.
		 */
		public int switchRight(int i) {
			int index1 = (i + 1+ size) % size;
			int index2 = (i + size) % size;
//			out("Switching pos %d and %d", index1, index2);
			int val1 = values.get(index1);
			int ind1 = indices.get(index1);
			values.set(index1, values.get(index2));
			values.set(index2, val1);
			indices.set(index1, indices.get(index2));
			indices.set(index2, ind1);
			return index2 == size - 1 ? 1 : 0;
		}

		/**
		 * Return how much to switch the entire row afterwards.
		 */
		public int switchLeft(int i) {
			int index1 = (i + size + size) % size;
			int index2 = (i - 1 + size + size) % size;
//			out("Switching pos %d and %d", index1, index2);
			int val1 = values.get(index1);
			int ind1 = indices.get(index1);
			values.set(index1, values.get(index2));
			values.set(index2, val1);
			indices.set(index1, indices.get(index2));
			indices.set(index2, ind1);
			return index2 == 0 ? -1 : 0;
		}
		
		public void shift(int shift) {
			final List<Integer> newValues;
			final List<Integer> newIndices;
			if (shift < 0) {
				newValues = values.subList(-shift, size);
				newIndices = indices.subList(-shift, size);
				for (int i = shift; i < 0; i++) {
//					out("adding %d - %d = %d", i, shift, i - shift);
					newValues.add(values.get(i - shift));
					newIndices.add(indices.get(i - shift));
				}
			} else {
				newValues = new ArrayList<>(size);
				newIndices = new ArrayList<>(size);
				
				for (int i = 0; i < shift; i++) {
					newValues.add(values.get(size - i - 1));
					newIndices.add(indices.get(size - i - 1));
				}
				newValues.addAll(values.subList(0, size - shift));
				newIndices.addAll(indices.subList(0, size - shift));
			}
			values = newValues;
			indices = newIndices;
		}
		
		@Override
		public String toString() {
			return String.format("IND: [%s]\nVAL: [%s]", glue(",", indices), glue(",", values));
		}
		
	}

}

// 1845883541 too high
// -20632 wrong
// -9629 wrong
