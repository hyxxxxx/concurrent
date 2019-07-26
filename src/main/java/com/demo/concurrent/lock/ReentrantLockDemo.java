package com.demo.concurrent.lock;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    private final ReentrantLock lock = new ReentrantLock();
    private int count;

    public int get() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }

    public void addOne() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ReentrantLockDemo demo = new ReentrantLockDemo();
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 10000000; i++) {
            executor.submit(demo::addOne);
        }

        Future<Integer> future = executor.submit(demo::get);

        System.out.println(future.get());

        executor.shutdown();

    }

}
