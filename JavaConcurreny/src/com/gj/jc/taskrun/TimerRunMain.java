package com.gj.jc.taskrun;

import java.util.Timer;
import java.util.TimerTask;

import static java.util.concurrent.TimeUnit.SECONDS;

public class TimerRunMain {

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new ThrowTask(), 1);
        try {
            SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        timer.schedule(new ThrowTask(), 1);
        try {
            SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    static class ThrowTask extends TimerTask {
        public void run() {

            System.out.println("run ----");
            throw new RuntimeException();
        }
    }
}
