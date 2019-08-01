package com.demo.jvm;

// Run with java -XX:+PrintGC -Xmn100M -XX:PretenureSizeThreshold=10000 LifetimeTest
// You may also try with -XX:+PrintHeapAtGC，-XX:-UsePSAdaptiveSurvivorSizePolicy or -XX:SurvivorRatio=N
public class LifetimeTest {
    private static final int K = 1024;
    private static final int M = K * K;
    private static final int G = K * M;

    private static final int ALIVE_OBJECT_SIZE = 2 * M;

    public static void main(String[] args) {
        int length = ALIVE_OBJECT_SIZE / 64;
        ObjectOf64Bytes[] array = new ObjectOf64Bytes[length];
        for (long i = 0; i < G; i++) {
            array[(int) (i % length)] = new ObjectOf64Bytes();
        }
    }
}

class ObjectOf64Bytes {
    long placeholder0;
    long placeholder1;
    long placeholder2;
    long placeholder3;
    long placeholder4;
    long placeholder5;
}
