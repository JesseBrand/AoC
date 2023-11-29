package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.jessebrand.aoc.Utils;
import nl.jessebrand.aoc.aoc2022.D16.Connection;
import nl.jessebrand.aoc.aoc2022.D16.Valve;

public class D16b {
	
	private static final int MAX_MINUTES = 26;

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

		int longestPath = 0;
		for (Map<String, Integer> path : paths.values()) {
			for (int value : path.values()) {
				longestPath = Math.max(longestPath, value);
			}
		}
		System.out.println("Longest path between 2 valves: " + longestPath);
		
		List<Move> nextAttempts = new ArrayList<>();
		Move startMove = new Move("AA", "AA", 0, 0, 0, new ArrayList<>(valves), Collections.emptyList(), null);
		nextAttempts.add(startMove);
		
		while (!nextAttempts.isEmpty()) {
			
			int lowestTime = Integer.MAX_VALUE;
			int highestTime = 0;
			
			for (final Move solution : nextAttempts) {
				if (solution.elapsedMinutes1() < lowestTime) {
					lowestTime = solution.elapsedMinutes1();
				}
				if (solution.elapsedMinutes2() < lowestTime) {
					lowestTime = solution.elapsedMinutes2();
				}
				if (solution.elapsedMinutes1() > highestTime) {
					highestTime = solution.elapsedMinutes1();
				}
				if (solution.elapsedMinutes2() > highestTime) {
					highestTime = solution.elapsedMinutes2();
				}
			}
			
			System.out.println("Evaluating " + nextAttempts.size() + " possibilities (" + lowestTime + " to " + highestTime + " min)");
			List<Move> nextNextAttempts = new ArrayList<>();
			for (final Move nextAttempt : nextAttempts) {
				possibleSolutions.add(nextAttempt);
				for (final Valve target : nextAttempt.closedValves()) {
					List<Valve> openedValves = new ArrayList<>(nextAttempt.openedValves());
					openedValves.add(target);
					List<Valve> closedValves = new ArrayList<>(nextAttempt.closedValves());
					closedValves.remove(target);

					if (nextAttempt.elapsedMinutes1() <= nextAttempt.elapsedMinutes2()) {
						// calc for 1
						int dist1 = getDist(paths, nextAttempt.location1(), target.id());
						int newElapsed1 = nextAttempt.elapsedMinutes1() + dist1 + 1;
						if (newElapsed1 <= MAX_MINUTES) {
							int targetFlow = (MAX_MINUTES - newElapsed1) * target.flowrate();
							nextNextAttempts.add(new Move(
									target.id(),
									nextAttempt.location2(),
									newElapsed1,
									nextAttempt.elapsedMinutes2(),
									nextAttempt.totalFlowRate() + targetFlow,
									Collections.unmodifiableList(closedValves),
									Collections.unmodifiableList(openedValves), nextAttempt));
						}
					}

					if (nextAttempt.elapsedMinutes2() <= nextAttempt.elapsedMinutes1()
							&& nextAttempt.elapsedMinutes1() != 0) {
						// calc for 2
						int dist2 = getDist(paths, nextAttempt.location2(), target.id());
						int newElapsed2 = nextAttempt.elapsedMinutes2() + dist2 + 1;
						if (newElapsed2 <= MAX_MINUTES) {
							int targetFlow = (MAX_MINUTES - newElapsed2) * target.flowrate();
							nextNextAttempts.add(new Move(
									nextAttempt.location1(),
									target.id(),
									nextAttempt.elapsedMinutes1(),
									newElapsed2,
									nextAttempt.totalFlowRate() + targetFlow,
									Collections.unmodifiableList(closedValves),
									Collections.unmodifiableList(openedValves), nextAttempt));
						}
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
	
	static record Move(String location1, String location2, int elapsedMinutes1, int elapsedMinutes2, int totalFlowRate, List<Valve> closedValves, List<Valve> openedValves, Move parent) {}
}

// 2013 too low