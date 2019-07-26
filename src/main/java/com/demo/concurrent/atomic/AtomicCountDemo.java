package com.demo.concurrent.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AtomicCountDemo {

    volatile static int count = 0;

    public static void main(String[] args) {

        AtomicCountDemo atomicCountDemo = new AtomicCountDemo();

        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int j = 0; j < 100; j++) {
            service.submit(atomicCountDemo::increaseOne);
        }

        service.shutdown();

        while (!service.isTerminated()) {
        }

        System.out.println(count);

    }

    public void increaseOne() {

        int newVal;
        do {
            newVal = count + 1;
        } while (count != compareAndSwap(count, newVal));

    }

    /**
     * CAS
     *
     * @param expVal 期望值
     * @param newVal 新值
     */
    private int compareAndSwap(int expVal, int newVal) {
        int curVal = count;
        if (curVal == expVal) {
            count = newVal;
        }
        return curVal;
    }

}
