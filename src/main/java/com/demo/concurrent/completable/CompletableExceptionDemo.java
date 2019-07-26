package com.demo.concurrent.completable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异常处理
 */
public class CompletableExceptionDemo {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(1);

        //exceptionally == try{}catch{}
        CompletableFuture
                .runAsync(() -> {
                    throw new RuntimeException("抛个异常");
                }, executor)
                .exceptionally(throwable -> {
                    System.out.println("Catch了异常：" + throwable.getMessage());
                    return null;
                });

        //whenComplete == try{}finally{} 不支持返回值
        CompletableFuture
                .runAsync(() -> {
                    throw new RuntimeException("抛个异常");
                }, executor)
                .whenComplete((aVoid, throwable) -> System.out.println("打印异常的行号 -> " + throwable.getStackTrace()[0].getLineNumber()));


        //handle == try{}finally{} 支持返回值
        CompletableFuture<String> handle = CompletableFuture
                .runAsync(() -> {
                    throw new RuntimeException("抛个异常");
                }, executor)
                .handle((aVoid, throwable) -> "返回异常：" + throwable.getMessage());

        System.out.println(handle.join());

        executor.shutdown();
    }

}
