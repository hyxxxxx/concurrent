package com.demo.concurrent.patterns.threadlocal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Yasin H on 2019/7/28.
 */
public class SafeDateFormat {

    private static final ThreadLocal<DateFormat> tl = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public static DateFormat get() {
        return tl.get();
    }


}
