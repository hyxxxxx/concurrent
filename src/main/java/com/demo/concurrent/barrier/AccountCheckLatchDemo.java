package com.demo.concurrent.barrier;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch实现对账
 */
public class AccountCheckLatchDemo {

    public static void main(String[] args) {

        CountDownLatch latch = new CountDownLatch(2);

        Thread checkingOrderThread = new Thread(new CheckingOrderThread(latch));
        Thread uncheckOrderThread = new Thread(new UncheckOrderThread(latch));

        checkingOrderThread.start();
        uncheckOrderThread.start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        doCheck();

    }

    public static void doCheck() {
        System.out.println("开始对账...");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("对账完成");
    }

    static class CheckingOrderThread implements Runnable {

        CountDownLatch latch;

        public CheckingOrderThread(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println("正在查询派送单...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        }
    }

    static class UncheckOrderThread implements Runnable {
        CountDownLatch latch;

        public UncheckOrderThread(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println("正在查询未对账订单...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        }
    }

}
