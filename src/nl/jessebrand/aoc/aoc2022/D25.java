package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.*;

import java.io.IOException;
import java.util.List;

public class D25 {

	public static void main(String[] args) throws IOException {
		runTests();
		
		final List<String> lines = readFile("2022/d25");
		long total = 0;
		for (String snafuLine : lines) {
			total += toDecimal(snafuLine);
//			out("Total now %d", total);
		}
		out("Total = %d = %s = %d", total, toSnafu(total), toDecimal(toSnafu(total)));
//		out(toDecimal("=-122=-==0-1=2212=100"));
	}
	
	static void runTests() {
		assertEquals(1, pow(5, 0));
		assertEquals(5, pow(5, 1));
		assertEquals(25, pow(5, 2));
		assertEquals(125, pow(5, 3));
		assertEquals(625, pow(5, 4));
		
		assertEquals(1, toDecimal("1"));
		assertEquals(2, toDecimal("2"));
		assertEquals(3, toDecimal("1="));
		assertEquals(4, toDecimal("1-"));
		assertEquals(5, toDecimal("10"));
		assertEquals(6, toDecimal("11"));
		assertEquals(7, toDecimal("12"));
		assertEquals(8, toDecimal("2="));
		assertEquals(9, toDecimal("2-"));
		assertEquals(10, toDecimal("20"));
		assertEquals(15, toDecimal("1=0"));
		assertEquals(20, toDecimal("1-0"));
		assertEquals(2022, toDecimal("1=11-2"));
		assertEquals(12345, toDecimal("1-0---0"));
		assertEquals(314159265, toDecimal("1121-1110-1=0"));
		assertEquals(1747, toDecimal("1=-0-2"));
		assertEquals(906, toDecimal("12111"));
		assertEquals(198, toDecimal("2=0="));
		assertEquals(11, toDecimal("21"));
		assertEquals(201, toDecimal("2=01"));
		assertEquals(31, toDecimal("111"));
		assertEquals(1257, toDecimal("20012"));
		assertEquals(32, toDecimal("112"));
		assertEquals(353, toDecimal("1=-1="));
		assertEquals(107, toDecimal("1-12"));
		assertEquals(7, toDecimal("12"));
		assertEquals(3, toDecimal("1="));
		assertEquals(37, toDecimal("122"));

		assertEquals("1", toSnafu(1));
		assertEquals("2", toSnafu(2));
		assertEquals("1=", toSnafu(3));
		assertEquals("1-", toSnafu(4));
		assertEquals("10", toSnafu(5));
		assertEquals("11", toSnafu(6));
		assertEquals("12", toSnafu(7));
		assertEquals("2=", toSnafu(8));
		assertEquals("2-", toSnafu(9));
		assertEquals("20", toSnafu(10));
		assertEquals("1=0", toSnafu(15));
		assertEquals("1-0", toSnafu(20));
		assertEquals("1=11-2", toSnafu(2022));
		assertEquals("1-0---0", toSnafu(12345));
		assertEquals("1121-1110-1=0", toSnafu(314159265));
		assertEquals("1=-0-2", toSnafu(1747));
		assertEquals("12111", toSnafu(906));
		assertEquals("2=0=", toSnafu(198));
		assertEquals("21", toSnafu(11));
		assertEquals("2=01", toSnafu(201));
		assertEquals("111", toSnafu(31));
		assertEquals("20012", toSnafu(1257));
		assertEquals("112", toSnafu(32));
		assertEquals("1=-1=", toSnafu(353));
		assertEquals("1-12", toSnafu(107));
		assertEquals("12", toSnafu(7));
		assertEquals("1=", toSnafu(3));
		assertEquals("122", toSnafu(37));
	}
	
	static long toDecimal(String snafu) {
		long result = 0;
		for (int i = 0;i < snafu.length();i++) {
			char c = snafu.charAt(snafu.length() - 1 - i);
			long placeValue = pow(5, i);
			long toAdd = switch (c) {
				case '2' -> placeValue * 2;
				case '1' -> placeValue;
				case '0' -> 0;
				case '-' -> -placeValue;
				case '=' -> -placeValue * 2;
				default -> throw new IllegalStateException();
			};
			result += toAdd;
		}
//		String hexal = Integer.toUnsignedString(result, 5);
//		out("%d -> %s", result, hexal);
		return result;
	}
	
	private static long pow(long a, int b) {
		long result = 1L;
		if (b > 0) {
			for (int i = 0; i < b; i++) {
				result *= a;
			}
		}
		if (b < 1) {
			for (int i = -1; i > -b; i--) {
				result /= a;
			}
		}
		return result;
	}

	static String toSnafu(long decimal) {
		long decValue = decimal;
		final String hexal = Long.toUnsignedString(decimal, 5);
		String result = "";
		for (int i = 0; i < hexal.length() + 1; i++) {
			long placeValue = pow(5, i);
			long inc = 2 * placeValue;
			decValue += inc;
			final String hexVal = Long.toUnsignedString(decValue, 5);
//			out("%s, %d, %d", hexVal, hexVal.length(), hexVal.length() - 1 - i);
//			if (hexVal.length() - 1 - i < 0) {
//				break;
//			}
			char c = hexVal.length() - 1 - i < 0 ? '0' : hexVal.charAt(hexVal.length() - 1 - i);
			char toAdd = switch (c) {
				case '0' -> '=';
				case '1' -> '-';
				case '2' -> '0';
				case '3' -> '1';
				case '4' -> '2';
				default -> throw new IllegalStateException();
			};
			result = toAdd + result;
			out("%d / %s: Place %d (%d, added %d); %s: val %c -> %c -> %c; %s", decimal, hexal, i, placeValue, inc, hexVal, i >= hexal.length() ? 'X' : hexal.charAt(hexal.length() - 1 - i), c, toAdd, result);
		}
		while (result.charAt(0) == '0') {
			result = result.substring(1);
		}
//		out("%d -> %s -> %s", decimal, hexal, result);
		return result;
	}
}

// 7121457963
// 1017942340

//        1-1-110=2=--== wrong
//      =01-1-110=2=--== wrong
// =-122=-==0-1=2212=100 wrong
//  2-0-01==0-1=2212=100
