package com.demo.concurrent.pool;

import java.util.concurrent.*;

public class ThreadPoolDemo {

    public void newFixedThreadPool() throws InterruptedException {

        int corePoolSize = 2;
        int maximumPoolSize = 10;
        long keepAliveTime = 10;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(10);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardOldestPolicy();

        workQueue.take();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize,
                keepAliveTime, timeUnit,
                workQueue,
                threadFactory,
                handler);

    }

}
