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
			long apply(long i0, long i1) {
				return i0 + i1;
			}
		},
		MULTI {
			@Override
			long apply(long i0, long i1) {
				return i0 * i1;
			}
		},
		CONCAT {
			@Override
			long apply(long i0, long i1) {
				return Long.parseLong("" + i0 + i1);
			}
		};
		
		abstract long apply(long i0, long i1);
	}
	
	private record Calibration(long total, List<Long> values) {}

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d07");
		final List<Calibration> cals = lines.stream().map(D07::toCal).toList();
		
		final long result = cals.stream().filter(D07::hasSolution).mapToLong(c -> c.total()).sum();
		System.out.println(result);
	}
	
	private static Calibration toCal(final String line) {
		final String[] split = line.split(":");
		final long total = Long.parseLong(split[0]);
		final List<Long> longs = parseLongsFromString(split[1]);
		return new Calibration(total, longs);
	}

	private static boolean hasSolution(final Calibration cal) {
		System.out.println(cal);
		for (final List<Operator> operatorSet : collectOperators(cal)) {
//			System.out.println(operatorSet);
			long result = applyOperators(cal, operatorSet);
//			System.out.println(result);
			if (result == cal.total()) {
				return true;
			}
		}
//		System.out.println();
		return false;
	}

	private static long applyOperators(Calibration cal, List<Operator> operatorSet) {
		final List<Long> values = cal.values();
		long value = values.get(0);
		for (int i = 0; i < operatorSet.size(); i++) {
			value = operatorSet.get(i).apply(value, values.get(i + 1));
		}
		return value;
	}

	private static List<List<Operator>> collectOperators(Calibration cal) {
		final int size = cal.values().size();
		List<List<Operator>> curSet = null;
		for (int i = 0; i < size - 1; i++) {
			List<List<Operator>> newSet = new ArrayList<>();
			if (curSet == null) {
				newSet = Arrays.stream(Operator.values()).map(o -> Arrays.asList(o)).toList();
			} else {
				for (final List<Operator> cur : curSet) {
					for (final Operator o : Operator.values()) {
						List<Operator> new0 = new ArrayList<>(cur);
						new0.add(o);
						newSet.add(new0);
					}
				}
			}
			curSet = newSet;
		}
		return curSet;
	}
	
}

// 1260333054159
// 162042343638683