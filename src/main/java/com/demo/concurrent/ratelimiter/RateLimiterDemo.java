package com.demo.concurrent.ratelimiter;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RateLimiterDemo {

    public static void main(String[] args) {

        RateLimiter limiter = RateLimiter.create(2.0);
        long prev = System.nanoTime();
        ExecutorService service = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 20; i++) {
            limiter.acquire();
            service.execute(() -> {
                long cur = System.nanoTime();
                System.out.println((cur - prev) / 1000_000);
            });
        }
    }

}
