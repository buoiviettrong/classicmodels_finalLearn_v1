package com.nixagh.classicmodels._ATestNewContent;

import java.util.Random;

public class ThreadRun {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Random random = new Random();
        // runable interface
        Runnable runnable = () -> {
            double i = random.nextDouble(1000) % random.nextDouble(1000);
            System.out.println("runnable: " + i);
        };

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
        }

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("total : " + timeElapsed);
    }
}
