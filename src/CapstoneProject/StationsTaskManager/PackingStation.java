package CapstoneProject.StationsTaskManager;

import java.util.concurrent.BlockingQueue;

public class PackingStation extends Station{
    public PackingStation(String id, BlockingQueue<Task> in, BlockingQueue<Task> out) {
        super(id, in, out);
    }

    @Override
    protected Task process(Task task) throws Exception {
        return task;
    }
}
