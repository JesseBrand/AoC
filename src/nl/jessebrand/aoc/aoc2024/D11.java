package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.parseLongsFromString;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D11 {
	
	private static final List<Long> LIST_1 = Arrays.asList((long) 1);
	private static final Map<Long, List<Long>> MAPPED = new HashMap<>();
	private static final List<Map<Long, Long>> DEEP_MAPPED = new ArrayList<>();
	
	static {
		for (int i = 0; i < 76; i++) {
			DEEP_MAPPED.add(new HashMap<>());
		}
	}
	
	private static enum Operation {
		TO_1 {
			@Override
			public List<Long> apply(Long i) {
				return LIST_1;
			}
		},
		SPLIT {
			@Override
			public List<Long> apply(Long i) {
				String s = "" + i;
				return Arrays.asList(Long.parseLong(s.substring(0, s.length() / 2)), Long.parseLong(s.substring(s.length() / 2)));
			}
		},
		TIMES_2024 {
			@Override
			public List<Long> apply(Long i) {
				return Arrays.asList(i * 2024);
			}
		};
		
		public abstract List<Long> apply(Long i);
		
		public static List<Long> applyAny(Long i) {
			if (!MAPPED.containsKey(i)) {
//				System.out.println("Added " + i);
				MAPPED.put(i, getByLong(i).apply(i));
			}
			return MAPPED.get(i);
		}
		
		public static Operation getByLong(Long i) {
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
		final List<String> lines = readFile("2024/d11");
		List<Long> set = parseLongsFromString(lines.get(0));
		out("1: %s", processDFS(set, 25));
		out("2: %s", processDFS(set, 75));
	}

	private static int processBFS(List<Long> set, int times) {
		if (times == 0) {
			return set.size();
		}
		return set.stream().map(i -> Operation.getByLong(i).apply(i)).mapToInt(l -> processBFS(l, times - 1)).sum();
	}

	private static long processDFS(List<Long> set, int times) {
		return set.stream().mapToLong(i -> processDFS(i, times)).sum();
	}

	private static long processDFS(long i, int times) {
		final Map<Long, Long> map = DEEP_MAPPED.get(times);
		if (!map.containsKey(i)) {
			if (times == 0) {
				map.put(i, 1L);
			} else {
				map.put(i, processDFS(Operation.applyAny(i), times - 1));
			}
		}
		return map.get(i);
	}

}
