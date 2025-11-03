package HomeworkAssignment3.loading.exceptions;

import HomeworkAssignment3.general.exceptions.WarehouseException;

public class CarNotFoundException extends WarehouseException {

    public CarNotFoundException(String message) {
        super(message);
    }
}
