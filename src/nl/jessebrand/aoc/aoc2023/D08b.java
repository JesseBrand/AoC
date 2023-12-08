package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.aoc2023.D08.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.jessebrand.aoc.aoc2023.D08.Node;

public class D08b {
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d08");
		String instructions = lines.get(0);
		System.out.println(instructions.length() + ": " + instructions);
		Map<String, Node> nodes = parseNodes(lines.subList(2, lines.size()));
//		System.out.println(nodes);
		
		List<Node> current = new ArrayList<>();
		for (Node node : nodes.values()) {
			if (node.id().endsWith("A")) {
				current.add(node);
			}
		}
		System.out.println(current);
		
		List<Integer> allValues = new ArrayList<>();
		for (Node node : current) {
			allValues.add(findSteps(instructions, nodes, node));
		}
		System.out.println(allValues);
		
		long result = calcCommonMultiple(allValues);
		System.out.println(result);
		
//		int step = 0;
//		outer: while (true) {
//			for (char c : instructions.toCharArray()) {
//				List<Node> newList = new ArrayList<>();
//				if (c == 'L') {
//					for (Node node : current) {
//						newList.add(nodes.get(node.left()));
//					}
//				} else {
//					for (Node node : current) {
//						newList.add(nodes.get(node.right()));
//					}
//				}
//				step++;
//				current = newList;
//				int onZ = 0;
////				System.out.println(step + ": " + current);
//				for (int i = 0; i < current.size(); i++) {
//					if (current.get(i).id().endsWith("Z")) {
//						System.out.println(String.format("Step %d: %d at Z", step, i));
//					}
//				}
//				for (Node node : current) {
//					if (node.id().endsWith("Z")) {
//						onZ++;
//					}
//				}
////				if (onZ > 0) {
////					System.out.println(String.format("Step %d: %d/%d on Z", step, onZ, current.size()));
////				}
//				if (onZ == current.size()) {
//					break outer;
//				}
//			}
//		}
//		System.out.println(step);
	}

	private static long calcCommonMultiple(List<Integer> allValues) {
		long attempt = allValues.get(0);
		List<Integer> subList = allValues.subList(1, allValues.size());
		while (true) {
//			System.out.println(attempt);
			boolean found = true;
			for (int val : subList) {
				if (attempt % val != 0) {
					found = false;
					break;
				}
			}
			if (found) {
				return attempt;
			}
			attempt += allValues.get(0);
		}
	}

	static int findSteps(String instructions, Map<String, Node> nodes, Node startNode) {
		Node current = startNode;
		int step = 0;
		while (true) {
			for (char c : instructions.toCharArray()) {
				if (c == 'L') {
					current = nodes.get(current.left());
				} else {
					current = nodes.get(current.right());
				}
				step++;
				if (current.id().endsWith("Z")) {
					return step;
				}
			}
		}
	}

	// 0: 24253 
	// 5: 16271
	
	
}
