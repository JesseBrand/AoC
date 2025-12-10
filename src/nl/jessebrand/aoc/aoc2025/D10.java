package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.allCombinations;
import static nl.jessebrand.aoc.Utils.glue;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.parseIntsFromString;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class D10 {



	public static void main(final String[] args) throws IOException {
		solve("2025/d10ex");
		solve("2025/d10");
	}

	private static void solve(final String file) throws IOException {
		final List<String> lines = readFile(file);
//		out(lines);
		final List<Machine> machines = lines.stream().map(D10::toMachine).toList();
		out(machines);

		final long totalA = machines.parallelStream().mapToInt(D10::calcSimplest).sum();
		out("Part 1: %d", totalA);
		
		final long totalB = machines.parallelStream().mapToInt(D10::calcJoltage).sum();
		out("Part 2: %d", totalB);
		
		out();
	}

	private static record Machine(String targetState, List<List<Integer>> presses, int[] joltages) {
		@Override
		public final String toString() {
			return String.format("[%s %s {%s}]", targetState, glue(" ", presses), unbox(Arrays.toString(joltages)));
		}
	}

	private static Machine toMachine(final String line) {
		final List<String> split = Arrays.asList(line.split(" "));
		final String state = unbox(split.get(0));
		final List<List<Integer>> presses = split.subList(1, split.size() - 1).stream().map(s -> unbox(s)).map(s -> parseIntsFromString(s, ",")).toList();
		final int[] joltages = parseIntsFromString(unbox(split.get(split.size() - 1)), ",").stream().mapToInt(i -> i).toArray();
		return new Machine(state, presses, joltages);
	}

	private static String unbox(final String string) {
		return string.substring(1, string.length() - 1);
	}

	private static final Comparator<List<List<Integer>>> COMPARATOR = new Comparator<>() {
		public int compare(List<List<Integer>> o1, List<List<Integer>> o2) {
			return Integer.compare(o1.size(), o2.size());
		}
	};

	private static int calcSimplest(final Machine m) {
		List<List<List<Integer>>> combis = new ArrayList<>(allCombinations(m.presses).stream().filter(l -> yieldsCorrectState(l, m.targetState)).toList());
		combis.sort(COMPARATOR);
		return combis.get(0).size();
	}

	// thought: A*

	private static int calcJoltage(final Machine m) {
		final int[] initialJoltages = new int[m.joltages().length];
		for (int i = 0; i < m.joltages().length; i++) {
			initialJoltages[i] = 0;
		}
		int result = calcJoltage(initialJoltages, 0, m.presses(), 0, m.joltages());
		out(result);
		return result;
	}
	
	private static Integer calcJoltage(final int[] curJoltages, final int presses, final List<List<Integer>> options, final int startI, final int[] targetJoltages) {
		if (Arrays.equals(curJoltages, targetJoltages)) {
			return presses;
		}
		if (disqualifies(curJoltages, targetJoltages)) {
			return null;
		}
		final List<Integer> results = IntStream.range(startI, options.size()).mapToObj(i -> {
			final List<Integer> option = options.get(i);
			final int[] newJoltages = curJoltages.clone();
			apply(newJoltages, option);
			return calcJoltage(newJoltages, presses + 1, options, i, targetJoltages);
		}).filter(Objects::nonNull).toList();
		return results.isEmpty() ? null : results.stream().mapToInt(i -> i).min().getAsInt();
	}

//	private static boolean contains(List<State> states, int[] joltages) {
//		return states.stream().filter(s -> Arrays.equals(s.joltages(), joltages)).findAny().isPresent();
//	}

	private static boolean disqualifies(int[] joltages, int[] targetJoltages) {
		for (int i = 0; i < joltages.length; i++) {
			if (joltages[i] > targetJoltages[i]) { 
				return true;
			}
		}
		return false;
	}

//	private static record State(int presses, int[] joltages, int start) {}

	private static boolean yieldsCorrectState(final List<List<Integer>> combi, final String target) {
		final Boolean[] targetState = toArray(target.chars().mapToObj(c -> c == '#').toList());
		final Boolean[] state = targetState.clone();
		for (int i = 0; i < state.length; i++) {
			state[i] = false;
		}
		for (final List<Integer> i : combi) {
			apply(state, i);
		}
		return Arrays.deepEquals(state, targetState);
	}

	private static Boolean[] toArray(List<Boolean> list) {
		final Boolean[] result = new Boolean[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	private static void apply(Boolean[] state, List<Integer> l) {
		for (int i : l) {
			state[i] = !state[i];
		}
	}

	private static void apply(int[] state, List<Integer> l) {
		for (int i : l) {
			state[i] = state[i] + 1;
		}
	}
}

// 19235
