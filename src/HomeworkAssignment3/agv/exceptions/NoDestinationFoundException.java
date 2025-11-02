package HomeworkAssignment3.agv.exceptions;


public class NoDestinationFoundException extends AGVException {
    public NoDestinationFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDestinationFoundException(String message) {
        super(message);
    }
}
