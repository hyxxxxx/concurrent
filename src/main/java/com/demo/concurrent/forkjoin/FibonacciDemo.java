package com.demo.concurrent.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * fork/join分治思想实现斐波拉契数列
 */
public class FibonacciDemo extends RecursiveTask<Integer> {

    private final int n;

    public FibonacciDemo(int n) {
        this.n = n;
    }

    @Override
    protected Integer compute() {
        if (n <= 1) {
            return n;
        }
        FibonacciDemo f1 = new FibonacciDemo(n - 1);
        //创建子任务
        f1.fork();
        FibonacciDemo f2 = new FibonacciDemo(n - 2);
        return f2.compute() + f1.join();
    }

    public static void main(String[] args) {

        ForkJoinPool pool = new ForkJoinPool(4);
        FibonacciDemo fibonacci = new FibonacciDemo(30);
        Integer result = pool.invoke(fibonacci);
        System.out.println(result);

    }

}
