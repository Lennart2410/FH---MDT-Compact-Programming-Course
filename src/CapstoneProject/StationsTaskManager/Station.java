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


    @Override
    public final void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Task task= in.take();                // waits if empty
                Task next = process(task);
                if (next != null && out != null) {
                    out.put(next);                // waits if full (back-pressure)
                }

            }
        } catch (InterruptedException stop) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            // As a safety net, if a station throws, convert task to EXCEPTION
            e.printStackTrace();
        }
    }

    protected abstract Task process(Task task) throws Exception;
}
