package SelfAssignment3;

import java.util.ArrayList;
import java.util.List;

public class IndustrialProcess extends Process {

    private final List<IOperation> operations;

    protected IndustrialProcess(String id) {
        super(id);
        this.operations = new ArrayList<>();
    }

	protected IndustrialProcess(String id, List<IOperation> initialOperations) {
		super(id);
        this.operations = initialOperations;
	}


	@Override
	public double processDuration() {
		double totalTime = 0.0;
        for (IOperation operation : getOperations()) {
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