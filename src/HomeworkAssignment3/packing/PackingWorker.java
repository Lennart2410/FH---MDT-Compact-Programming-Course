package HomeworkAssignment3.packing;

public class PackingWorker {


    public enum WorkerType { EMPLOYEE, MACHINE }

    private final String workerId;                 // e.g.,"M-1"
    private final long baseMs;                     // base processing time
    private final double speedFactor;// <1 faster, >1 slower
    private boolean currentlyOccupied;
    public PackingWorker(String workerId, long baseMs, double speedFactor) {
        this.workerId = workerId;
        this.baseMs = baseMs;
        this.speedFactor = speedFactor;
    }


    public String getWorkerId() {
        return workerId;
    }

    public long getBaseMs() {
        return baseMs;
    }

    public double getSpeedFactor() {
        return speedFactor;
    }

    public boolean isCurrentlyOccupied() {
        return currentlyOccupied;
    }

    public void setCurrentlyOccupied(boolean currentlyOccupied) {
        this.currentlyOccupied = currentlyOccupied;
    }

}
