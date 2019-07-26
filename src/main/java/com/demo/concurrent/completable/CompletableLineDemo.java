package com.demo.concurrent.completable;

import java.util.concurrent.*;

/**
 * CompletableFuture串行Demo
 */
public class CompletableLineDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(1);

        long start = System.currentTimeMillis();
        CompletableFuture<Void> future = CompletableFuture
                .supplyAsync(() -> "supplyAsync", executor)
                //可接收并可返回值
                .thenApply(s -> {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return s + " -> thenApply";

                })
                //可接收值
                .thenAccept(s -> {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    s += " -> thenAccept";
                    System.out.println(s.toUpperCase());

                })
                //无参数且无返回值
                .thenRun(() -> System.out.println("Run over"));

        //需要显示捕获异常，中断线程同样会抛出异常
        System.out.println(future.get());
        //只有运行时异常
        System.out.println(future.join());
        System.out.println("执行耗时：" + (System.currentTimeMillis() - start));

        executor.shutdown();

    }

}
