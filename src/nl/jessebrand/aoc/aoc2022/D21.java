package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D21 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2022/d21");
		final Map<String, String> monkeys = parseMonkeys(lines);
		out(monkeys);
		out("root: %d", evaluate("root", monkeys));
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
		return Integer.parseInt(value);
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
