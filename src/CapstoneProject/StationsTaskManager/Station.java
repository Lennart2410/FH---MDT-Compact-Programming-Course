package CapstoneProject.StationsTaskManager;

import java.util.concurrent.BlockingQueue;

/**
 * Shared worker loop for all stations:
 * take() -> process(job) -> put() with built-in back-pressure via BlockingQueues.
 */
public abstract class Station implements IStation {
    protected final String id;                    // station id (for logs)
    protected final BlockingQueue<Task> in;        // input queue (inbox)
    protected final BlockingQueue<Task> out;       // output queue (outbox)

    public Station(String id, BlockingQueue<Task> in, BlockingQueue<Task> out) {
        this.id = id;
        this.in = in;
        this.out = out;
    }

    /** Non-overrideable worker loop (Template Method pattern). */
    @Override
    public final void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Task j = in.take();                // waits if empty
                Task next = process(j);
                if (next != null && out != null) {
                    out.put(next);                // waits if full (back-pressure)
                }

            }
        } catch (InterruptedException stop) {
            Thread.currentThread().interrupt();   // graceful shutdown
        } catch (Exception e) {
            // As a safety net, if a station throws, convert task to EXCEPTION
            // In practice you’ll also log and/or send to a dead-letter queue.
            e.printStackTrace();
        }
    }

    /**
     * Implement this in concrete stations to transform the task
     * (e.g., PICKING -> PACKING, add payload, etc.).
     */
    protected abstract Task process(Task task) throws Exception;
}
