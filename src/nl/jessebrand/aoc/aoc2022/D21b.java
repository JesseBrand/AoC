package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D21b {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2022/d21b");
		final Map<String, String> monkeys = parseMonkeys(lines);
		out(monkeys);
		out("humn: %d", evaluate("root", monkeys));
	}

	private static long evaluate(final String name, Map<String, String> monkeys) {
		if (!monkeys.containsKey(name)) {
			throw new IllegalArgumentException("No monkey found with name " + name);
		}
		String value = monkeys.get(name);
		if (value.contains(" + ")) {
			String[] plus = value.split(" \\+ ");
			return evaluate(plus[0], monkeys) + evaluate(plus[1], monkeys);
		}
		if (value.contains(" - ")) {
			String[] minus = value.split(" - ");
			return evaluate(minus[0], monkeys) - evaluate(minus[1], monkeys);
		}
		if (value.contains(" * ")) {
			String[] multi = value.split(" \\* ");
			return evaluate(multi[0], monkeys) * evaluate(multi[1], monkeys);
		}
		if (value.contains(" / ")) {
			String[] div = value.split(" / ");
			return evaluate(div[0], monkeys) / evaluate(div[1], monkeys);
		}
		if (value.contains(" = ")) {
			String[] eq = value.split(" = ");
			long val0 = evaluateOptional(eq[0], monkeys);
			if (val0 != Long.MIN_VALUE) {
				out("%s = %d == %s", name, val0, eq[1]);
				return evaluateTo(val0, eq[1], monkeys);
			} else {
				long val1 = evaluateOptional(eq[1], monkeys);
				out("%s = %d == %s", name, val1, eq[0]);
				return evaluateTo(val1, eq[0], monkeys);
			}
		}
		return Integer.parseInt(value);
	}

	private static long evaluateOptional(String string, Map<String, String> monkeys) {
		try {
			return evaluate(string, monkeys);
		} catch(IllegalArgumentException e) {
			if (!e.getMessage().equals("No monkey found with name humn")) {
				throw e;
			}
//			System.err.println(e);
			return Long.MIN_VALUE;
		}
	}

	private static long evaluateTo(long targetValue, String name, Map<String, String> monkeys) {
		if (!monkeys.containsKey(name)) {
			if (name.equals("humn")) {
				return targetValue;
			}
			throw new IllegalArgumentException("No monkey found with name " + name);
		}
		String value = monkeys.get(name);
		out("%s: %s", name, value);
		try {
			return Integer.parseInt(value);
		} catch(NumberFormatException e) {}
		if (value.contains(" + ")) {
			String[] add = value.split(" \\+ ");
			long val0 = evaluateOptional(add[0], monkeys);
			if (val0 != Long.MIN_VALUE) {
				out("%s = %d - %d", add[1], targetValue, val0);
				return evaluateTo(targetValue - val0, add[1], monkeys);
			} else {
				long val1 = evaluate(add[1], monkeys);
				out("%s = %d - %d", add[0], targetValue, val1);
				return evaluateTo(targetValue - val1, add[0], monkeys);
			}
		}
		if (value.contains(" - ")) {
			String[] min = value.split(" - ");
			long val0 = evaluateOptional(min[0], monkeys);
			if (val0 != Long.MIN_VALUE) {
				out("%s = %d - %d", min[1], val0, targetValue);
				return evaluateTo(val0 - targetValue, min[1], monkeys);
			} else {
				long val1 = evaluate(min[1], monkeys);
				out("%s = %d + %d", min[0], targetValue, val1);
				return evaluateTo(targetValue + val1, min[0], monkeys);
			}
		}
		if (value.contains(" * ")) {
			String[] multi = value.split(" \\* ");
			long val0 = evaluateOptional(multi[0], monkeys);
			if (val0 != Long.MIN_VALUE) {
				out("%s = %d / %d", multi[1], targetValue, val0);
				return evaluateTo(targetValue / val0, multi[1], monkeys);
			} else {
				long val1 = evaluate(multi[1], monkeys);
				out("%s = %d / %d", multi[0], targetValue, val1);
				return evaluateTo(targetValue / val1, multi[0], monkeys);
			}
		}
		if (value.contains(" / ")) {
			String[] div = value.split(" / ");
			long val0 = evaluateOptional(div[0], monkeys);
			if (val0 != Long.MIN_VALUE) {
				out("%s = %d / %d", div[1], val0, targetValue);
				return evaluateTo(val0 / targetValue, div[1], monkeys);
			} else {
				long val1 = evaluate(div[1], monkeys);
				out("%s = %d * %d", div[0], targetValue, val1);
				return evaluateTo(targetValue * val1, div[0], monkeys);
			}
		}
		throw new IllegalStateException("Invalid operation: " + value);
	}

	private static Map<String, String> parseMonkeys(List<String> lines) {
		final Map<String, String> result = new HashMap<>();
		for (final String line : lines) {
			String[] split = line.split(": ");
			result.put(split[0], split[1]);
		}
		return result;
	}

}
