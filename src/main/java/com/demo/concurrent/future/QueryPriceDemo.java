package com.demo.concurrent.future;

import java.util.concurrent.*;

public class QueryPriceDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService service = Executors.newFixedThreadPool(3);

        Future<Double> p1 = service.submit(new Price1());
        Future<Double> p2 = service.submit(new Price2());
        Future<Double> p3 = service.submit(new Price3());

        System.out.println(p1.get() + " " + p2.get() + " " + p3.get());

        service.shutdown();

    }

    static class Price1 implements Callable<Double> {

        @Override
        public Double call() throws Exception {
            return 3.6D;
        }
    }

    static class Price2 implements Callable<Double> {

        @Override
        public Double call() throws Exception {
            return 4.1D;
        }
    }

    static class Price3 implements Callable<Double> {

        @Override
        public Double call() throws Exception {
            return 1.6D;
        }
    }

}
