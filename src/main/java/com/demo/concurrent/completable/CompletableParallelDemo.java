package com.demo.concurrent.completable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CompletableParallelDemo {

    static final private ExecutorService executors = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {

        CompletableParallelDemo futureDemo = new CompletableParallelDemo();

        //任务一
        CompletableFuture<List<Integer>> orderListFuture = CompletableFuture
                .supplyAsync(futureDemo::getOrders, executors);
        //任务二
        CompletableFuture<List<Integer>> mentorListFuture = CompletableFuture
                .supplyAsync(futureDemo::getMentors, executors);
        //任务一、二完成后执行
//        CompletableFuture<List<Integer>> unionFuture = mentorListFuture
//                .thenCombine(orderListFuture, futureDemo::getUnion);

        //任务一、任务二任意完成一个后执行
        CompletableFuture<List<Integer>> applyToEither = orderListFuture
                .applyToEither(mentorListFuture, futureDemo::eachAddOne);

        //等待任务三完成
        List<Integer> list = applyToEither.join();
        list.forEach(System.out::println);

        executors.shutdown();

    }

    private List<Integer> eachAddOne(List<Integer> list) {
        return list.stream().map(l -> l + 1).collect(Collectors.toList());
    }

    private List<Integer> getUnion(List<Integer> l1, List<Integer> l2) {
        List<Integer> list = new ArrayList<>();
        for (Integer order : l1) {
            for (Integer mentor : l2) {
                if (Objects.equals(order, mentor)) {
                    list.add(order);
                }
            }
        }
        return list;
    }

    private List<Integer> getOrders() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(1, 23, 4, 5, 6);
    }

    private List<Integer> getMentors() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(23, 21, 44, 5, 1);
    }

}
