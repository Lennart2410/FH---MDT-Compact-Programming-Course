package SelfAssignment3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Process {
	private final String id;                      
    private final List<IOperation> operations;    

    protected Process(String id) {
        this.id = id;
        this.operations = new ArrayList<>();
    }

    public String getId() { return id; }

    public int getNoOfOperations() { return operations.size(); }

    public List<IOperation> getOperations() {
        return Collections.unmodifiableList(operations);
    }

    public void addOperation(IOperation op) {
        operations.add(op);
    }

    public abstract double processDuration();

    public abstract List<Resource> processResources();

}
