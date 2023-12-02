import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Day1 {
    public static void main(String[] args) throws IOException {
        int[] values = Util.readIntList(1);

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
