package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.jessebrand.aoc.Utils;

public class D16 {
	
	private static final int MAX_MINUTES = 30;

	public static void main(String[] args) throws IOException {
		List<String> lines = readFile("2022/d16");
		List<Valve> valves = parseValves(lines);
		System.out.println(valves.size() + " valves: " + valves);
		List<Connection> connections = parseConnections(lines);
		System.out.println(connections.size() + " connections: " + connections);
		Map<String, Map<String, Integer>> paths = calculatePaths(valves, connections);
		debugPaths(paths);
		Move optimalPath = findOptimalPath(valves, paths);
		System.out.println(optimalPath);
		int result1 = optimalPath.totalFlowRate();
		System.out.println("Result 1: " + result1);
	}
	
	private static void debugPaths(Map<String, Map<String, Integer>> paths) {
		System.out.println("Paths connecting all valves");
		int count = 0;
		for (final String from : paths.keySet()) {
			count += paths.get(from).size();
			for (final String to : paths.get(from).keySet()) {
				System.out.println(String.format("%s to %s: %d", from, to, paths.get(from).get(to)));
			}
		}
		System.out.println(count + " paths");
	}

	private static List<Valve> parseValves(final List<String> lines) {
		final List<Valve> result = new ArrayList<>(lines.size());
		for (final String line : lines) {
			String[] split = line.split(" has flow rate=");
			String id = split[0].substring(split[0].length() - 2);
			int flowrate = Integer.parseInt(split[1].split(";")[0]);
			if (flowrate > 0) {
				result.add(new Valve(id, flowrate));
			}
		}
		return Collections.unmodifiableList(result);
	}
	
	
	private static List<Connection> parseConnections(final List<String> lines) {
		final List<Connection> result = new ArrayList<>();
		for (final String line : lines) {
			String[] split = line.split(" has flow rate=");
			String id = split[0].substring(split[0].length() - 2);
			String targets = split[1].split("tunnels? leads? to valves? ")[1];
			for (final String target : targets.split(", ")) {
				result.add(new Connection(id, target));
			}
		}
		return Collections.unmodifiableList(result);
	}


	private static Move findOptimalPath(List<Valve> valves, Map<String, Map<String, Integer>> paths) {
		List<Move> possibleSolutions = new ArrayList<>();
		
		List<Move> nextAttempts = new ArrayList<>();
		Move startMove = new Move("AA", 0, 0, new ArrayList<>(valves), Collections.emptyList(), null);
		nextAttempts.add(startMove);

		while (!nextAttempts.isEmpty()) {
			
			int lowestTime = Integer.MAX_VALUE;
			int highestTime = 0;
			
			for (final Move solution : nextAttempts) {
				if (solution.elapsedMinutes() < lowestTime) {
					lowestTime = solution.elapsedMinutes();
				}
				if (solution.elapsedMinutes() > highestTime) {
					highestTime = solution.elapsedMinutes();
				}
			}

			System.out.println("Evaluating " + nextAttempts.size() + " possibilities (" + lowestTime + " to " + highestTime + " min)");
			List<Move> nextNextAttempts = new ArrayList<>();
			for (final Move nextAttempt : nextAttempts) {
				possibleSolutions.add(nextAttempt);
				for (final Valve target : nextAttempt.closedValves()) {
					int dist = getDist(paths, nextAttempt.location(), target.id());
					int newElapsed = nextAttempt.elapsedMinutes() + dist + 1;
					if (newElapsed <= MAX_MINUTES) {
						int targetFlow = (MAX_MINUTES - newElapsed) * target.flowrate();
						List<Valve> openedValves = new ArrayList<>(nextAttempt.openedValves());
						openedValves.add(target);
						List<Valve> closedValves = new ArrayList<>(nextAttempt.closedValves());
						closedValves.remove(target);
						nextNextAttempts.add(new Move(target.id(), newElapsed, nextAttempt.totalFlowRate() + targetFlow, Collections.unmodifiableList(closedValves), Collections.unmodifiableList(openedValves), nextAttempt));
					}
				}
			}
			nextAttempts = nextNextAttempts;
		}
		
		System.out.println("Examined a total of " + possibleSolutions.size() + " possible solutions");
		
		int highestFlow = 0;
		Move highestMove = null;
		
		for (final Move solution : possibleSolutions) {
			if (solution.totalFlowRate() > highestFlow) {
				highestFlow = solution.totalFlowRate();
				highestMove = solution;
			}
		}
		
		return highestMove;
	}

	private static int getDist(Map<String, Map<String, Integer>> paths, final String from, final String to) {
		return paths.get(from).get(to);
	}

	private static Map<String, Map<String, Integer>> calculatePaths(List<Valve> valves, List<Connection> connections) {
		final List<String> ids = new ArrayList<>(valves.stream().map(valve -> valve.id()).toList());
		ids.add("AA");
		
		Map<String, Map<String, Integer>> result = new HashMap<>();
		for (final String originId : ids) {
			Map<String, Integer> targets = new HashMap<>();
			for (final Valve target : valves) {
				if (originId.equals(target.id())) {
					continue;
				}
				targets.put(target.id(), calculateDistance(connections, originId, target.id()));
			}
			result.put(originId, targets);
		}
		return result;
	}

	private static Integer calculateDistance(List<Connection> connections, String originId, String targetId) {
		List<String> tried = new ArrayList<>();
		List<String> nextAttempts = new ArrayList<>(); // contains from attempts
		nextAttempts.add(originId);
		int length = 1;
		while (!nextAttempts.isEmpty()) {
			List<String> nextNextAttempts = new ArrayList<>();
			for (String nextAttempt : nextAttempts) {
				for (Connection conn : connections) {
					if (!conn.idFrom().equals(nextAttempt)) {
						continue;
					}
					if (conn.idTo().equals(targetId)) {
						return length;
					}
					if (!tried.contains(conn.idTo())) {
						nextNextAttempts.add(conn.idTo());
					}
				}
			}
			tried.addAll(nextAttempts);
			nextAttempts = nextNextAttempts;
			length++;
		}
		throw new IllegalStateException("No path found from " + originId + " to " + targetId + " (tried " + Utils.glue(", ", tried) + ")");
	}

	static record Valve(String id, int flowrate) {}
	
	static record Connection(String idFrom, String idTo) {}
	
	static record Move(String location, int elapsedMinutes, int totalFlowRate, List<Valve> closedValves, List<Valve> openedValves, Move parent) {}
}

// 1724