package com.demo.concurrent.patterns.guardedsuspension;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

/**
 * Created by Yasin H on 2019/7/28.
 */
public class GuardedObject<T> {

    T t;
    final Lock lock = new ReentrantLock();
    final Condition done = lock.newCondition();

    final int timeout = 2;

    final static Map<Object, GuardedObject> gos = new HashMap<>();

    static <K> GuardedObject create(K key) {
        GuardedObject<K> go = new GuardedObject<>();
        gos.put(key, go);
        return go;
    }

    static <K, T> void fireEvent(K key, T t) {
        GuardedObject go = gos.remove(key);
        if (go != null) {
            go.onChange(t);
        }
    }

    public T get(Predicate<T> p) {
        lock.lock();
        try {
            //MASA管程推荐写法
            while (!p.test(t)) {
                done.await(timeout, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        return t;
    }

    public void onChange(T t) {
        lock.lock();
        try {
            this.t = t;
            done.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
