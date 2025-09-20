package SelfAssignment1.Problem2;

public class Problem2 {
    public static void main(String[] args) {
        int nValues = 50;
        // defined the outer for-loop to make it skippable
        outer:
        for (int i = 2; i <= nValues; i++) {
            // Changed to Math.sqrt for the square root and to lesser equals to include the square root itself
            for (int j = 2; j <= Math.sqrt(i); j++) {
                if (i % j == 0) {
                    // Skip the outer loop, because it is no prime anyway
                    continue outer;
                }
            }
            System.out.println(i);
        }
    }
}