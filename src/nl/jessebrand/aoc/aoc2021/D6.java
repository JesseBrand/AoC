package nl.jessebrand.aoc.aoc2021;

import static nl.jessebrand.aoc.Utils.countTotal;
import static nl.jessebrand.aoc.Utils.glue;
import static nl.jessebrand.aoc.Utils.readFileCSInts;

import java.io.IOException;
import java.util.List;

public class D6 {
	
	private static final int DAYS = 256;

	public static void main(String[] args) throws IOException {
		final List<Integer> input = readFileCSInts("2021/d6");
		long[] state = loadState(input);
		System.out.println(String.format("State after 0 days: %d (%s)", countTotal(state), glue(",", state)));
		for (int i = 0; i < DAYS; i++) {
			state = processDay(state);
			System.out.println(String.format("State after %d days: %d (%s)", i + 1, countTotal(state), glue(",", state)));
		}
	}

	private static long[] loadState(List<Integer> input) {
		final long[] result = new long[9];
		for (final int i : input) {
			result[i]++;
		}
		return result;
	}

	private static long[] processDay(long[] state) {
		return new long[] {state[1], state[2], state[3], state[4], state[5], state[6], state[7] + state[0], state[8], state[0]};
	}
}

// 1050444 too high