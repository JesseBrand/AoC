package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D08 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d08");
		final String instructions = lines.get(0);
		System.out.println(instructions.length() + ": " + instructions);
		final Map<String, Node> nodes = parseNodes(lines.subList(2, lines.size()));

		final Node start = nodes.get("AAA");
		final int result = findSteps(instructions, nodes, start, "ZZZ");
		System.out.println(result);
	}

	static int findSteps(final String instructions, final Map<String, Node> nodes, final Node startNode, final String endsWith) {
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
				if (current.id().endsWith(endsWith)) {
					return step;
				}
			}
		}
	}

	static Map<String, Node> parseNodes(final List<String> lines) {
		final Map<String, Node> result = new HashMap<>();
		for (final String line : lines) {
			final Node node = parseNode(line);
			result.put(node.id(), node);
		}
		return result;
	}

	private static Node parseNode(final String line) {
		final String id = line.substring(0, 3);
		final String left = line.substring(7, 10);
		final String right = line.substring(12, 15);
		return new Node(id, left, right);
	}

	static record Node(String id, String left, String right) {}

}
