package CapstoneProject.StationsTaskManager;

import CapstoneProject.agv.AgvTask;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AGVRunner implements Runnable {
    private final BlockingQueue<Task> from, to;
        private final long travelMs;
        public AGVRunner( BlockingQueue<Task> from, BlockingQueue<Task> to, long travelMs) {
            this.travelMs = travelMs;
            this.from = from;
            this.to = to;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Task j = from.take();
                    Thread.sleep(travelMs);
                    to.put(j);
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }

    }
