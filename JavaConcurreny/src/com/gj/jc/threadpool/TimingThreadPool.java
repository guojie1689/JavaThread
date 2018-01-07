package com.gj.jc.threadpool;

import java.util.concurrent.*;

public class TimingThreadPool extends ThreadPoolExecutor {

    private ThreadLocal<Long> setTime = new ThreadLocal<Long>();

    public TimingThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public TimingThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public TimingThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public TimingThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        setTime.set(System.nanoTime());

    }

    @Override
    protected void terminated() {
        super.terminated();
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        Long beforeTime = setTime.get();
        System.out.println(Thread.currentThread().getName() + ":" + (System.nanoTime() - beforeTime));
    }

    public static void main(String[] args) {
        TimingThreadPool timingThreadPool = new TimingThreadPool(2, 2, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        for (int i = 0; i < 3; i++) {
            timingThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("run ---");
                }
            });
        }

    }
}
