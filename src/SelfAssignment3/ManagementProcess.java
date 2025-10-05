package SelfAssignment3;

import java.util.ArrayList;
import java.util.List;

public class ManagementProcess extends Process{

	protected ManagementProcess(String id) {
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
            resources.addAll(operation.getResources());
        }
        return resources;
	}

}
