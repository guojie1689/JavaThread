package com.gj.jc.syncutil;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FutureTaskTest {


    private static FutureTask<Integer> futureTask = new FutureTask<Integer>(new Callable<Integer>() {
        @Override
        public Integer call() throws Exception {
            System.out.println("开始计算 ---");

            Thread.sleep(3000);

            return 100;
        }
    });

    public static RuntimeException launcherThrowable(Throwable t) {
        if (t instanceof RuntimeException) {
            return (RuntimeException) t;
        } else if (t instanceof Error) {
            throw (Error) t;
        } else
            throw new IllegalStateException("Not unchecked", t);
    }


    public static void main(String args[]) {

        Thread thread = new Thread(futureTask);

        try {
            thread.start();

            System.out.println("start ---");

            Integer result = futureTask.get();

            System.out.println(result);

        } catch (Exception e) {
            launcherThrowable(e);
        }
    }
}
