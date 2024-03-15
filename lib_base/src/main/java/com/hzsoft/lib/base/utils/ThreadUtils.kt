package com.hzsoft.lib.base.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.hzsoft.lib.base.utils.ToastUtils.showToast
import java.util.concurrent.*

/**
 * 线程和线程池的相关工具
 *
 * @author zhouhuan
 * @time 2021/5/15 18:50
 */
object ThreadUtils {

    private const val LOG_TAG = "线程工具类"
    private val M_MAIN_LOOPER_HANDLER = Handler(Looper.getMainLooper())
    private var threadPoolInstance: ScheduledThreadPoolExecutor? = null
    private val threadPool: ScheduledThreadPoolExecutor
        get() = if (threadPoolInstance == null) ScheduledThreadPoolExecutor(
            Math.max(8, Runtime.getRuntime().availableProcessors() * 2),
            threadFactory("InPoolThread-", false)
        ).also { threadPoolInstance = it } else threadPoolInstance!!

    private fun threadFactory(name: String, daemon: Boolean): ThreadFactory {
        return ThreadFactory { runnable: Runnable ->
            val result = Thread(
                {
                    try {
                        runnable.run()
                    } catch (e: Exception) {
                        showToast("线程池中的某个线程发生了问题，请查看控制台或者日志文件！。")
                    }
                }, name + System.currentTimeMillis()
            )
            result.isDaemon = daemon
            result
        }
    }

    /**
     * 从线程池中创建子线程执行异步任务
     * 在任务数超过最大值，或者线程池Shutdown时将抛出异常
     *
     * @param runnable Runnable
     */
    fun submit(runnable: Runnable?): Future<*> {
        checkThreadPool()
        return threadPool.submit(runnable)
    }

    /**
     * 从线程池中创建子线程执行异步任务
     * 带延迟时间的调度，只执行一次
     * 调度之后可通过Future.get()阻塞直至任务执行完毕
     * 参数列表详见
     * [ScheduledThreadPoolExecutor.schedule]
     */
    fun schedule(runnable: Runnable?, delay: Long, unit: TimeUnit?): Future<*> {
        checkThreadPool()
        return threadPool.schedule(runnable, delay, unit)
    }

    /**
     * 从线程池中创建子线程执行异步任务
     * 带延迟时间的调度，只执行一次
     * 调度之后可通过Future.get()阻塞直至任务执行完毕，并且可以获取执行结果
     * 参数列表详见
     * [ScheduledThreadPoolExecutor.schedule]
     */
    fun <V> schedule(callable: Callable<V>?, delay: Long, unit: TimeUnit?): Future<*> {
        checkThreadPool()
        return threadPool.schedule(callable, delay, unit)
    }

    /**
     * 从线程池中创建子线程执行异步任务
     * 带延迟时间的调度，循环执行，固定频率
     * 参数列表详见
     * [ScheduledThreadPoolExecutor.scheduleAtFixedRate]
     */
    fun scheduleAtFixedRate(
        command: Runnable?,
        initialDelay: Long,
        period: Long,
        unit: TimeUnit?
    ): Future<*> {
        checkThreadPool()
        return threadPool.scheduleAtFixedRate(command, initialDelay, period, unit)
    }

    /**
     * 从线程池中创建子线程执行异步任务
     * 带延迟时间的调度，循环执行，固定延迟
     * 参数列表详见
     * [ScheduledThreadPoolExecutor.scheduleWithFixedDelay]
     */
    fun scheduleWithFixedDelay(
        command: Runnable?,
        initialDelay: Long,
        delay: Long,
        unit: TimeUnit?
    ): Future<*> {
        checkThreadPool()
        return threadPool.scheduleWithFixedDelay(command, initialDelay, delay, unit)
    }

    /**
     * 在任务数超过最大值，或者线程池Shutdown时将抛出异常
     */
    private fun checkThreadPool() {
        if (threadPool.queue.size == threadPool.maximumPoolSize || threadPool.isShutdown) {
            Log.e(
                LOG_TAG, "线程池爆满警告，请查看是否开启了过多的耗时线程", RuntimeException("线程池爆满警告，请查看是否开启了过多的耗时线程")
            )
            //重置一下线程池，并且抛弃之前的线程池的引用（优化此处的逻辑，要在App退出时销毁线程池的！）
            threadPoolInstance = null
        }
    }

    fun releaseThreadPool() {
        threadPool.shutdown()
    }

    /**
     * 延时切换到主线程
     *
     * @param runnable Runnable
     * @param delayed  时长 Millis
     */
    fun runOnUiThread(runnable: Runnable, delayed: Long) {
        M_MAIN_LOOPER_HANDLER.postDelayed(runnable, delayed)
    }

    /**
     * 切换到主线程
     *
     * @param runnable Runnable
     */
    fun runOnUiThread(runnable: Runnable) {
        M_MAIN_LOOPER_HANDLER.post(runnable)
    }

    /**
     * 切换到主线程并尽可能立刻执行。
     *
     * @param runnable Runnable
     */
    fun runOnUiThreadImediatly(runnable: Runnable) {
        M_MAIN_LOOPER_HANDLER.postAtFrontOfQueue(runnable)
    }

    /**
     * 切换到主线程
     *
     * @param runnable Runnable
     */
    fun runOnUiThreadDelay(runnable: Runnable, delayMillis: Long) {
        M_MAIN_LOOPER_HANDLER.postDelayed(runnable, delayMillis)
    }
}