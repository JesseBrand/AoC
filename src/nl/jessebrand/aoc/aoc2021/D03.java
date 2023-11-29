package nl.jessebrand.aoc.aoc2021;

import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class D03 {

	public static void main(String[] args) throws IOException {
		final List<String> lines = readFile("2021/d3");
		System.out.println(lines);
		int lineLength = lines.get(0).length();
		char mcvs[] = new char[lineLength];
		for (int i = 0; i < lineLength; i++) {
			mcvs[i] = getMostCommonValue(lines, i);
		}
		String gamma = "";
		String epsilon = "";
		for (int i = 0; i < lineLength; i++) {
			gamma += mcvs[i];
			epsilon += not(mcvs[i]);
		}
		int gI = Integer.parseInt(gamma, 2);
		int eI = Integer.parseInt(epsilon, 2);
		System.out.println(String.format("gamma= %s (%d), epsilon= %s (%d), power cons=  %d", gamma, gI, epsilon, eI, gI*eI));
		// 1458194
		
		List<String> ox = new ArrayList<>(lines);
		int i = 0;
		while (ox.size() > 1) {
			char mcv = getMostCommonValue(ox, i);
			System.out.println("mcv " + i + " = " + mcv);
			final int iV = i;
			ox = ox.stream().filter(s -> s.charAt(iV) == mcv).toList();
			System.out.println("after " + i + ": " + ox);
			i++;
		}
		List<String> scrub = new ArrayList<>(lines);
		i = 0;
		while (scrub.size() > 1) {
			char mcv = getMostCommonValue(scrub, i);
			final int iV = i;
			scrub = scrub.stream().filter(s -> s.charAt(iV) != mcv).toList();
			i++;
		}
		int oxI = Integer.parseInt(ox.get(0), 2);
		int scrubI = Integer.parseInt(scrub.get(0), 2);
		System.out.println(String.format("oxygen=%s (%d), scrubber=%s (%d), life support=%d", ox.get(0), oxI, scrub.get(0), scrubI, oxI*scrubI));
	}

	private static char getMostCommonValue(List<String> lines, int pos) {
		int count = 0;
		for (String line : lines) {
			if (line.charAt(pos) == '1') {
				count++;
			}
		}
		System.out.println(count + "/" + lines.size());
		return count * 2 >= lines.size() ? '1' : '0';
	}
	
	private static char not(char x) {
		return x == '0' ? '1' : '0';
	}
		
}

// 2829456 too high