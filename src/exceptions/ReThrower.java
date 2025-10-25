package exceptions;

public class ReThrower {
    public void riskyOperation(String input) throws Exception {
        try {
            if (input == null) {
                throw new NullPointerException("Input is null");
            }
            int value = Integer.parseInt(input);
            System.out.println("Result: " + value);
        } catch (NumberFormatException e) {
            System.out.println("Caught NumberFormatException, rethrowing...");
            throw e;
        }
    }
}