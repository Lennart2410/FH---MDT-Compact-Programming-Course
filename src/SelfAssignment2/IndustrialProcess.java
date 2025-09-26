package SelfAssignment2;

import java.util.List;

/**
 * Process holding N operations
 */
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
        for(IOperation operation : operations){
            totalTime += operation.getNominalTimeMinutes();
        }
        return totalTime;
    }

    public int processResources(){
        int avgResourceCount = 0;

        // Implementation to be done

        return avgResourceCount;
    }


}
