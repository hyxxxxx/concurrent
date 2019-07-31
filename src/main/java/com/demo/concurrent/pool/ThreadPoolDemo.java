package com.demo.concurrent.pool;

import java.util.concurrent.*;

public class ThreadPoolDemo {

    public static ThreadPoolExecutor newFixedThreadPool() throws InterruptedException {

        int corePoolSize = 1;
        int maximumPoolSize = 1;
        long keepAliveTime = 1;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(2);
        ThreadFactory threadFactory = r -> new Thread(r, "thread_name" + r.hashCode());   //根据业务命名线程
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize,
                keepAliveTime, timeUnit,
                workQueue,
                threadFactory,
                handler);
        return executor;

    }

    public static void main(String[] args) throws InterruptedException {

        ThreadPoolExecutor executor = newFixedThreadPool();
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " running...");
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
    }


}
