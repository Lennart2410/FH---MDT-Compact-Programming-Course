package SelfAssignment3;

import java.time.LocalDate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IndustrialProcessesSimulation {

    public static void main(String[] args) {

        performSelfAssignmentThreeTasks();
        //performSelfAssignmentTwoTasks();

    }

    private static void performSelfAssignmentTwoTasks() {
        // Step 1: Create AGVs
        AGV agv_01 = new AGV("A01", 100.0, 15.0, new Position(40, 40), 10, 4.0f, 2.0f, LocalDate.of(2025, 10, 3));
        AGV agv_02 = new AGV("A02", 50.0, 20.0, new Position(10, 78), 12, 10.0f, 6.0f, LocalDate.now());
        AGV agv_03 = new AGV("A03", 85.0, 5.0, new Position(1, 1), 10, 2.0f, 1.0f, LocalDate.of(2020, 1, 1));

        // Step 2: Create Operations
        SingleOperation op1 = new SingleOperation("OP01", "Transport goods", 30.0, List.of(agv_01, agv_02));
        SingleOperation op2 = new SingleOperation("OP02", "Load materials", 45.0, List.of(agv_03));
        SingleOperation op3 = new SingleOperation("OP03", "Unload items", 20.0, List.of(agv_01));

        // Step 3: Create IndustrialProcess
        List<IOperation> operations = List.of(op1, op2, op3);
        IndustrialProcess process = new IndustrialProcess("Process01", operations);

        // === Task 1: Operation Analysis ===
        double avgTime = operations.stream()
                .mapToDouble(IOperation::getNominalTimeMinutes)
                .average()
                .orElse(0.0);

        IOperation fastest = operations.stream()
                .min((a, b) -> Double.compare(a.getNominalTimeMinutes(), b.getNominalTimeMinutes()))
                .orElse(null);

        IOperation slowest = operations.stream()
                .max((a, b) -> Double.compare(a.getNominalTimeMinutes(), b.getNominalTimeMinutes()))
                .orElse(null);

        List<IOperation> sorted = operations.stream()
                .sorted((a, b) -> Double.compare(a.getNominalTimeMinutes(), b.getNominalTimeMinutes()))
                .collect(Collectors.toList());

        System.out.println("=== Task 1: Operation Analysis ===");
        System.out.println("Average Operation Time: " + avgTime + " minutes");
        System.out.println("Fastest Operation: " + (fastest != null ? fastest.getId() : "None"));
        System.out.println("Slowest Operation: " + (slowest != null ? slowest.getId() : "None"));
        System.out.println("Sorted Operations by Time:");
        sorted.forEach(op -> System.out.println(op.getId() + " - " + op.getNominalTimeMinutes() + " min"));

        // === Task 2: AGV Analysis ===
        List<Resource> allAGVs = process.processResources();

        List<AGV> lowBatteryAGVs = allAGVs.stream()
                .filter(resource -> resource instanceof AGV)
                .map(resource -> (AGV) resource)
                .filter(agv -> agv.getBatteryLoad() < 20.0)
                .collect(Collectors.toList());

        Map<String, Integer> agvUsageCount = new HashMap<>();
        for (IOperation op : operations) {
            for (Resource agv : op.getResourceList()) {
                agvUsageCount.put(agv.getId(), agvUsageCount.getOrDefault(agv.getId(), 0) + 1);
            }
        }

        List<String> reusedAGVs = agvUsageCount.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        AGV fastestAGV = allAGVs.stream()
                .filter(resource -> resource instanceof AGV)
                .map(resource -> (AGV) resource)
                .max((a, b) -> Float.compare(a.getMaxSpeed(), b.getMaxSpeed()))
                .orElse(null);

        System.out.println("\n=== Task 2: AGV Analysis ===");
        System.out.println("AGVs with low battery (<20%):");
        if (lowBatteryAGVs.isEmpty()) {
            System.out.println("None");
        } else {
            lowBatteryAGVs.forEach(agv -> System.out.println(agv.getId()));
        }

        System.out.println("AGVs used in multiple operations:");
        if (reusedAGVs.isEmpty()) {
            System.out.println("None");
        } else {
            reusedAGVs.forEach(System.out::println);
        }

        System.out.println("Fastest AGV: " + (fastestAGV != null ? fastestAGV.getId() : "None"));
    }

    private static void performSelfAssignmentThreeTasks() {
        AGV agv_01 = new AGV("A01", 100.0, 15.0, new Position(40, 40), 10, 4.0f, 2.0f, LocalDate.of(2025, 10, 3));
        AGV agv_02 = new AGV("A02", 50.0, 20.0, new Position(10, 78), 12, 10.0f, 6.0f, LocalDate.now());
        AGV agv_03 = new AGV("A03", 85.0, 5.0, new Position(1, 1), 10, 2.0f, 1.0f, LocalDate.of(2020, 1, 1));

        // Step 2: Create Operations
        SingleOperation op1 = new SingleOperation("OP01", "Transport goods", 30.0, List.of(agv_01, agv_02));
        SingleOperation op2 = new SingleOperation("OP02", "Load materials", 45.0, List.of(agv_03));
        SingleOperation op3 = new SingleOperation("OP03", "Unload items", 20.0, List.of(agv_01));

        // Step 3: Create IndustrialProcess
        List<IOperation> operations = List.of(op1, op2, op3);
        IndustrialProcess process = new IndustrialProcess("Process01", operations);

        System.out.println(agv_01);
    }
}