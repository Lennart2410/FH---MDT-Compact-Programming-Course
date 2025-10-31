package HomeworkAssignment1.general;


import HomeworkAssignment1.general.exceptions.WarehouseException;

import java.util.UUID;

/**
 * Shared worker loop for all stations:
 * take() -> process(job) -> put() with built-in back-pressure via BlockingQueues.
 */
public abstract class Station<T extends Task> implements IStation {
    protected final String id;                    // station id (for logs)


    public Station() {
        this.id = UUID.randomUUID().toString();
    }

    protected abstract Order process(T task) throws WarehouseException;
}
