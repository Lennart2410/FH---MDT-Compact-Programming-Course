package CapstoneProject.loading.exceptions;

import CapstoneProject.general.exceptions.WarehouseException;

public class CarNotFoundException extends WarehouseException {

    public CarNotFoundException(String message) {
        super(message);
    }
}
