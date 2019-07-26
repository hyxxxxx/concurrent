package com.demo.concurrent.barrier;

import java.util.concurrent.TimeUnit;

/**
 * 使用管程实现对账
 */
public class AccountCheckMonitorDemo {

    public static void main(String[] args) {

        Thread uncheckOrderThread = new Thread(new UncheckOrderThread(), "CheckingOrderThread");
        Thread checkingOrderThread = new Thread(new CheckingOrderThread(), "checkingOrderThread");

        uncheckOrderThread.start();
        checkingOrderThread.start();

        try {
            uncheckOrderThread.join();
            checkingOrderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        doCheck();

    }

    static class CheckingOrderThread implements Runnable {

        @Override
        public void run() {
            System.out.println("正在查询派送单...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class UncheckOrderThread implements Runnable {
        @Override
        public void run() {
            System.out.println("正在查询未对账订单...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

}
