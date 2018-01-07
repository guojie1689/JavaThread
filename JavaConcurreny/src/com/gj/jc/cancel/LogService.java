package com.gj.jc.cancel;

import java.security.SecureRandom;
import java.util.concurrent.*;

public class LogService {
    private final BlockingQueue<String> queue;
    private LoggerThread thread;
    private boolean isShutdown = false;
    private int reservations;

    public LogService() {
        queue = new LinkedBlockingQueue<>(100);
        thread = new LoggerThread();
    }

    public void addLog(String msg) throws InterruptedException {
        synchronized (this) {
            if (isShutdown) {
                throw new IllegalStateException("is closed");
            }

            reservations++;
        }

        System.out.println(Thread.currentThread().getName() + ":" + msg);
        queue.put(msg);
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        synchronized (this) {
            isShutdown = true;
        }

        thread.interrupt();
    }

    private void writeLog(String msg) {
        System.out.println("写入：" + msg);
    }

    private class LoggerThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {

                    synchronized (LogService.this) {
                        if (isShutdown && reservations == 0) {
                            break;
                        }
                    }

                    String msg = queue.take();
                    synchronized (LogService.this) {
                        reservations--;
                    }

                    writeLog(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {

        SecureRandom secureRandom = new SecureRandom();

        LogService logService = new LogService();
        logService.start();

        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 10; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted()) {
                        int value = secureRandom.nextInt();

                        try {
                            logService.addLog(value + "");
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            });
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

    }

}
