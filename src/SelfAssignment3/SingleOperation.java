package SelfAssignment3;

import java.util.List;

public class SingleOperation implements IOperation {
	private final String id;
	private final String description;
	private final double nominalTimeMinutes;
	private final List<Resource> resources;

	public SingleOperation(String id, String description,
			double nominalTimeMinutes, List<Resource> agvResources) {
		this.id = id;
		this.description = description;
		this.nominalTimeMinutes = nominalTimeMinutes;
		this.resources = agvResources;
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public List<Resource> getResourceList() {
		return resources;
	}

	@Override
	public double getNominalTimeMinutes() {
		return nominalTimeMinutes;
	}

	@Override
	public int getResourcesRequired() {
		return resources.size();
	}
}
