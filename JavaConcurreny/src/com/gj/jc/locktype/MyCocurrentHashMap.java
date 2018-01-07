package com.gj.jc.locktype;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

class Node<K, V> {
    public K key;
    public V value;
    public Node next;

    public Node(K key, V value, Node next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

}

public class MyCocurrentHashMap<K, V> {
    private final static int LOCK_NUM = 16;
    private Object[] locks = new Object[LOCK_NUM];
    private Node<K, V>[] buckets = new Node[LOCK_NUM];

    public MyCocurrentHashMap() {
        for (int i = 0; i < locks.length; i++) {
            locks[i] = new Object();
        }
    }

    private int getLockIdxByKey(K k) {
        return k.hashCode() % LOCK_NUM;
    }

    public Node put(K key, V value) {
        int hashIdx = getLockIdxByKey(key);

        synchronized (locks[hashIdx]) {

            if (buckets[hashIdx] == null) {
                buckets[hashIdx] = new Node<>(key, value, null);
            } else {
                Node head = buckets[hashIdx];
                Node lastNode = head;

                while (head != null) {

                    if (head.key.equals(key)) {
                        head.value = value;
                        return head;
                    }

                    lastNode = head;
                    head = head.next;
                }

                lastNode.next = new Node(key, value, null);
            }
        }

        return null;

    }

    public V get(K key) {
        int hashIdx = getLockIdxByKey(key);

        synchronized (locks[hashIdx]) {
            for (Node<K, V> node = buckets[hashIdx]; node != null; node = node.next) {
                if (node.key.equals(key)) {
                    return node.value;
                }
            }
        }

        return null;
    }

    public Set<K> keySet() {
        Set<K> set = new HashSet<>();

        for (int i = 0; i < locks.length; i++) {
            synchronized (locks[i]) {
                for (Node<K, V> node = buckets[i]; node != null; node = node.next) {
                    System.out.println("bucket " + i + " get key:" + node.key);
                    set.add(node.key);
                }
            }
        }

        return set;

    }

    public static void main(String[] args) {


        MyCocurrentHashMap<String, String> myCocurrentHashMap = new MyCocurrentHashMap();
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        int value = random.nextInt(100);
                        myCocurrentHashMap.put(value + "", value + "");
                    }
                }
            }).start();
        }


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String value = myCocurrentHashMap.get("19393");

        System.out.println("value:" + value);

        myCocurrentHashMap.keySet();

    }
}
