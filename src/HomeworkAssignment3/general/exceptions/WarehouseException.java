package HomeworkAssignment3.general.exceptions;

public class WarehouseException extends Exception{
    public WarehouseException(String message) {
        super(message);
    }

    public WarehouseException(String message, Throwable cause) {
        super(message,cause);
    }
}
