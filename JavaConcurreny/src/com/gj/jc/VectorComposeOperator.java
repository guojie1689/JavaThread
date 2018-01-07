package com.gj.jc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class VectorComposeOperator {

    public Vector<String> vector = new Vector();

    private void putIfAbsent(String value) {
        synchronized (vector) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!vector.contains(value)) {
                vector.add(value);
                System.out.println("not exist add");
            } else {
                System.out.println("exist");
            }


        }
    }

    public static void main(String[] args) {

        VectorComposeOperator vectorComposeOperator = new VectorComposeOperator();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                vectorComposeOperator.putIfAbsent("123");
            }
        });

        thread.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        vectorComposeOperator.vector.add("123");
        System.out.println("add");

    }

}
