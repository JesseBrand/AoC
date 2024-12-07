package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.parseLongsFromString;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class D07 {
	
	private enum Operator {
		ADD {
			@Override
			long apply(final long i0, final long i1) {
				return i0 + i1;
			}
		},
		MULTI {
			@Override
			long apply(final long i0, final long i1) {
				return i0 * i1;
			}
		},
		CONCAT {
			@Override
			long apply(final long i0, final long i1) {
				return Long.parseLong("" + i0 + i1);
			}
		};
		
		abstract long apply(final long i0, final long i1);
	}
	
	private record Calibration(long total, List<Long> values) {}

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d07");
		final List<Calibration> cals = lines.stream().map(D07::toCalibration).toList();
		
		final long result = cals.stream().filter(D07::hasSolution).mapToLong(c -> c.total()).sum();
		System.out.println(result);
	}
	
	private static Calibration toCalibration(final String line) {
		final String[] split = line.split(":");
		final long total = Long.parseLong(split[0]);
		final List<Long> longs = parseLongsFromString(split[1]);
		return new Calibration(total, longs);
	}

	private static boolean hasSolution(final Calibration cal) {
		for (final List<Operator> operatorSet : collectOperators(cal.values().size())) {
			if (applyOperators(cal, operatorSet) == cal.total()) {
				return true;
			}
		}
		return false;
	}

	private static long applyOperators(final Calibration cal, final List<Operator> operatorSet) {
		final List<Long> values = cal.values();
		long value = values.get(0);
		for (int i = 0; i < operatorSet.size(); i++) {
			value = operatorSet.get(i).apply(value, values.get(i + 1));
		}
		return value;
	}

	private static List<List<Operator>> collectOperators(final int size) {
		List<List<Operator>> curSet = Arrays.stream(Operator.values()).map(o -> Arrays.asList(o)).toList();
		for (int i = 1; i < size - 1; i++) {
			final List<List<Operator>> newSet = new ArrayList<>();
			for (final List<Operator> cur : curSet) {
				for (final Operator o : Operator.values()) {
					final List<Operator> new0 = new ArrayList<>(cur);
					new0.add(o);
					newSet.add(new0);
				}
			}
			curSet = newSet;
		}
		return curSet;
	}
	
}

// 1260333054159
// 162042343638683
