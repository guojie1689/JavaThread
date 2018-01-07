package com.gj.jc.cancel;

import java.util.*;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 调用shutdownnow时，通过getCanceledTask获取到还未执行的任务
 */
public class TrackingExecutor extends AbstractExecutorService {

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private final Set<Runnable> tasksCancelledAtShutdown = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void shutdown() {
        executorService.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return executorService.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executorService.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executorService.awaitTermination(timeout, unit);
    }

    public List<Runnable> getCanceledTasks() {
        if (!executorService.isTerminated())
            throw new IllegalStateException();

        return new ArrayList<Runnable>(tasksCancelledAtShutdown);
    }

    @Override
    public void execute(final Runnable command) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    command.run();
                } finally {
                    if (isShutdown() && Thread.currentThread().isInterrupted())
                        tasksCancelledAtShutdown.add(command);
                }
            }
        });
    }

    private static void saveCancelTask(List<Runnable> runnables) {

        System.out.println("entry saveCancelTask --");

        if (runnables == null || runnables.size() == 0) {
            System.out.println("cancelRunnable is null return ");
            return;
        }

    }

    public static void main(String args[]) {

        TrackingExecutor service = new TrackingExecutor();

        for (int i = 0; i < 100; i++) {
            int index = i;
            service.execute(new Runnable() {
                @Override
                public void run() {

                    for (long j = 0; j < index * 999999; j++) {
                        if (Thread.currentThread().isInterrupted()) {
                            return;
                        }
                    }

                    System.out.println(index + " ---- run");
                }
            });
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        saveCancelTask(service.shutdownNow());

        try {
            service.awaitTermination(3000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        saveCancelTask(service.getCanceledTasks());

    }

}
