package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.*;

import java.io.IOException;
import java.util.List;

public class D01 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d01");
		List<List<Integer>> cols = parseColumnsAsInts(lines);
		for (List<Integer> list : cols) {
			list.sort(null);
		}
		System.out.println(cols);
		final int size = cols.get(0).size();
		final List<Integer> list1 = cols.get(0);
		final List<Integer> list2 = cols.get(1);
		
		int total = 0;
		for (int i = 0; i < size; i++) {
			total += Math.abs(list1.get(i) - list2.get(i));
		}
		System.out.println(total);
		
		int total2 = 0;
		for (int i : list1) {
			total2 += i * countOccurances(list2, i);
		}
		System.out.println(total2);
	}

	private static <T> int countOccurances(List<T> list1, T i) {
		int total = 0;
		for (T t: list1) {
			if (t.equals(i)) {
				total++;
			}
		}
		return total;
	}
}
