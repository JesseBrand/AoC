package nl.jessebrand.leetcode;

import static nl.jessebrand.aoc.Utils.assertTrue;

public class D73 {

    public static void main(String[] args) {
        performTest("ADOBECODEBANC", "ABC", "BANC");
        performTest("a", "a", "a");
        performTest("a", "aa", "");
    }

    private static void performTest(String large, String small, String expected) {
        String answer = minWindow(large, small);
        System.out.println("answer: " + answer);
        assertTrue(answer.equals(expected));
    }

    public static String minWindow(String s, String t) {
        String shortest = null;
        for (int i = 0; i < t.length(); i++) {
            String shortestX = findLowest(s, t.substring(i, i + 1), t.substring(0, i) + t.substring(i + 1));
            if (shortest == null || shortestX.length() < shortest.length()) {
                shortest = shortestX;
            }
        }
        if (shortest == null) {
            return "";
        }
        return shortest;
    }

    private static String findLowest(String large, String character, String others) {
        String working = large;
        String result = null;
        while (true) {
            int start = working.indexOf(character);
            if (start == -1) {
                break;
            }
            working = working.substring(start);
            int latest = 0;
            for (char c : others.toCharArray()) {
                int index = working.indexOf(c, 1);
                if (index == -1) {
                    index = Integer.MAX_VALUE;
                }
                latest = Math.max(latest, index);
            }
            if (latest == Integer.MAX_VALUE) {
                break;
            }
            if (result == null || result.length() > (latest + 1)) {
                result = working.substring(0, latest + 1);
            }
            working = working.substring(1);
        }
//        System.out.println(result);
        return result;
    }
}
