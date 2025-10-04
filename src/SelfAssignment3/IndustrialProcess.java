package SelfAssignment3;

import java.util.ArrayList;
import java.util.List;

public class IndustrialProcess extends Process {
	
	protected IndustrialProcess(String id) {
		super(id);
	}


	@Override
	public double processDuration() {
		double totalTime = 0.0;
        for (IOperation operation : getOperations()) {
            totalTime += operation.getNominalTimeMinutes();
        }
        return totalTime;
	}

	@Override
	public List<Resource> processResources() {
		List<Resource> resources = new ArrayList<>();
        for (IOperation operation : getOperations()) {
            resources.addAll(operation.getAGVList());
        }
        return resources;
	}

}