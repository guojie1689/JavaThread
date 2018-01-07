package com.gj.jc.threadpool;

import java.util.concurrent.*;

class MyThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable r) {
        return new MyThread(r);
    }
}

class MyThread extends Thread {
    private Runnable runnable;

    public MyThread(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void run() {
        System.out.println("run ----");
        runnable.run();
    }
}


public class ThreadFactoryMain {

    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(10, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new MyThreadFactory());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("runnable is run --");
            }
        });

    }


}
