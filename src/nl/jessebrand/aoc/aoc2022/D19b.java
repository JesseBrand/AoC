package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;
import static nl.jessebrand.aoc.Utils.set;
import static nl.jessebrand.aoc.aoc2022.D19.RobotType.CLAY;
import static nl.jessebrand.aoc.aoc2022.D19.RobotType.GEODE;
import static nl.jessebrand.aoc.aoc2022.D19.RobotType.OBSIDIAN;
import static nl.jessebrand.aoc.aoc2022.D19.RobotType.ORE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import nl.jessebrand.aoc.aoc2022.D19.Blueprint;
import nl.jessebrand.aoc.aoc2022.D19.RobotType;
import nl.jessebrand.aoc.aoc2022.D19.State;

public class D19b {
	
	private static final int MAX_MINUTES = 32;

	public static void main(String[] args) throws IOException {
		List<String> lines = readFile("2022/d19");
		List<Blueprint> bps = parseBlueprints(lines).subList(0, 3);
		out(bps);
		simulate(bps, MAX_MINUTES);
	}
	
	private static void simulate(List<Blueprint> bps, int maxMinutes) {
		int result = 1;
		for (final Blueprint bp : bps) {
			int subresult = simulateBlueprint(bp, maxMinutes);
			out("Blueprint %d: %d geodes after %d minute", bp.index(), subresult, maxMinutes);
			result *= subresult;
		}
		out("Result 2: %d", result);
	}

	private static List<Blueprint> parseBlueprints(List<String> lines) {
		Pattern p = Pattern.compile("Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.");
		final List<Blueprint> result = new ArrayList<>();
		for (final String line : lines) {
			Matcher matcher = p.matcher(line);
			if (!matcher.matches()) {
				throw new IllegalStateException();
			}
			result.add(new Blueprint(
					Integer.parseInt(matcher.group(1)),
					Integer.parseInt(matcher.group(2)),
					Integer.parseInt(matcher.group(3)),
					Integer.parseInt(matcher.group(4)),
					Integer.parseInt(matcher.group(5)),
					Integer.parseInt(matcher.group(6)),
					Integer.parseInt(matcher.group(7))
			));
//			break;
		}
		return result;
	}

	private static int simulateBlueprint(Blueprint bp, int maxMinutes) {
		State startState = new State();
		List<State> toExamine = Arrays.asList(startState);
		final int maxOreCost = Stream.of(bp.oreOreCost(), bp.clayOreCost(), bp.obsidianOreCost(), bp.geodeOreCost()).reduce((i, j) -> Math.max(i, j)).get();
		int maxGeodes = 0;
		while (!toExamine.isEmpty()) {
			out("To examine at minute %d: %d", toExamine.get(0).minute(), toExamine.size());
			List<State> nextToExamine = new ArrayList<>();
			for (final State state : toExamine) {
				if (state.minute() == maxMinutes) {
					maxGeodes = Math.max(maxGeodes, state.geodes());
//					if (state.geodes() > 0 || state.geodeBots()  > 0) {
//						out("Finished Blueprint %d with %d geodes after moves %s", bp.index(), state.geodes(), state.actions());
//					}
				} else {
					int prevNextToExamineLength = nextToExamine.size();
					
//					out("Evaluating %s", state);
					
//					boolean foundBetter = false;
//					for (final State comp : toExamine) {
//						if (comp.isBetterThan(comp)) {
//							out("Found better option than:\n%s\n%s\nNot considering", state, comp);
//							foundBetter = true;
//							break;
//						}
//					}
//					if (foundBetter) {
//						break;
//					}
					if (state.minute() >= 28 && (state.geodeBots() * (maxMinutes - state.minute()) + state.geodes()) < 15 + bp.index() * 2) {
//					if (state.minute() >= 28 && (state.geodeBots() + state.geodes()) == 0) {
						continue;
					}
					
					Set<RobotType> ignoredAdded = new HashSet<>();
					if (state.oreBots() < maxOreCost && state.ore() >= bp.oreOreCost() && !state.ignored().contains(ORE)) {
//						out("Possible to build new ORE bot at cost %d ore", bp.oreOreCost());
						nextToExamine.add(state.newState(-bp.oreOreCost(), 0, 0, 0, 1, 0, 0, 0, Collections.emptySet(), ORE));
						ignoredAdded.add(ORE);
					}
					if (state.clayBots() < bp.obsidianClayCost() && state.ore() >= bp.clayOreCost() && !state.ignored().contains(CLAY)) {
//						out("Possible to build new CLAY bot at cost %d ore", bp.clayOreCost());
						nextToExamine.add(state.newState(-bp.clayOreCost(), 0, 0, 0, 0, 1, 0, 0, Collections.emptySet(), CLAY));
						ignoredAdded.add(CLAY);
					}
					if (state.obsidianBots() < bp.geodeObsidianCost() && state.ore() >= bp.obsidianOreCost() && state.clay() >= bp.obsidianClayCost() && !state.ignored().contains(OBSIDIAN)) {
//						out("Possible to build new OBSIDIAN bot at cost %d ore + %d clay", bp.obsidianOreCost(), bp.clayOreCost());
						nextToExamine.add(state.newState(-bp.obsidianOreCost(), -bp.obsidianClayCost(), 0, 0, 0, 0, 1, 0, Collections.emptySet(), OBSIDIAN));
						ignoredAdded.add(OBSIDIAN);
					}
					if (state.ore() >= bp.geodeOreCost() && state.obsidian() >= bp.geodeObsidianCost() && !state.ignored().contains(GEODE)) {
//						out("Possible to build new GEODE bot at cost %d ore + %d obsidian", bp.geodeOreCost(), bp.geodeObsidianCost());
						nextToExamine.add(state.newState(-bp.geodeOreCost(), 0, -bp.geodeObsidianCost(), 0, 0, 0, 0, 1, Collections.emptySet(), GEODE));
						ignoredAdded.add(GEODE);
					}
					if (!ignoredAdded.isEmpty() || nextToExamine.size() == prevNextToExamineLength) {
						Set<RobotType> ignored = set(state.ignored(), ignoredAdded);
						if (ignored.size() < (state.clayBots() == 0 ? 2 : state.obsidianBots() == 0 ? 3 : 4)) {
							nextToExamine.add(state.newState(0, 0, 0, 0, 0, 0, 0, 0, ignored, null));
						}
					}
//					if (state.ignored().size() < RobotType.values().length) {
//						nextToExamine.add(state.newState(0, 0, 0, 0, 0, 0, 0, 0, state.ignored(), null));
//					}
				}
			}
			toExamine = nextToExamine;
		}
		return maxGeodes;
	}
}
