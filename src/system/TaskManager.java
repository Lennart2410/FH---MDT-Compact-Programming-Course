package system;

public class TaskManager {
    public void assignTask(StorageVehicle vehicle, String task) {
        String message = "Assigned task: " + task;
        vehicle.logActivity(message);
    }
}