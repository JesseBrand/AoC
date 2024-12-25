package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.glue;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.Utils.repeatString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

public class D24 {

	public static void main(String[] args) throws IOException {
//		execute("2024/d24ex");
//		execute("2024/d24");
//		execute2("2024/d24ex2");
		execute2("2024/d24");
	}

	private static enum Operator {
		AND((a, b) -> a && b),
		OR((a, b) -> a || b),
		XOR((a, b) -> a ^ b);
		
		private BiPredicate<Boolean, Boolean> p;

		Operator(BiPredicate<Boolean, Boolean> p) {
			this.p = p;
			
		}

		final boolean eval(boolean a, boolean b) {
			return p.test(a, b);
		}
	}

	
	private static record Gate(String key1, String key2, Operator op, String keyOut) {
		@Override
		public String toString() {
			return String.format("%s %s %s -> %s", key1, key2, op, keyOut);
		}
	}

	private static record Input(Map<String, Boolean> values, List<Gate> gates) {
		public Map<String, Boolean> values() {
			return new HashMap<String, Boolean>(values);
		}

		public List<Gate> gates() {
			return new ArrayList<>(gates);
		}
	}
	
	private static void execute2(final String file) throws IOException {
		Map<String, String> switches = new HashMap<>();
		addSwitch(switches, "z14", "hbk");
		addSwitch(switches, "z18", "kvn");
		addSwitch(switches, "z23", "dbb");
		addSwitch(switches, "cvh", "tfn");
		final Input in = readInput(file, switches);
		for (int i = 0; ; i++) {
			final List<Gate> gates = in.gates();
			final String key = "z" + (i < 10 ? "0" : "") + i;
			final Gate gate = getGate(gates, key);
			if (gate == null) {
				break;
			}
			final String actual = flatToString(gates, gate);
			final String expected = expected(key, Operator.XOR, Operator.XOR);
			if (!expected.equals(actual)) {
				out("%s exp: %s", key, expected);
				out("%s act: %s", key, actual);
				out(key + " seems inconsistent");
//			} else {
//				out(key + " seems okay");
			}
//			outputTree(gates, gate);
		}
		out("2: %s", glue(",", switches.keySet().stream().sorted().toList()));
	}

	private static void addSwitch(final Map<String, String> switches, final String s1, final String s2) {
		switches.put(s1, s2);
		switches.put(s2, s1);
	}

	private static String expected(String key, Operator op1, Operator op2) {
		String iKey = key.substring(1);
		String form = String.format("(x%s %s y%s)", iKey, op1, iKey);
		if (iKey.equals("00")) {
			return form;
		}
		int parent = Integer.parseInt(iKey) - 1;
		String sParent = (parent > 9 ? "" : "0") + parent;
		if (parent > 0) {
			return String.format("((%s OR (x%s AND y%s)) %s %s)", expected("z" + sParent, Operator.XOR, Operator.AND), sParent, sParent, op2, form);
		}
		return String.format("(%s %s %s)", expected("z" + sParent, Operator.AND, Operator.AND), op2, form);
	}

	private static boolean seemsInconsistent(final List<Gate> gates, final Gate gate, final String key) {
		final String key1 = gate.key1();
		if (isInput(key1)) {
			if (Integer.parseInt(key1.substring(1)) > Integer.parseInt(key.substring(1))) {
//				out("here %s %s", key1.substring(1), key.substring(1));
				return true;
			}
		} else if (seemsInconsistent(gates, getGate(gates, key1), key)) {
			return true;
		}
		final String key2 = gate.key2();
		if (isInput(key2)) {
			if (Integer.parseInt(key2.substring(1)) > Integer.parseInt(key.substring(1))) {
				return true;
			}
		} else if (seemsInconsistent(gates, getGate(gates, key2), key)) {
			return true;
		}
		return false;
	}

	private static String flatToString(final List<Gate> gates, final Gate gate) {
		final List<String> terms = new ArrayList<>();
		final String key1 = gate.key1();
		final String key2 = gate.key2();
		if (isInput(key1)) {
			terms.add(key1);
		} else {
			terms.add(flatToString(gates, getGate(gates, key1)));
		}
		if (isInput(key2)) {
			terms.add(key2);
		} else {
			terms.add(flatToString(gates, getGate(gates, key2)));
		}
		Collections.sort(terms);
		return String.format("(%s %s %s)", terms.get(0), gate.op(), terms.get(1));
	}

	private static void outputTree(final List<Gate> gates, final Gate gate) {
		out("Tree for %s", gate.keyOut());
		int depth = getDepth(gates, gate);
		outputGate(gates, gate, depth);
		out();
	}

	private static int getDepth(List<Gate> gates, Gate gate) {
		final String key1 = gate.key1();
		final int depth1 = isInput(key1) ? 0 : getDepth(gates, getGate(gates, key1));
		final String key2 = gate.key2();
		final int depth2 = isInput(key2) ? 0 : getDepth(gates, getGate(gates, key2));
		return Math.max(depth1, depth2) + 1;
	}

	private static Gate getGate(final List<Gate> gates, final String key) {
		return gates.stream().filter(g -> g.keyOut().equals(key)).findFirst().orElseGet(() -> null);
	}

	private static void outputGate(final List<Gate> gates, final Gate gate, final int depth) {
		final String key1 = gate.key1();
		final String key2 = gate.key2();
		if (isInput(key1)) {
			out("%s%s", repeatString("  ", depth - 1), key1);
		} else {
			outputGate(gates, getGate(gates, key1), depth - 1);
		}
		out("%s%s %s", repeatString("  ", depth), gate.op(), gate.keyOut());
		if (isInput(key2)) {
			out("%s%s", repeatString("  ", depth - 1), key2);
		} else {
			outputGate(gates, getGate(gates, key2), depth - 1);
		}
	}

	private static boolean isInput(final String key) {
		return key.charAt(0) >= 'x' && key.charAt(0) <= 'z'
				&& key.charAt(1) >= '0' && key.charAt(1) <= '9'
				&& key.charAt(2) >= '0' && key.charAt(2) <= '9';
	}

	private static void execute(final String file) throws IOException {
		final Input in = readInput(file);
		final Map<String, Boolean> values = in.values();
		final List<Gate> gates = in.gates();
		out(values);
		out(gates);
		evaluateValues(values, gates);
		out(values);
		out("1: %d", parseNumber(values, "z")); // 58740594706150
	}

	private static Input readInput(String file) throws IOException {
		return readInput(file, Collections.emptyMap());
	}

	private static Input readInput(String file, final Map<String, String> switches) throws IOException {
		final List<String> lines = readFile(file);
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).isEmpty()) {
				final Map<String, Boolean> values = new HashMap<>();
				lines.subList(0, i).stream().map(s -> s.split(": ")).forEach(sa -> values.put(sa[0], Integer.parseInt(sa[1]) == 1));
				final List<Gate> gates = new ArrayList<>(lines.subList(i + 1, lines.size()).stream().map(s -> parseGate(s, switches)).toList());
				return new Input(values, gates);
			}
		}
		throw new IllegalStateException();
	}

	private static long parseNumber(final Map<String, Boolean> values, final String prefix) {
		long result = 0;
		for (int i = 0; ; i++) {
			final String key = prefix + (i < 10 ? "0" : "") + i;
			if (!values.containsKey(key)) {
				break;
			}
			if (values.get(key)) {
				result += Math.pow(2L, i);
			}
		}
		return result;
	}

	private static void evaluateValues(final Map<String, Boolean> values, final List<Gate> gates) {
		while (!gates.isEmpty()) {
			final Iterator<Gate> i = gates.iterator();
			while (i.hasNext()) {
				final Gate gate = i.next();
				if (values.containsKey(gate.key1()) && values.containsKey(gate.key2())) {
					values.put(gate.keyOut(), gate.op().eval(values.get(gate.key1()), values.get(gate.key2())));
					i.remove();
				}
			}
		}
	}

	private static Gate parseGate(final String s, final Map<String, String> switches) {
		final String[] split = s.split(" ");
		String out = split[4];
		if (switches.containsKey(out)) {
			out = switches.get(out);
		}
		return new Gate(split[0], split[2], Operator.valueOf(split[1]), out);
	}
}
