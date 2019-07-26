package com.demo.concurrent.completion;

import java.util.concurrent.*;

/**
 * 分别查询3种价格，先查询到结果的线程先执行下一步操作
 */
public class CompletionServiceDemo {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        CompletionService<Double> completionService =
                new ExecutorCompletionService<>(executor, new LinkedBlockingQueue<>());

        completionService.submit(CompletionServiceDemo::getPrice1);
        completionService.submit(CompletionServiceDemo::getPrice2);
        completionService.submit(CompletionServiceDemo::getPrice3);

        //非阻塞
//        completionService.poll();

        try {
            for (int i = 0; i < 3; i++) {
                Future<Double> future = completionService.take();
                System.out.println(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

    }

    private static Double getPrice1() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        return 2.3;
    }

    private static Double getPrice2() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        return 5.5;
    }

    private static Double getPrice3() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        return 2.67;
    }

}
