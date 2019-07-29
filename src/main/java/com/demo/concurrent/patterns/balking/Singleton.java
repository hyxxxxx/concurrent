package com.demo.concurrent.patterns.balking;

/**
 * 双重检查的单例实现
 */
public class Singleton {

    //volatile用来禁止编译优化
    private static volatile Singleton singleton;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }

}
