package com.demo.concurrent.patterns.balking;

/**
 * balking模式
 * 单次初始化的典型应用
 */
public class InitDemo {

    boolean inited = false;

    synchronized void init() {
        if (inited) {
            return;
        }
        doInit();
        inited = true;
    }

    private void doInit() {

    }

}
