package com.demo.concurrent.future;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 通过Result传递参数
 * Result相当于主线程和子线程之间的桥梁
 * 实现线程间共享数据
 */
public class FutureResultDemo {

    public static void main(String[] args) {

        Result result = new Result();
        ExecutorService service = Executors.newFixedThreadPool(2);

        try {
            Future<Result> future = service.submit(new Task(result), result);
            System.out.println(future.get().getTea());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            service.shutdown();
        }

    }

    static class Task implements Runnable {
        private Result result;

        public Task(Result result) {
            this.result = result;
        }

        @Override
        public void run() {
            result.setTea("铁观音");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    static class Result {
        private String tea;

        public String getTea() {
            return tea;
        }

        public void setTea(String tea) {
            this.tea = tea;
        }
    }

}
