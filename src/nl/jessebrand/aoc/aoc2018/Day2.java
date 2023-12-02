package nl.jessebrand.aoc.aoc2018;

import java.io.IOException;
import java.util.List;

import nl.jessebrand.aoc.Utils;

public class Day2 {

    public static void main(String[] args) throws IOException {
        List<String> values = Utils.readFile("2018/d02");

        int total2s = 0;
        int total3s = 0;

        for (final String str : values) {
            boolean found2 = false;
            boolean found3 = false;
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                long count = str.chars().filter(chr -> chr == c).count();
                if (count == 2)
                    found2 = true;
                if (count == 3)
                    found3 = true;
            }
            if (found2)
                total2s++;
            if (found3)
                total3s++;
        }
        System.out.println(String.format("%d * %d = %d", total2s, total3s, total2s * total3s));
    }

}

// 130410 too high