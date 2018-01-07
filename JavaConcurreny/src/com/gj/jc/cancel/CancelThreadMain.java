package com.gj.jc.cancel;

import java.math.BigInteger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class PrimeProducter extends Thread {

    private BlockingQueue<BigInteger> blockingQueue = new ArrayBlockingQueue<>(10);

    @Override
    public void run() {
        BigInteger bigInteger = BigInteger.ONE;


        try {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("add -----");
                blockingQueue.put(bigInteger.nextProbablePrime());
            }
        } catch (InterruptedException e) {
            //线程退出
            e.printStackTrace();
        }

    }

    public void cancel() {
        interrupt();
    }
}

public class CancelThreadMain {

    public static void main(String[] args) {
        PrimeProducter runThread = new PrimeProducter();

        runThread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        runThread.cancel();

    }

}
