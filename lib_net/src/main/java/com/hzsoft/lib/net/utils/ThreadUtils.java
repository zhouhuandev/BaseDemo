package com.hzsoft.lib.net.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * 线程和线程池的相关工具
 *
 * @author zhouhuan
 * @time 2021/5/15 18:50
 */
public class ThreadUtils {
    private static final String LOG_TAG = "线程工具类";
    private static final Handler M_MAIN_LOOPER_HANDLER = new Handler(Looper.getMainLooper());

    /**
     * 延时切换到主线程
     *
     * @param runnable Runnable
     * @param delayed  时长 Millis
     */
    public static void runOnUiThread(Runnable runnable, long delayed) {
        M_MAIN_LOOPER_HANDLER.postDelayed(runnable, delayed);
    }

    /**
     * 切换到主线程
     *
     * @param runnable Runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        M_MAIN_LOOPER_HANDLER.post(runnable);
    }

    /**
     * 切换到主线程并尽可能立刻执行。
     *
     * @param runnable Runnable
     */
    public static void runOnUiThreadImediatly(Runnable runnable) {
        M_MAIN_LOOPER_HANDLER.postAtFrontOfQueue(runnable);
    }

    /**
     * 切换到主线程
     *
     * @param runnable Runnable
     */
    public static void runOnUiThreadDelay(Runnable runnable, long delayMillis) {
        M_MAIN_LOOPER_HANDLER.postDelayed(runnable, delayMillis);
    }
}
