package SelfAssignment3;

import java.util.List;

public abstract class IOperation {
	protected String id;
	protected String description;
	protected List<Resource> resources;

	public IOperation(String id, String description, List<Resource> resources) {
		this.id = id;
		this.description = description;
		this.resources = resources;
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public abstract double getNominalTimeMinutes();

	public abstract int getResourceRequired();
}