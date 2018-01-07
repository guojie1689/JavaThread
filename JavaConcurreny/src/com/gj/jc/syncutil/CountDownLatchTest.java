package com.gj.jc.syncutil;

import java.util.concurrent.CountDownLatch;

/**
 * 闭锁
 * <p>
 * 起始门 - 结束门 两个状态
 */
public class CountDownLatchTest {

    public static void main(String args[]) {
        int threadNum = 1000;
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(threadNum);

        for (int i = 0; i < threadNum; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        startGate.await();

                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    endGate.countDown();
                }
            }).start();
        }

        long start = System.currentTimeMillis();
        startGate.countDown();
        try {
            endGate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long runTime = System.currentTimeMillis() - start;

        System.out.println(runTime);

    }
}
