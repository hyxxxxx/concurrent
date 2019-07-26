package com.demo.concurrent.forkjoin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 统计单词数量
 */
public class MapReduceDemo extends RecursiveTask<Map<String, Long>> {

    private String[] fc;
    private int start, end;

    public MapReduceDemo(String[] fc, int from, int to) {
        this.fc = fc;
        this.start = from;
        this.end = to;
    }

    /**
     * 通过二分法，将fc递归地拆成多个子任务
     * 直到每个子任务手上只有一个字符串
     * 否则再拆分
     * 当只有一个字符串时调用calc()方法计算单词数量
     * 待所有子任务计算完成后调用merge()方法
     * 将所有计算结果汇总
     */
    @Override
    protected Map<String, Long> compute() {

        if (end - start == 1) {
            return calc(fc[start]);
        } else {
            int mid = (start + end) / 2;

            MapReduceDemo m1 = new MapReduceDemo(fc, start, mid);
            System.out.println("m1 start " + start + " mid " + mid);

            m1.fork();  //异步执行 m1的compute任务

            MapReduceDemo m2 = new MapReduceDemo(fc, mid, end);
            System.out.println("m2 mid " + mid + " end " + end);

            //join阻塞当前线性等待m1执行完成
            Map<String, Long> merge = merge(m2.compute(), m1.join());
            System.out.println(merge);

            return merge;
        }
    }

    /**
     * 合并两个map的key value
     */
    private Map<String, Long> merge(Map<String, Long> m1, Map<String, Long> m2) {
        m2.forEach((k, v) -> m1.merge(k, v, (a, b) -> b + a));
        return m1;
    }

    /**
     * 统计一行里的单词数量
     */
    private Map<String, Long> calc(String line) {
        HashMap<String, Long> result = new HashMap<>();
        String[] words = line.split("\\s+");
        for (String word : words) {
            //对word对应的value+1
            result.merge(word, 1L, (a, b) -> a + b);
        }
        return result;
    }

    public static void main(String[] args) {

        String[] fc = {"hello world", "hello me", "hello fork", "hello join", "fork join in world"};
        ForkJoinPool pool = new ForkJoinPool(3);
        MapReduceDemo m = new MapReduceDemo(fc, 0, fc.length);
        Map<String, Long> result = pool.invoke(m);
//        result.forEach((k, v) -> System.out.println(k + " : " + v));
        System.out.println("result : " + result);

    }

}
