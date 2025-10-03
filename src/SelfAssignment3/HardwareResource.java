package SelfAssignment3;

import java.time.LocalDate;
import java.util.Random;

public abstract class HardwareResource extends NonHumanResource {

    // 9 digit random number
    private final String serialNumber;
    private Position position;

    public HardwareResource(String id, Position position, LocalDate purchaseDate) {
        super(id, purchaseDate);
        this.position = position;
        this.serialNumber = String.valueOf(new Random().nextLong(999999999 - 100000000 + 1) + 100000000);
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return super.toString() +
                "Serial number: " + getSerialNumber() + "\n" +
                "Current position: X-> " + getPosition().getxCoordinate() + " - Y-> " + getPosition().getyCoordinate() + "\n";

    }

}
