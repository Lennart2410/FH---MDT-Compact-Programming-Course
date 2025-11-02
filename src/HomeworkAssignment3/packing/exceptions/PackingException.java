package HomeworkAssignment3.packing.exceptions;

import HomeworkAssignment3.general.exceptions.WarehouseException;

public class PackingException extends WarehouseException {
    public PackingException(String message) { super(message); }
    public PackingException(String message, Throwable cause) { super(message, cause); }
}
