package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class D09b {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d09");
		
		final int[] disk = parseFragmented(lines.get(0));
		System.out.println(Arrays.toString(disk));
		defrag(disk);
		System.out.println(Arrays.toString(disk));
		System.out.println(countResult(disk));
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
		int x = disk.length - 1;
		while (x > 0) {
			int curEnd = findLastNot(disk, -1, x);
			int val = disk[curEnd];
			int curStart = findFirst(disk, val);
			int curLength = curEnd - curStart + 1;
			output("%d is %d long (%d-%d)", val, curLength, curStart, curEnd);
			int targetStart = findFirst(disk, -1, curLength);
			if (targetStart != -1 && targetStart < curStart) {
				// TODO
				output("Moving block to %d", targetStart);
				for (int i = 0; i < curLength; i++) {
					disk[targetStart + i] = val;
					disk[curStart + i]  = -1;
				}
			}

			x = curStart - 1;
		}
	}

	private static void output(String string, Object... args) {
		System.out.println(String.format(string, args));
	}

	private static int findFirst(int[] disk, int val) {
		for (int i = 0; i < disk.length; i++) {
			if (disk[i] == val) {
				return i;
			}
		}
		return -1;
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
