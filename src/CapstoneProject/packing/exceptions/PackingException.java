package CapstoneProject.packing.exceptions;

import CapstoneProject.general.exceptions.WarehouseException;

public class PackingException extends WarehouseException {
    public PackingException(String message) { super(message); }
    public PackingException(String message, Throwable cause) { super(message, cause); }
}
