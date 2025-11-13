package HomeworkAssignment3.general;


import HomeworkAssignment3.general.exceptions.WarehouseException;
import HomeworkAssignment3.logging.LogFiles;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

/**
 * Shared worker loop for all stations:
 * take() -> process(job) -> put() with built-in back-pressure via BlockingQueues.
 */
public abstract class Station<T extends Task> implements IStation, Runnable {
    protected final String id;                    // station id (for logs)
    protected final LogFiles logManager;
    protected final BlockingQueue<Task> in;        // input queue (inbox)
    protected final BlockingQueue<Task> out;
    private final OrderStatusListener listener;

    public Station(BlockingQueue<Task> in, BlockingQueue<Task> out, OrderStatusListener listener) {
        logManager = new LogFiles();
        this.id = UUID.randomUUID().toString();
        this.in = in;
        this.out = out;
        this.listener = listener;
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

    public LogFiles getLogManager() {
        return logManager;
    }

    public OrderStatusListener getListener() {
        return listener;
    }
}
