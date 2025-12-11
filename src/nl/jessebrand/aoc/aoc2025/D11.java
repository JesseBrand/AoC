package nl.jessebrand.aoc.aoc2025;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class D11 {


	public static void main(final String[] args) throws IOException {
		solve("2025/d11ex");
		solve("2025/d11exb");
		solve("2025/d11");
	}

	private static void solve(final String file) throws IOException {
		final List<String> lines = readFile(file);
		final List<Machine> machines = new ArrayList<>(lines.stream().map(D11::toMachine).toList());
		machines.add(new Machine("out", Collections.emptyList()));
		fillLeadsTo(machines);
		out(machines);
		
		if (findMachine(machines, "you") != null) {
			out("Part 1: %d", solveA(machines));
		}
		if (findMachine(machines, "svr") != null) {
			out("Part 2: %d", solveB(machines));
		}
		out();
	}

	private static void fillLeadsTo(final List<Machine> machines) {
		Set<Machine> cur = new HashSet<>();
		cur.add(findMachine(machines, "out"));
		while (!cur.isEmpty()) {
			final Set<Machine> next = new HashSet<>();
			for (final Machine m : cur) {
				List<Machine> to = findMachinesWithOut(machines, m.name());
				for (final Machine toM : to) {
					toM.leadsTo().add(m.name());
					toM.leadsTo().addAll(m.leadsTo());
					next.add(toM);
				}
			}
			cur = next;
		}
	}

	private static long solveA(final List<Machine> machines) {
		return findRoutesLong(machines, "you", "out");
	}

	private static long solveB(final List<Machine> machines) {
		final long sf = findRoutesLong(machines, "svr", "fft");
		final long fd = findRoutesLong(machines, "fft", "dac");
		final long dout = findRoutesLong(machines, "dac", "out");
		out("%d * %d * %d", sf, fd, dout);
		return sf * fd * dout;
	}


	private static long findRoutesLong(final List<Machine> machines, final String start, final String end) {
		return findAllLong(findMachine(machines, start), end, machines);
	}

	private static long findAllLong(final Machine start, final String target, final List<Machine> machines) {
		return start.outputs().stream().mapToLong(out -> {
			final String key = out + "-" + target;
			if (CACHE.containsKey(key)) {
				return CACHE.get(key);
			}
			if (out.equals(target)) {
				return mapResult(key, 1);
			}
			final Machine next = findMachine(machines, out);
			if (!next.leadsTo().contains(target)) {
				return mapResult(key, 0);
			}
			return mapResult(key, findAllLong(next, target, machines));
		}).sum();
	}

	private static final Map<String, Long> CACHE = new HashMap<>();

	private static long mapResult(final String key, final long result) {
		CACHE.put(key, result);
		return result;
	}

	public record Machine(String name, List<String> outputs, Set<String> leadsTo) {
		Machine(final String name, final List<String> outputs) {
			this(name, outputs, new HashSet<>());
		}
	}

	private static Machine toMachine(final String line) {
		final String[] split = line.split(":");
		return new Machine(split[0], Arrays.asList(split[1].trim().split(" ")));
	}

	private static Machine findMachine(final List<Machine> machines, final String name) {
		return machines.stream().filter(m -> m.name().equals(name)).findFirst().orElse(null);
	}

	private static List<Machine> findMachinesWithOut(final List<Machine> machines, final String out) {
		return machines.stream().filter(m -> m.outputs().contains(out)).toList();
	}
}
