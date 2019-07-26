package com.demo.concurrent.future;

import java.util.concurrent.*;

/**
 * 烧水泡茶
 */
public class FutureTaskDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService service = Executors.newFixedThreadPool(2);
        FutureTask<String> task2 = new FutureTask<>(new Task2());
        FutureTask<String> task1 = new FutureTask<>(new Task1(task2));

        service.submit(task2);
        service.submit(task1);

        System.out.println("上茶 " + task1.get());

        service.shutdown();

    }

    /**
     * 任务一执行洗水壶 -> 烧开水 -> 泡茶
     */
    static class Task1 implements Callable<String> {

        FutureTask<String> task2;

        public Task1(FutureTask<String> task2) {
            this.task2 = task2;
        }

        @Override
        public String call() throws Exception {
            System.out.println("task1 洗水壶...");
            TimeUnit.SECONDS.sleep(1);

            System.out.println("task1 烧开水...");
            TimeUnit.SECONDS.sleep(3);

            String result = task2.get();
            System.out.println("task1 拿到" + result);

            return result;
        }
    }

    /**
     * 任务二执行洗茶壶 -> 洗茶杯 -> 拿茶叶
     */
    static class Task2 implements Callable<String> {

        @Override
        public String call() throws Exception {
            System.out.println("task2 洗茶壶...");
            TimeUnit.SECONDS.sleep(2);

            System.out.println("task2 洗茶杯...");
            TimeUnit.SECONDS.sleep(2);

            System.out.println("task2 拿茶叶...");
            TimeUnit.SECONDS.sleep(2);

            return "龙井";
        }
    }

}
