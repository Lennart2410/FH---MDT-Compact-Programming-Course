package SelfAssignment2;

import java.util.ArrayList;
import java.util.List;

/** Process holding N operations */
public class IndustrialProcess {
	private final String id;
    private final List<IOperation> operations = new ArrayList<>();

    public IndustrialProcess(String id) {
        this.id = id;
    }

    public void addOperation(IOperation op) {
        operations.add(op);
    }

}
