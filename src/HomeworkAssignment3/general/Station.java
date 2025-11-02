package HomeworkAssignment3.general;


import HomeworkAssignment3.general.exceptions.WarehouseException;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

/**
 * Shared worker loop for all stations:
 * take() -> process(job) -> put() with built-in back-pressure via BlockingQueues.
 */
public abstract class Station<T extends Task> implements IStation, Runnable {
    protected final String id;                    // station id (for logs)

    protected final BlockingQueue<Task> in;        // input queue (inbox)
    protected final BlockingQueue<Task> out;


    public Station(BlockingQueue<Task> in, BlockingQueue<Task> out) {

        this.id = UUID.randomUUID().toString();
        this.in = in;
        this.out = out;
    }


    protected abstract void process(T task) throws WarehouseException;

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                T task = (T) in.take();                // waits if empty
                process(task);
            }
        } catch (InterruptedException stop) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            // As a safety net, if a station throws, convert task to EXCEPTION
            e.printStackTrace();
        }
    }

    @Override
    public void addToQueue(Task task) {
        out.add(task);
    }
}
