package SelfAssignment2;


public class SingleOperation implements IOperation {
    private final String id;
    private final String description;
    private final double nominalTimeMinutes;


    public SingleOperation(String id, String description,
                           double nominalTimeMinutes) {
        this.id = id;
        this.description = description;
        this.nominalTimeMinutes = nominalTimeMinutes;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }


    @Override
    public double getNominalTimeMinutes() {
        return nominalTimeMinutes;
    }


    @Override
    public int getAgvRequired() {
        // To be implemented
        return 0;
    }
}


