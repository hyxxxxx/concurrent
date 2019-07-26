package com.demo.concurrent.completion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Dubbo的Forking集群模式Demo
 * 并行查询多个服务，返回任意一个优先获取的可用服务
 */
public class ForkingDemo {

    private static final ExecutorService executors = Executors.newFixedThreadPool(3);

    private static List<Future<String>> futures = new ArrayList<>(3);

    public static void main(String[] args) {

        String address = null;
        try {
            address = getAvailableAddress();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (address != null) {
                cancelAll();
            }
            executors.shutdown();
        }
        System.out.println(address);

    }

    private static void cancelAll() {
        for (Future<String> future : futures) {
            future.cancel(true);
        }
    }

    private static String getAvailableAddress() throws InterruptedException, ExecutionException {

        ExecutorCompletionService<String> cs = new ExecutorCompletionService<>(executors);

        futures.add(cs.submit(() -> {
            TimeUnit.SECONDS.sleep(2);
            return "0.0.0.1";
        }));

        futures.add(cs.submit(() -> {
            TimeUnit.SECONDS.sleep(1);
            return "0.0.0.2";
        }));

        futures.add(cs.submit(() -> {
            TimeUnit.SECONDS.sleep(3);
            return "0.0.0.3";
        }));

        return cs.take().get();
    }

}
