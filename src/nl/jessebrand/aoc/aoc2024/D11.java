package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.parseLongsFromString;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D11 {
	
	private static final List<Long> LIST_1 = Arrays.asList(1L);
	private static final List<Map<Long, Long>> DEEP_MAPPED = new ArrayList<>();
	
	private static enum Operation {
		TO_1 {
			@Override
			public List<Long> apply(final Long i) {
				return LIST_1;
			}
		},
		SPLIT {
			@Override
			public List<Long> apply(final Long i) {
				final String s = "" + i;
				return Arrays.asList(Long.parseLong(s.substring(0, s.length() / 2)), Long.parseLong(s.substring(s.length() / 2)));
			}
		},
		TIMES_2024 {
			@Override
			public List<Long> apply(final Long i) {
				return Arrays.asList(i * 2024);
			}
		};
		
		public abstract List<Long> apply(final Long i);
		
		public static List<Long> applyAny(final Long i) {
			return getByLong(i).apply(i);
		}
		
		public static Operation getByLong(final Long i) {
			if (i == 0) {
				return TO_1;
			}
			if (("" + i).length() % 2 == 0) {
				return SPLIT;
			}
			return TIMES_2024;
		}
	}

	public static void main(String[] args) throws IOException {
		final long start = System.currentTimeMillis();
		final List<String> lines = readFile("2024/d11");
		List<Long> set = parseLongsFromString(lines.get(0));
		out("1: %s", solveDFS(set, 25));
		out("2: %s", solveDFS(set, 75));
		out("Finished in %dms", System.currentTimeMillis() - start);
	}

	private static long solveDFS(final List<Long> set, final int rep) {
		while (DEEP_MAPPED.size() < rep) {
			DEEP_MAPPED.add(new HashMap<>());
		}
		return processDFS(set, rep);
	}

	private static long processDFS(final List<Long> set, final int rep) {
		return set.stream().mapToLong(i -> processDFS(i, rep)).sum();
	}

	private static long processDFS(final long i, final int rep) {
		if (rep == 0) {
			return 1L;
		}
		final Map<Long, Long> map = DEEP_MAPPED.get(rep - 1);
		if (!map.containsKey(i)) {
			map.put(i, processDFS(Operation.applyAny(i), rep - 1));
			out("Added %d(%d/%d)[%d -> %d]", rep, map.size(), DEEP_MAPPED.stream().mapToInt(Map::size).sum(), i, map.get(i));
		}
		return map.get(i);
	}

}
