package CapstoneProject.agv.exceptions;

import CapstoneProject.general.exceptions.WarehouseException;

public class AGVException extends WarehouseException {

    public AGVException(String message) {
        super(message);
    }

    public AGVException(String message, Throwable cause) {
        super(message, cause);
    }
}
