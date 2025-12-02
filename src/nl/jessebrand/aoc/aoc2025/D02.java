package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class D02 {

	public static void main(String[] args) throws IOException {
		solve("2025/d02ex");
		solve("2025/d02");
	}
	private static final void solve(final String file) throws IOException {
		final List<String> entries = Arrays.asList(readFile(file).get(0).split(","));
		out(entries);
		long totalA = 0;
		long totalB = 0;
		for (final String entry : entries) {
			String[] split = entry.split("-");
			long min = Long.parseLong(split[0]);
			long max = Long.parseLong(split[1]);
//			out("%d-%d", min, max);
			for (long i = min; i <= max; i++) {
				if (!isValidA("" + i)) {
					totalA += i;
				}
				if (!isValidB("" + i)) {
					totalB += i;
				}
			}
		}
		out("1: %d", totalA);
		out("2: %d", totalB);
	}

	private static boolean isValidA(String s) {
		final int len = s.length() / 2;
		if (len == 0 || s.length() % len != 0 || s.length() % 2 != 0) {
			return true;
		}
		final String sub = s.substring(0, len);
		if (matches(s, 0, sub) && matches(s, len, sub)) {
			return false;
		}
		return true;
	}

	private static boolean matches(final String s, final int pos, final String sub) {
		return s.substring(pos, pos + sub.length()).equals(sub);
	}

	private static boolean isValidB(String s) {
		for (int len = 1; len < s.length() / 2 + 1; len++) {
			if (s.length() % len != 0) {
				continue;
			}
			final String sub = s.substring(0, len);
//			out("check: %s (%s)", s, sub);
			boolean good = false;
			for (int j = 0; j < s.length(); j += len) {
				if (!matches(s, j, sub)) {
					good = true;
					break;
				}
			}
			if (!good) {
//				out("faulty : %s (%s)", s, sub);
				return false;
			}
		}
		return true;
	}

}

// 31000885834 wrong
// 31000881061
// 46769308485