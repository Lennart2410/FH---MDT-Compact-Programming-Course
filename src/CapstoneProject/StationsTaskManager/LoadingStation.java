package CapstoneProject.StationsTaskManager;

import java.util.concurrent.BlockingQueue;

public class LoadingStation extends Station{
    public LoadingStation(String id, BlockingQueue<Task> in, BlockingQueue<Task> out) {
        super(id, in, out);
    }

    @Override
    protected Task process(Task task) throws Exception {
        return null;
    }
}
