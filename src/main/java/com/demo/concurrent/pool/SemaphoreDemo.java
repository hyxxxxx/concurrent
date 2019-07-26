package com.demo.concurrent.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

/**
 * 信号量实现对象池
 */
public class SemaphoreDemo<T, R> {

    private List<T> pool;

    private Semaphore semaphore;

    public SemaphoreDemo(int size, T t) {
        pool = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            pool.add(t);
        }
        semaphore = new Semaphore(size);
    }

    public R exec(Function<T, R> func) throws InterruptedException {
        T t = null;
        try {
            semaphore.acquire();
            t = pool.remove(0);
            return func.apply(t);
        } finally {
            pool.add(t);
            semaphore.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        SemaphoreDemo<Integer, Integer> semaphore = new SemaphoreDemo<>(3, 2);

        Integer result = null;
        for (int i = 0; i < 10; i++) {
            result = semaphore.exec(j -> j + 1);
        }

        System.out.println(result);

    }
}
