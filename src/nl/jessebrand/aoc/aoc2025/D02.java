package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.parseLongsFromStrings;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.List;

public class D02 {

	public static void main(String[] args) throws IOException {
		solve("2025/d02ex");
		solve("2025/d02");
	}
	private static final void solve(final String file) throws IOException {
		final List<List<Long>> entries = parseLongsFromStrings(readFile(file).get(0), ",", "-");
		out(entries);
		long totalA = 0;
		long totalB = 0;
		for (final List<Long> entry : entries) {
//			out("%d-%d", entry.get(0), entry.get(1));
			for (long i = entry.get(0); i <= entry.get(1); i++) {
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

	private static boolean matches(final String s, final int pos, final String sub) {
		return s.substring(pos, pos + sub.length()).equals(sub);
	}

}

// 31000881061
// 46769308485
