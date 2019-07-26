package com.demo.concurrent.lock;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 阻塞队列的实现
 *
 * @param <T>
 */
public class BlockingQueueDemo<T> {

    private final int capacity = 3;
    private int size = 0;
    private final Queue<T> queue = new ArrayDeque<>();

    private final ReentrantLock lock = new ReentrantLock();
    //队列不满
    private final Condition notFull = lock.newCondition();
    //队列不空
    private final Condition notEmpty = lock.newCondition();

    public void push(T t) throws InterruptedException {
        lock.lock();
        try {
            while (size == capacity) {
                notFull.await();
            }
            queue.offer(t);
            size++;
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }

    }

    public T take() throws InterruptedException {

        lock.lock();
        T t;
        try {
            while (size == 0) {
                notEmpty.await();
            }
            t = queue.poll();
            size--;
            notFull.signalAll();
        } finally {
            lock.unlock();
        }
        return t;
    }

    public static void main(String[] args) throws InterruptedException {

        BlockingQueueDemo<Integer> queue = new BlockingQueueDemo<>();
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(() -> {
            try {
                while (true){
                    System.out.println(queue.take());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        service.submit(() -> {
            try {
                Random random = new Random();
                while (true) {
                    queue.push(random.nextInt(100));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        service.shutdown();

    }

}
