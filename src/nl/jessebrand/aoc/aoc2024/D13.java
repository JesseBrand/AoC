package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class D13 {
	
	private static final long part2Inc = 10000000000000L;
	
	private static record Machine(int ax, int ay, int bx, int by, long prizeX, long prizeY) {
		
		Machine(String ax, String ay, String bx, String by, String prizeX, String prizeY) {
			this(Integer.parseInt(ax), Integer.parseInt(ay),
					Integer.parseInt(bx), Integer.parseInt(by),
					Integer.parseInt(prizeX), Integer.parseInt(prizeY));
		}
	}
	
	private static record Result(long a, long b) {}

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d13");
		final List<Machine> machines = new ArrayList<>();
		for (int i = 0; i < lines.size(); i += 4) {
			machines.add(parseMachine(lines.get(i), lines.get(i + 1), lines.get(i + 2)));
		}
		long total1 = 0;
		long total2 = 0;
		for (final Machine machine : machines) {
			final Result result1 = calculateResult(machine);
			if (result1 != null) {
				total1 += result1.a() * 3 + result1.b();
			}
//			out("Find: %s", findResult(machine));
//			out("Calc: %s", mResult);

			final Machine machine2 = new Machine(machine.ax(), machine.ay(), machine.bx(), machine.by(), machine.prizeX() + part2Inc, machine.prizeY() + part2Inc);
			final Result result2 = calculateResult(machine2);
			out(result2);
			if (result2 != null) {
				total2 += result2.a() * 3 + result2.b();
			}
		}
		out("1: %d", total1);
		out("2: %d", total2);
	}
	
	private static long score(final Result result) {
		return result.a() * 3 + result.b();
	}

	private static Result calculateResult(Machine m) {
		double da = (double) m.ay() / m.ax();
		double db = (double) m.by() / m.bx();
		double dPlus = m.prizeY - (m.prizeX * db);
		double tX = dPlus / (da - db);
		out("a(x) = %fx;   b(x) = %fx %s %.3f;  intersect at x = %.3f;   %s", da, db, dPlus > 0 ? '+' : '-', Math.abs(dPlus), tX, m);
		final long a = Math.round(tX) / m.ax();
		final long b = (m.prizeX() - Math.round(tX)) / m.bx();
		if (a * m.ax() + b * m.bx() == m.prizeX() && a * m.ay() + b * m.by() == m.prizeY()) {
			out("%d * %d + %d * %d = %d vs %d;   %d * %d + %d * %d = %d vs %d", a, m.ax(), b, m.bx(), a * m.ax() + b * m.bx(), m.prizeX(), a, m.ay(), b, m.by(), a * m.ay() + b * m.by(), m.prizeY());
			return new Result(a, b);
		}
		return null;
	}

	private static Result findResult(Machine m) {
		long b = m.prizeX() / m.bx();
		long a = 0;
		do {
//			out("%d, %d", a, b);
			if ((b * m.bx() + a * m.ax()) == m.prizeX() && (b * m.by() + a * m.ay()) == m.prizeY()) {
				return new Result(a, b);
			}
			b--;
			a = (m.prizeX() - (m.bx() * b)) / m.ax();
		}
		while (b > 0);
		return null;
	}

	private static Machine parseMachine(String line1, String line2, String line3) {
		String[] split1 = line1.split(" ");
		String[] split2 = line2.split(" ");
		String[] split3 = line3.split(" ");
		return new Machine(
				split1[2].substring(2, split1[2].length() - 1),
				split1[3].substring(2),
				split2[2].substring(2, split2[2].length() - 1),
				split2[3].substring(2),
				split3[1].substring(2, split3[1].length() - 1),
				split3[2].substring(2)
				);
	}
}

// EX: 875318608908

// 35255
// 51740098162403 too low
// 89969890228936 too high
// 87582154060429