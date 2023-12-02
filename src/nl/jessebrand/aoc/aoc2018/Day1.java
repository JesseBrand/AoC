package nl.jessebrand.aoc.aoc2018;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.jessebrand.aoc.Utils;

public class Day1 {
    public static void main(String[] args) throws IOException {
        List<Integer> values = Utils.readFileInts("2018/d01");

        Set<Integer> existingValues = new HashSet<>();
        int value = 0;
        int loop = 0;
        while (true) {
            loop++;
            for (int val : values) {
                value += val;
                if (existingValues.contains(value)) {
                    System.out.println(String.format("%d after %d repetitions", value, loop));
                    System.exit(0);
                } else {
                    existingValues.add(value);
                }
            }
        }
    }
}
