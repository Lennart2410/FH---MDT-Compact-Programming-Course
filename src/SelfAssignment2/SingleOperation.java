package SelfAssignment2;


import java.util.List;

public class SingleOperation implements IOperation {
    private final String id;
    private final String description;
    private final double nominalTimeMinutes;
    private List<AGV> agvResources;


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
    public List<AGV> getAGVList() {
        return agvResources;
    }


    @Override
    public double getNominalTimeMinutes() {
        return nominalTimeMinutes;
    }


    @Override
    public int getAgvRequired() {
        return agvResources.size();
    }
}


