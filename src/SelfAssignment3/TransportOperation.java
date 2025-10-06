package SelfAssignment3;

import java.util.List;

public class TransportOperation extends IOperation {
    private double distance;
    private double loadWeight;

    public TransportOperation(String id, String description, double distance, double loadWeight,
            List<Resource> resources) {
        super(id, description, resources);
        this.distance = distance;
        this.loadWeight = loadWeight;
    }

    @Override
    public double getNominalTimeMinutes() {
        double avgSpeed = resources.stream()
                .filter(r -> r instanceof AGV)
                .map(r -> ((AGV) r).getActSpeed())
                .mapToDouble(Float::doubleValue)
                .average()
                .orElse(1.0); // fallback speed
        return distance / avgSpeed;
    }

    @Override
    public int getResourceRequired() {
        return resources.size();
    }
}