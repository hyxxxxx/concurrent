package com.demo.concurrent.barrier;

import java.util.concurrent.*;

/**
 * 使用Barrier实现类似生产 - 消费者对账
 * 当查询出一个待对账订单和一个派送单，就执行对账 doCheck 方法
 */
public class AccountCheckBarrierDemo {

    static LinkedBlockingQueue pos = new LinkedBlockingQueue(10);
    static LinkedBlockingQueue dos = new LinkedBlockingQueue(10);


    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(2);

        final CyclicBarrier barrier = new CyclicBarrier(2, AccountCheckBarrierDemo::doCheck);

        service.submit(new CheckingOrderThread(barrier));
        service.submit(new UncheckOrderThread(barrier));
        service.shutdown();

    }

    public static void doCheck() {
        //对账订单和派送单各取出一个进行对账
        int p = (int) pos.remove();
        int d = (int) dos.remove();

        doCheck(p, d);

    }

    public static void doCheck(int pos, int dos) {

        try {
            System.out.println("开始对账...");
            System.out.println(pos + " " + dos);
            TimeUnit.MILLISECONDS.sleep(500);
            System.out.println("对账完成");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    static class CheckingOrderThread implements Runnable {

        CyclicBarrier barrier;

        public CheckingOrderThread(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            int a = 1;    //派送单数量
            while (a <= 200) {
                try {
                    dos.add(a);
                    System.out.println("派送单 " + a);
                    TimeUnit.SECONDS.sleep(2);
                    a++;
                    barrier.await(1, TimeUnit.SECONDS);
                } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class UncheckOrderThread implements Runnable {

        CyclicBarrier barrier;

        public UncheckOrderThread(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            int a = 1;  //待对账订单数量
            while (a <= 190) {
                try {
                    pos.add(a);
                    System.out.println("待对账订单 " + a);
                    TimeUnit.SECONDS.sleep(3);
                    a++;
                    barrier.await(1, TimeUnit.SECONDS);
                } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
