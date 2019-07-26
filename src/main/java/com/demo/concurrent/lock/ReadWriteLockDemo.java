package com.demo.concurrent.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 缓存实现
 */
public class ReadWriteLockDemo<K, V> {

    private final Map<K, V> cache = new HashMap<>();
    private final ReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock rl = rwl.readLock();
    private final Lock wl = rwl.writeLock();

    public V get(K key) {
        rl.lock();
        V v;
        try {
            v = cache.get(key);
        } finally {
            rl.unlock();
        }
        if (v != null) {
            return v;
        }
        wl.lock();
        try {
            v = cache.get(key);
            if (v == null) {
                v = getValueFromDatabase((V) "数据库缓存 V");
                put(key, v);
            }
        } finally {
            wl.unlock();
        }
        return v;
    }

    public void put(K key, V value) {
        wl.lock();
        try {
            cache.put(key, value);
        } finally {
            wl.unlock();
        }
    }

    public V getValueFromDatabase(V v) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return v;
    }

    public static void main(String[] args) {
        ReadWriteLockDemo<String, String> demo = new ReadWriteLockDemo<>();
        System.out.println(demo.get("2"));
    }

}
