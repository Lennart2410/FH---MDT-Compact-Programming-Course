package CapstoneProject.picking.exceptions;

import CapstoneProject.general.exceptions.WarehouseException;

/**
 * WarehouseException thrown during picking operations.
 * Used to signal unexpected failures such as unavailable items or system
 * errors.
 */
public class PickingException extends WarehouseException {

    /**
     * Constructs a new PickingException with a detail message.
     * 
     * @param message the detail message
     */
    public PickingException(String message) {
        super(message);
    }

    /**
     * Constructs a new PickingException with a detail message and cause.
     * Supports exception chaining.
     * 
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public PickingException(String message, Throwable cause) {
        super(message, cause);
    }
}