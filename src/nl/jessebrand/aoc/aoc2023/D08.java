package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D08 {
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d08");
		String instructions = lines.get(0);
		Map<String, Node> nodes = parseNodes(lines.subList(2, lines.size()));
		System.out.println(nodes);
		Node start = nodes.get("AAA");
		int result = findSteps(instructions, nodes, start);
		System.out.println(result);
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
				if (current.id().equals("ZZZ")) {
					return step;
				}
			}
		}
	}
	
	static Map<String, Node> parseNodes(List<String> lines) {
		final Map<String, Node> result = new HashMap<>();
		for (final String line : lines) {
			Node node = parseNode(line);
			result.put(node.id(), node);
		}
		return result;
	}

	private static Node parseNode(String line) {
		String id = line.substring(0, 3);
		String left = line.substring(7, 10);
		String right = line.substring(12, 15);
		return new Node(id, left, right);
	}

	static record Node(String id, String left, String right) {}

}
