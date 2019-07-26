package com.demo.concurrent.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

/**
 * 比读写锁更快的锁
 * 实现原理是乐观锁
 * 该锁不可重入
 * 读写不支持重入条件
 * 若要调用interrupt方法一定要用writeLockInterruptibly方法，否则会导致CPU飙升
 *
 * @param <K>
 * @param <V>
 */
public class StampedLockDemo<K, V> {

    private final Map<K, V> cache = new HashMap<>();
    private final StampedLock sl = new StampedLock();

    public V get(K key) {
        long stamp = sl.tryOptimisticRead();    //乐观读
        V v;
        v = cache.get(key);
        if (!sl.validate(stamp)) {
            stamp = sl.readLock();  //升级成悲观读锁
            try {
                v = cache.get(key);
            } finally {
                sl.unlockRead(stamp);
            }
        }
        return v;
    }

    public void put(K key, V val) {
        long stamp = sl.writeLock();
        try {
            cache.put(key, val);
        } finally {
            sl.unlockWrite(stamp);
        }
    }

}
