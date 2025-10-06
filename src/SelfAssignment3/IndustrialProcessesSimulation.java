package SelfAssignment3;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IndustrialProcessesSimulation {

    public static void main(String[] args) {
        performSelfAssignmentThreeTasks();
        // performSelfAssignmentTwoTasks();
    }

    private static void performSelfAssignmentTwoTasks() {
        AGV agv_01 = new AGV("A01", 100.0, 15.0, new Position(40, 40), 10, 4.0f, 2.0f, LocalDate.of(2025, 10, 3));
        AGV agv_02 = new AGV("A02", 50.0, 20.0, new Position(10, 78), 12, 10.0f, 6.0f, LocalDate.now());
        AGV agv_03 = new AGV("A03", 85.0, 5.0, new Position(1, 1), 10, 2.0f, 1.0f, LocalDate.of(2020, 1, 1));

        TransportOperation op1 = new TransportOperation("OP01", "Transport goods", 100.0, 50.0,
                List.of(agv_01, agv_02));
        TransportOperation op2 = new TransportOperation("OP02", "Load materials", 45.0, 30.0, List.of(agv_03));
        TransportOperation op3 = new TransportOperation("OP03", "Unload items", 50.0, 20.0, List.of(agv_01));

        List<IOperation> operations = List.of(op1, op2, op3);

        IndustrialProcess industrialProcess = new IndustrialProcess("Process01", operations);

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

        List<Resource> allResources = industrialProcess.processResources();

        List<AGV> lowBatteryAGVs = allResources.stream()
                .filter(resource -> resource instanceof AGV)
                .map(resource -> (AGV) resource)
                .filter(agv -> agv.getBatteryLoad() < 20.0)
                .collect(Collectors.toList());

        Map<String, Integer> agvUsageCount = new HashMap<>();
        for (IOperation op : operations) {
            for (Resource resource : op.getResources()) {
                if (resource instanceof AGV) {
                    agvUsageCount.put(resource.getId(), agvUsageCount.getOrDefault(resource.getId(), 0) + 1);
                }
            }
        }

        List<String> reusedAGVs = agvUsageCount.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        AGV fastestAGV = allResources.stream()
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

        HumanResource worker_01 = new HumanResource("H01", "Ali", "Loader", 3);
        HumanResource worker_02 = new HumanResource("H02", "Sara", "Inspector", 5);

        MaterialResource mat1 = new MaterialResource("M01", "Steel", 500.0);
        SoftwareResource soft1 = new SoftwareResource("S01", "SAP", "2025.3");

        TransportOperation op1 = new TransportOperation("OP01", "Transport goods", 100.0, 50.0,
                List.of(agv_01, agv_02));
        HumanOperation op2 = new HumanOperation("OP02", "Load materials", List.of(worker_01));
        TransportOperation op3 = new TransportOperation("OP03", "Unload items", 50.0, 20.0, List.of(agv_01));
        HumanOperation op4 = new HumanOperation("OP04", "Inspect items", List.of(worker_02));
        HumanOperation op5 = new HumanOperation("OP05", "Configure system", List.of(soft1));
        TransportOperation op6 = new TransportOperation("OP06", "Deliver steel", 80.0, 500.0, List.of(mat1));

        List<IOperation> operations = List.of(op1, op2, op3, op4, op5, op6);
        IndustrialProcess process = new IndustrialProcess("Process01", operations);

        System.out.println("=== Task 3: Simulation Output ===");
        System.out.println("Total Process Duration: " + process.processDuration() + " minutes");
        System.out.println("Resources Used:");
        process.processResources().forEach(r -> System.out.println(r.getId()));
    }
}