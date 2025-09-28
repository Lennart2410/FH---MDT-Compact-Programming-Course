package SelfAssignment2;

import java.util.Comparator;
import java.util.List;

public class IndustrialProcessesSimulation {
	public static void main(String[] args) {
		IndustrialProcess process = new IndustrialProcess("Process-Alpha");

		// Add SingleOperations to the process
		process.addOperation(new SingleOperation("Op1", "Loading", 2, 10.0));
		process.addOperation(new SingleOperation("Op2", "Transport", 3, 25.0));
		process.addOperation(new SingleOperation("Op3", "Packing", 1, 5.0));
		process.addOperation(new SingleOperation("Op4", "Inspection", 2, 15.0));

		// Get all operations
		List<IOperation> operations = process.getOperations();

		// Calculate average nominal time
		double totalTime = 0;
		IOperation fastest = operations.get(0);
		IOperation slowest = operations.get(0);

		for (IOperation op : operations) {
			totalTime += op.getNominalTimeMinutes();
			if (op.getNominalTimeMinutes() < fastest.getNominalTimeMinutes()) {
				fastest = op;
			}
			if (op.getNominalTimeMinutes() > slowest.getNominalTimeMinutes()) {
				slowest = op;
			}
		}

		double averageTime = totalTime / operations.size();

		// Print results
		System.out.println("Industrial Process ID: " + process.getId());
		System.out.println("Average Nominal Time: " + averageTime + " minutes");
		System.out.println("Fastest Operation: " + fastest.getId() + " (" + fastest.getNominalTimeMinutes() + " min)");
		System.out.println("Slowest Operation: " + slowest.getId() + " (" + slowest.getNominalTimeMinutes() + " min)");

		// Print sorted list by finish time
		System.out.println("\nOperations sorted by nominal time:");
		operations.stream()
				.sorted(Comparator.comparingDouble(IOperation::getNominalTimeMinutes))
				.forEach(op -> System.out.println(op.getId() + ": " + op.getNominalTimeMinutes() + " min"));
	}
}
