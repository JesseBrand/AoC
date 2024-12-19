package nl.jessebrand.aoc.aoc2024;

import static nl.jessebrand.aoc.Utils.*;
import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.parseIntsFromString;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class D17 {
	
	static int[] program;
	static long regA, regB, regC; // registers
	static int instructionPointer;
	static List<Integer> output;
	
	static enum ComboOperand {
		_0(0),
		_1(1),
		_2(2),
		_3(3),
		_4 {
			@Override
			long getValue() {
				return regA;
			}
		},
		_5 {
			@Override
			long getValue() {
				return regB;
			}
		},
		_6 {
			@Override
			long getValue() {
				return regC;
			}
		},
		_7 {
			@Override
			long getValue() {
				throw new IllegalStateException("Combo Operand 7 should never occur");
			}
		};
		
		private long value;

		ComboOperand() {
			this(-1);
		}
		ComboOperand(int value) {
			this.value = value;
		}
		
		long getValue() {
			if (value == -1) {
				throw new IllegalStateException();
			}
			return value;
		}
	}
	
	static enum Instruction {
		ADV { // 0
			@Override
			void execute() {
				regA = (int) (regA / Math.pow(2, readComboOperand()));
			}
		},
		BXL { // 1
			@Override
			void execute() {
				regB = regB ^ readLiteralOperand();
			}
		},
		BST { // 2
			@Override
			void execute() {
				regB = readComboOperand() % 8;
			}
		},
		JNZ { // 3
			@Override
			void execute() {
				if (regA == 0) { 
					return;
				}
				instructionPointer = readLiteralOperand() - 2;
			}
		},
		BXC { // 4
			@Override
			void execute() {
//				readLiteralOperand();
				regB = regB ^ regC;
			}
		},
		OUT { // 5
			@Override
			void execute() {
				output.add((int) (readComboOperand() % 8));
			}
		},
		BDV { // 6
			@Override
			void execute() {
				regB = (int) (regA / Math.pow(2, readComboOperand()));
			}
		},
		CDV { // 7
			@Override
			void execute() {
				regC = (int) (regA / Math.pow(2, readComboOperand()));
			}
		};
		
		abstract void execute();
		
	}

	static long readComboOperand() {
		int operand = readLiteralOperand();
//		if (operand == -1) {
//			return -1;
//		}
		return ComboOperand.values()[operand].getValue(); 
	}

	static int readLiteralOperand() {
//		if (instructionPointer + 1 > program.length) {
//			halted = true;
//			return -1;
//		}
		return program[instructionPointer + 1];
	}

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2024/d17");
		
		final long origA = Integer.parseInt(lines.get(0).split(" ")[2]);
		final long origB = Integer.parseInt(lines.get(1).split(" ")[2]);
		final long origC = Integer.parseInt(lines.get(2).split(" ")[2]);
		
		final List<Integer> expected = parseIntsFromString(lines.get(4).split(" ")[1], ",");
		program = expected.stream().mapToInt(i->i).toArray();
		
		executeProgram(origA, origB, origC);
		
		out("1: %s\n", glue(",", output));

		out("Expected: %s", expected);
//		for (long i = 0; ; i++) {
//			boolean result = executeProgram(i, origB, origC, expected);
//			if (i % 10000000 == 0) {
//				out("[%d]: %s", i, glue(",", output));
//			}
//			if (result) {
//				out("Found match at i = %d", i);
//				out("Expected: %s", expected);
//				break;
//			}
////			if (output.size() < expected.length() / 2) {
////				i *= 2;
////			}
//		}
		
//		final List<Integer> solution = new ArrayList<>();
//		solution.add(0);
		int expA = 3;
		long total = 0;
		for (int i = 0; i < expected.size(); i++) {
			int solI = expected.size() - i - 1;
			expA = findSolutionWithRemainder(expA, expected.get(solI));
			out(expA);
			expA %= 8;
			total = total * 8 + expA;
			executeProgram(total, origB, origC);
			out(output);
		}
		out(total);
		executeProgram(total, origB, origC);
		out(output);
	}

	private static int findSolutionWithRemainder(int expA, int target) {
		for (int i = 0; ; i++) {
			executeProgram(i, 0, 0, false);
			out("%d: output=%d remainder=%d", i, output.get(0), regA);
			if (regA == expA && output.get(0) == target) {
//				out("%d: output=%d remainder=%d", i, output.get(0), regA);
				return i;
			}
			if (regA > expA) {
				break;
			}
		}
		throw new IllegalStateException("No solution with output=" + target + " and remainder=" + expA);
	}
	
	
	private static int pow(int a, int b) {
		return (int) Math.pow(a, b);
	}

	private static void executeProgram(final long a, final long b, final long c) {
		executeProgram(a, b, c, true);
	}

//	private static void executeProgram(final long a, final long b, final long c, final boolean allowJumps) {
//		regA = a;
//		regB = b;
//		regC = c;
//		instructionPointer = 0;
//		output = new ArrayList<>();
//
//		while (instructionPointer < program.length) {
//			final Instruction in = Instruction.values()[program[instructionPointer]];
////			out("Executing [%d]: %d=%s", instructionPointer, program[instructionPointer], in);
//			if (in == Instruction.JNZ) {
//				return;
//			}
//			in.execute();
//			instructionPointer += 2;
//		}
//	}
	
	private static void executeProgram(final long a, final long b, final long c, final boolean allowJumps) {
		regA = a;
		regB = b;
		regC = c;
		instructionPointer = 0;
		output = new ArrayList<>();
		
		do {
			regB = regA % 8;
			regB = regB ^ 3;
			regC = (int) (regA / Math.pow(2, regB));
			regB = regB ^ regC;
			regB = regB ^ 3;
			regA = regA / 8;
			output.add((int) (regB % 8));
		} while (regA > 0 && allowJumps);
	}
//	
//	private static boolean executeProgram(final long a, final long b, final long c, final List<Integer> expected) {
//		regA = a;
//		regB = b;
//		regC = c;
//		instructionPointer = 0;
//		output = new ArrayList<>();
//		
//		while (instructionPointer < program.length) {
//			final Instruction in = Instruction.values()[program[instructionPointer]];
////			out("Executing [%d]: %d=%s", instructionPointer, program[instructionPointer], in);
//			in.execute();
//			if (in == Instruction.OUT) {
//				int last = output.size() - 1;
//				if (output.get(last) != expected.get(last)) {
//					return false;
//				}
//			}
//			instructionPointer += 2;
//		}
//		
//		return output.equals(expected);
//	}
}
