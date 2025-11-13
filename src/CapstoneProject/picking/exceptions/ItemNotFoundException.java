package CapstoneProject.picking.exceptions;

/**
 * Exception thrown when a requested item is not found during picking.
 * Used to signal missing inventory or shelf errors.
 */
public class ItemNotFoundException extends PickingException {

    /**
     * Constructs a new ItemNotFoundException with a detail message.
     * 
     * @param message the detail message
     */
    public ItemNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ItemNotFoundException with a detail message and cause.
     * Supports exception chaining.
     * 
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public ItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}