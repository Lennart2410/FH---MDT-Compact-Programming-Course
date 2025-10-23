package CapstoneProject.StationsTaskManager;

import java.util.concurrent.BlockingQueue;

public class PickingStation extends Station {
    public PickingStation(String id, BlockingQueue<Task> in, BlockingQueue<Task> out) {
        super(id, in, out);
    }

    @Override
    protected Task process(Task task) throws Exception {
        return null;
    }
}
