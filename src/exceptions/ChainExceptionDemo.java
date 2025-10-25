package exceptions;

public class ChainExceptionDemo {
    public void triggerChain() {
        try {
            try {
                throw new IllegalArgumentException("Initial cause");
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Wrapped exception", e); 
            }
        } catch (RuntimeException e) {
            System.out.println("Caught chained exception: " + e.getMessage());
            System.out.println("Original cause: " + e.getCause());
        }
    }
}