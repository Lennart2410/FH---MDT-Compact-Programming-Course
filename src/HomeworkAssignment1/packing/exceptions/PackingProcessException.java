package HomeworkAssignment1.packing.exceptions;

public class PackingProcessException extends RuntimeException{
    public PackingProcessException(String message) { super(message); }
    public PackingProcessException(String message, Throwable cause) { super(message, cause); }
}
