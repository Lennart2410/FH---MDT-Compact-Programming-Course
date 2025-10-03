package SelfAssignment3;

import java.time.LocalDate;

public abstract class NonHumanResource extends Resource {

    // Date of purchase
    private final LocalDate purchaseDate;

    public NonHumanResource(String id, LocalDate purchaseDate) {
        super(id);

        this.purchaseDate = purchaseDate;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    @Override
    public String toString() {
        return super.toString() +
                "Date of purchase: " + getPurchaseDate() + "\n";
    }
}
