package com.gj.jc.threadpool;

import java.util.concurrent.*;

public class CallerRunPolicyThreadPool {

    private static ThreadPoolExecutor executorService = new ThreadPoolExecutor(10, 10,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(10));

    public static void main(String[] args) {
        //executorService.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        executorService.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executorService.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < 22; i++) {
            final int idx = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (idx >= 20) {
                            Thread.sleep(100);
                        } else {
                            Thread.sleep(10000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println(idx + "  run ---");
                }
            });
        }

    }
}
