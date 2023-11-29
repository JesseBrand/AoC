package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import nl.jessebrand.aoc.Tuple;
import nl.jessebrand.aoc.Utils;

public class D13 {

	public static void main(String[] args) throws IOException {
		final List<Tuple<ListEntry>> input = readList();
		int solution1 = 0;
		for (int i = 1; i <= input.size(); i++) {
			Result result = compareEntries(input.get(i - 1));
			System.out.println(i + ": " + result + "\n" + input.get(i - 1));
			if (result == Result.RIGHT_ORDER) {
				solution1 += i;
			}
		}
		System.out.println("Solution 1: " + solution1);
		
		List<ListEntry> fullList = new ArrayList<>();
		for (final Tuple<ListEntry> tuple : input) {
			fullList.add(tuple.l1());
			fullList.add(tuple.l2());
		}
		ListEntry d1 = new ListEntry(new ListEntry(new IntEntry(2)));
		fullList.add(d1);
		ListEntry d2 = new ListEntry(new ListEntry(new IntEntry(6)));
		fullList.add(d2);
		fullList.sort(new EntryComparator());
		System.out.println("\nSolution 2:");
		for (ListEntry e : fullList) {
			System.out.println(e);
		}
		System.out.println((fullList.indexOf(d1) + 1) + " * " + (fullList.indexOf(d2) + 1));
		System.out.print((fullList.indexOf(d1) + 1) * (fullList.indexOf(d2) + 1));
	}

	private static List<Tuple<ListEntry>> readList() throws IOException {
		final List<String> lines = readFile("2022/d13");
	    List<Tuple<ListEntry>> result = new ArrayList<>();
	    for (int i = 0; i < lines.size(); i += 3) {
        	result.add(new Tuple<ListEntry>(parseList(lines.get(i)), parseList(lines.get(i + 1))));
        }
		return result;
	}
	
	private static ListEntry parseList(String line) {
		if (!line.startsWith("[") ) {
			throw new IllegalArgumentException("\"" + line + "\" is no list");
		}
		ListEntry result = new ListEntry();
		String sub = line.substring(1, line.length() - 1);
		int oldStart = 0;
		int countLeft = 0;
		int countRight = 0;
		for (int i = 0; i < sub.length(); i++) {
			if (sub.charAt(i) == '[') {
				countLeft++;
			} else if (sub.charAt(i) == ']') {
				countRight++;
			} else if (sub.charAt(i) == ',' && countLeft == countRight) {
				result.add(parse(sub.substring(oldStart, i)));
				oldStart = i + 1;
			}
		}
		if (sub.length() != 0) {
			result.add(parse(sub.substring(oldStart, sub.length())));
		}
		return result;
	}
	
	private static Entry parse(String line) {
		try {
			int i = Integer.parseInt(line);
			return new IntEntry(i);
		} catch(NumberFormatException e) {
			return parseList(line);
		}
	}

	private static Result compareEntries(Tuple<ListEntry> tuple) {
		return compareEntries(tuple.l1(), tuple.l2());
	}
	
	private static Result compareEntries(IntEntry i1, IntEntry i2) {
		if (i1.value < i2.value) {
			return Result.RIGHT_ORDER;
		}
		if (i1.value > i2.value) {
			return Result.WRONG_ORDER;
		}
		return Result.UNDEFINED;
	}
	
	private static Result compareEntries(Entry e1, Entry e2) {
		if (e1 instanceof IntEntry) {
			if (e2 instanceof IntEntry) {
				return compareEntries((IntEntry) e1, (IntEntry) e2);
			}
			return compareEntries(new ListEntry(e1), (ListEntry) e2);
		}
		if (e2 instanceof IntEntry) {
			return compareEntries((ListEntry) e1, new ListEntry(e2));
		}
		return compareEntries((ListEntry) e1, (ListEntry) e2);
		
	}
	
	private static Result compareEntries(ListEntry l1, ListEntry l2) {
		int length = Math.min(l1.size(),  l2.size());
		for (int i = 0; i < length; i++) {
			Result result = compareEntries(l1.get(i), l2.get(i));
			if (result != Result.UNDEFINED) {
				return result;
			}
		}
		if (l1.size() < l2.size()) {
			return Result.RIGHT_ORDER;
		}
		if (l1.size() > l2.size()) {
			return Result.WRONG_ORDER;
		}
		return Result.UNDEFINED;
	}

	static interface Entry {}
	
	static class IntEntry implements Entry {
		private final int value;

		IntEntry(int value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return "" + value;
		}
	}
	
	static class ListEntry implements Entry {
		private List<Entry> entries = new ArrayList<>();
		
		ListEntry() {}
		
		ListEntry(Entry entry) {
			entries.add(entry);
		}
		
		void add(Entry e) {
			entries.add(e);
		}
		
		public Entry get(int i) {
			return entries.get(i);
		}

		public int size() {
			return entries.size();
		}

		@Override
		public String toString() {
			return "[" + Utils.glue(",", entries) + "]";
		}
	}
	
	static enum Result {
		RIGHT_ORDER,
		UNDEFINED,
		WRONG_ORDER
	}

	
	static class EntryComparator implements Comparator<Entry> {
		@Override
		public int compare(Entry o1, Entry o2) {
			Result result = compareEntries(o1, o2);
			return switch(result) {
				case RIGHT_ORDER -> -1;
				case WRONG_ORDER -> 1;
				default -> throw new IllegalStateException();
			};
		}
	}
}


// 22425 = too high