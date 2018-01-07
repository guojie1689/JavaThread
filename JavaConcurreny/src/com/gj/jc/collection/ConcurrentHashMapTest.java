package com.gj.jc.collection;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapTest {

    private static ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();


    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            Thread thread;
            String tName = "idx:" + i;
            if (i % 2 == 0) {
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        concurrentHashMap.put(tName, tName);
                    }
                }, tName);
            } else {
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        concurrentHashMap.get(tName);
                    }
                }, tName);
            }
            thread.start();
        }
    }
}
