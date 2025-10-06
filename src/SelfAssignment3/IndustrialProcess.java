package SelfAssignment3;

import java.util.ArrayList;
import java.util.List;

public class IndustrialProcess extends Process {

    private final List<IOperation> operations;

    // Constructor for empty process
    protected IndustrialProcess(String id) {
        super(id);
        this.operations = new ArrayList<>();
    }

    // Constructor with initial operations
    protected IndustrialProcess(String id, List<IOperation> initialOperations) {
        super(id);
        this.operations = initialOperations;
    }

    @Override
    public double processDuration() {
        double totalTime = 0.0;
        for (IOperation operation : operations) {
            totalTime += operation.getNominalTimeMinutes();
        }
        return totalTime;
    }

    // Returns all resources used in the process
    public List<Resource> processResources() {
        List<Resource> resources = new ArrayList<>();
        for (IOperation operation : operations) {
            resources.addAll(operation.getResources());
        }
        return resources;
    }

    // Optional: Add operation manually
    public void addOperation(IOperation operation) {
        operations.add(operation);
    }

    // Optional: Get all operations
    public List<IOperation> getOperations() {
        return operations;
    }
}