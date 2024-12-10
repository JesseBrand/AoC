package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class D09 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d09");
		
		int[] disk = parseFragmented(lines.get(0));
		out(Arrays.toString(disk));

		defrag(disk);
		out(Arrays.toString(disk));
		out("1: %d", countResult(disk));
		
		disk = parseFragmented(lines.get(0));
		defragSegments(disk);
		out(Arrays.toString(disk));
		out("2: %d", countResult(disk));
		
	}

	private static long countResult(int[] disk) {
		long result = 0;
		for (int i = 0; i < disk.length; i++) {
			if (disk[i] != -1) {
				result += i * disk[i];
			}
		}
		return result;
	}

	private static int[] parseFragmented(String string) {
		final int length = (int) string.chars().map(c -> Integer.parseInt("" + (char) c)).sum();
		final int[] result = new int[length];
		int x = 0;
		for (int i = 0; i < string.length(); i++) {
			int value = Integer.parseInt("" + string.charAt(i));
			int id = i % 2 == 1 ? -1 : i / 2;
			for (int j = 0; j < value; j++) {
				result[x + j] = id;
			}
			x += value;
		}
		return result;
	}

	private static void defrag(int[] disk) {
		while (true) {
			int firstEmpty = findFirst(disk, -1);
			int lastNonEmpty = findLastNot(disk, -1);
			if (firstEmpty > lastNonEmpty) {
				return;
			}
//			out("Switching %d (%d) and %d (%d)", firstEmpty, disk[firstEmpty], lastNonEmpty, disk[lastNonEmpty]);
			disk[firstEmpty] = disk[lastNonEmpty];
			disk[lastNonEmpty] = -1;
		}
	}

	private static void defragSegments(int[] disk) {
		int x = disk.length - 1;
		while (x > 0) {
			int curEnd = findLastNot(disk, -1, x);
			int val = disk[curEnd];
			int curStart = findFirst(disk, val);
			int curLength = curEnd - curStart + 1;
//			out("%d is %d long (%d-%d)", val, curLength, curStart, curEnd);
			int targetStart = findFirst(disk, -1, curLength);
			if (targetStart != -1 && targetStart < curStart) {
//				out("Moving block to %d", targetStart);
				for (int i = 0; i < curLength; i++) {
					disk[targetStart + i] = val;
					disk[curStart + i]  = -1;
				}
			}

			x = curStart - 1;
		}
	}

	private static int findFirst(int[] disk, int val) {
		for (int i = 0; i < disk.length; i++) {
			if (disk[i] == val) {
				return i;
			}
		}
		throw new IllegalStateException();
	}

	private static int findLastNot(int[] disk, int val) {
		for (int i = disk.length - 1; i >= 0; i--) {
			if (disk[i] != val) {
				return i;
			}
		}
		throw new IllegalStateException();
	}

	private static int findFirst(int[] disk, int val, int length) {
		for (int i = 0; i < disk.length; i++) {
			if (disk[i] == val) {
				boolean valid = true;
				for (int j = 0; j < length; j++) {
					if (i + j >= disk.length || disk[i + j] != val) {
						valid = false;
						break;
					}
				}
				if (valid) {
					return i;
				}
			}
		}
		return -1;
	}

	private static int findLastNot(int[] disk, int val, int start) {
		for (int i = start; i >= 0; i--) {
			if (disk[i] != val) {
				return i;
			}
		}
		throw new IllegalStateException();
	}
	
}
