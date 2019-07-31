package com.demo.concurrent.ratelimiter;

import java.util.concurrent.TimeUnit;

/**
 * 基于令牌桶算法的限流器
 */
public class SimpleLimiter {

    //当前令牌桶中的数量
    private long existPermits = 0;
    //令牌桶总量
    private long capacity;
    //下一个令牌产生时间
    private long next = System.nanoTime();
    //令牌发放间隔  纳秒
    private long interval;

    private TimeUnit timeUnit;

    public SimpleLimiter(long capacity, TimeUnit timeUnit, long interval) {
        this.capacity = capacity;
        this.interval = interval;
        this.timeUnit = timeUnit;
    }

    /**
     * 返回能够获取令牌的时间
     */
    private synchronized long reserve(long now) {
        reSync(now);
        //能够获取令牌的时间
        long at = next;
        //令牌桶中能提供的令牌
        long fb = Math.min(1, existPermits);
        //令牌净需求：首先减掉令牌桶中的令牌
        long nr = 1 - fb;
        //重新计算下一令牌产生时间
        next = next + nr * interval;
        //重新计算令牌桶中的令牌数
        this.existPermits -= fb;
        return at;
    }

    /**
     * 请求时间在下一令牌产生时间之后
     * 1.重新计算令牌桶中的令牌数
     * 2.将下一个令牌发放时间重置为当前时间
     */
    private void reSync(long now) {
        if (now > next) {
            //新产生的令牌数
            long newPermit = (now - next) / interval;
            //新令牌增加到令牌桶
            existPermits = Math.min(capacity, existPermits + newPermit);
            //将下一个令牌发放时间重置为当前时间
            next = now;
        }
    }

    public void acquire() {
        //申请令牌时的时间
        long now = System.nanoTime();
        //预占令牌
        long at = reserve(now);
        long waitTime = Math.max(at - now, 0);
        //按照条件等待
        if (waitTime > 0) {
            try {
                timeUnit.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        SimpleLimiter limiter = new SimpleLimiter(10, TimeUnit.NANOSECONDS, 1000_000_000);
        TimeUnit.SECONDS.sleep(5);
        for (int i = 0; i < 10; i++) {
            limiter.acquire();
            System.out.println(i);
        }

    }

}
