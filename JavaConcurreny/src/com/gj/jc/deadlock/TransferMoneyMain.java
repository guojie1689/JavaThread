package com.gj.jc.deadlock;

import java.util.Random;

class Account {
    private int amount = 10000;
    private String name;

    public Account(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void add(int value) {
        amount += value;
    }

    public void sub(int value) {
        amount -= value;
    }

    @Override
    public String toString() {
        return name;
    }
}

public class TransferMoneyMain {
    private static final int NUM_THREADS = 20;
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_ITERATIONS = 100000;

    private static Object objLock = new Object();

    private static void transferMoney(Account fromaccount, Account toAccount, int money) {
        int fromhash = System.identityHashCode(fromaccount);
        int toHash = System.identityHashCode(toAccount);

        if (fromhash < toHash) {
            synchronized (fromaccount) {
                synchronized (toAccount) {
                    System.out.println(fromaccount + "---" + toAccount);
                }
            }
        } else if (toHash < fromhash) {
            synchronized (toAccount) {
                synchronized (fromaccount) {
                    System.out.println(fromaccount + "---" + toAccount);
                }
            }
        } else {
            synchronized (objLock) {
                synchronized (fromaccount) {
                    synchronized (toAccount) {
                        System.out.println(fromaccount + "---" + toAccount);
                    }
                }
            }
        }

    }

    public static void main(String[] args) {
        final Random random = new Random();
        final Account[] accounts = new Account[NUM_ACCOUNTS];

        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account(i + "");
        }

        class TransferThread extends Thread {
            @Override
            public void run() {
                for (int i = 0; i < NUM_ITERATIONS; i++) {
                    int fromAccount = random.nextInt(NUM_ACCOUNTS);
                    int toAccount = random.nextInt(NUM_ACCOUNTS);
                    int amount = 10;

                    transferMoney(accounts[fromAccount], accounts[toAccount], amount);
                }
            }
        }

        for (int i = 0; i < NUM_THREADS; i++) {
            new TransferThread().start();
        }
    }
}
