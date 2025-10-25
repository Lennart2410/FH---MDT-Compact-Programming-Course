package exceptions;

public class MultiExceptionHandler {
    public void process(String input) {
        try {
            int number = Integer.parseInt(input); // May throw NumberFormatException
            int result = 100 / number;            // May throw ArithmeticException
            System.out.println("Result: " + result);
        } catch (NumberFormatException | ArithmeticException e) {
            System.out.println("Handled exception: " + e.getClass().getSimpleName());
        }
    }
}