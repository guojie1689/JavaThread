package com.gj.jc.syncutil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {

    private static List<String> stringList = new ArrayList();
    private static Semaphore semaphore = new Semaphore(10);

    private static void addToList(String value) throws InterruptedException {
        semaphore.acquire();
        stringList.add(value);
        System.out.println("add:" + value);
    }

    private static void removeValue(String value) {
        stringList.remove(value);
        semaphore.release();
        System.out.println("remove:" + value);
    }


    public static void main(String args[]) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        addToList("i" + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    removeValue("i" + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
