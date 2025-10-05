package SelfAssignment3;

import java.util.ArrayList;
import java.util.List;

/** Process holding N operations */
public class IndustrialProcess {
    private final String id;
    private final List<IOperation> operations;

    public IndustrialProcess(String id, List<IOperation> operations) {
        this.id = id;
        this.operations = operations;
    }

    public String getId() {
        return id;
    }

    public double processDuration() {
        double totalTime = 0.0;
        for (IOperation operation : operations) {
            totalTime += operation.getNominalTimeMinutes();
        }
        return totalTime;
    }

    public List<Resource> processResources() {
        List<Resource> resources = new ArrayList<>();
        for (IOperation operation : operations) {
            resources.addAll(operation.getResources());
        }
        return resources;
    }

}