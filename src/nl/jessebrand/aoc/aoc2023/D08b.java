package nl.jessebrand.aoc.aoc2023;

import static nl.jessebrand.aoc.Utils.calcCommonMultiple;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.aoc2023.D08.findSteps;
import static nl.jessebrand.aoc.aoc2023.D08.parseNodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.jessebrand.aoc.aoc2023.D08.Node;

public class D08b {
	
	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2023/d08");
		final String instructions = lines.get(0);
		System.out.println(instructions.length() + ": " + instructions);
		final Map<String, Node> nodes = parseNodes(lines.subList(2, lines.size()));
		
		final List<Node> startNodes = new ArrayList<>();
		final List<Integer> allValues = new ArrayList<>();
		for (final Node node : nodes.values()) {
			if (node.id().endsWith("A")) {
				startNodes.add(node);
				allValues.add(findSteps(instructions, nodes, node, "Z"));
			}
		}
		System.out.println(startNodes);
		System.out.println(allValues);
		
		final long result = calcCommonMultiple(allValues);
		System.out.println(result);
	}
}
