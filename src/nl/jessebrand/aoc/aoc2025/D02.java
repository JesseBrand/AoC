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
		out("1: %d\n", totalA);
		out("2: %d\n", totalB);
	}

	private static boolean isValidA(String s) {
			int len = s.length() / 2;
			if (len == 0 || s.length() % len != 0 || s.length() % 2 != 0) {
				return true;
			}
			String sub = s.substring(0, len);
//			out("check: %s (%s)", s, sub);
			boolean good = false;
			for (int j = 0; j < s.length(); j += len) {
				if (!s.substring(j, j + len).equals(sub)) {
//					out("good: %s (%s, %s)", s, sub, s.substring(j, j + len));
					good = true;
					break;
				}
			}
//			good = false;
			if (!good) {
//				out("faulty : %s (%s)", s, sub);
				return false;
			}
//			out("false: %s try %s", s, sub);
		return true;
	}

	private static boolean isValidB(String s) {
		for (int i = 0; i < s.length() / 2; i++) {
			int len = i + 1;
			if (s.length() % len != 0) {
				continue;
			}
			String sub = s.substring(0, len);
//			out("check: %s (%s)", s, sub);
			boolean good = false;
			for (int j = 0; j < s.length(); j += len) {
				if (!s.substring(j, j + len).equals(sub)) {
//					out("good: %s (%s, %s)", s, sub, s.substring(j, j + len));
					good = true;
					break;
				}
			}
//			good = false;
			if (!good) {
//				out("faulty : %s (%s)", s, sub);
				return false;
			}
//			out("false: %s try %s", s, sub);
		}
		return true;
	}

}

// 31000885834 wrong
// 31000881061
// 46769308485