package com.hzsoft.lib.base.manager

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hzsoft.lib.base.BaseApplication
import com.hzsoft.lib.log.KLog
import java.util.*
import kotlin.system.exitProcess

/**
 * App页面管理器
 * @author zhouhuan
 * @time 2021/6/20 19:35
 */
open class ActivityManager : ActivityLifecycleCallbacks {

    val isActivityStackEmpty: Boolean
        get() = activityStack.empty()

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity? {
        return try {
            activityStack.lastElement()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 获取当前Activity的前一个Activity
     */
    fun preActivity(): Activity? {
        val index = activityStack.size - 2
        return if (index < 0) {
            null
        } else activityStack[index]
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        val activity = activityStack.lastElement()
        finishActivity(activity)
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        activityStack.remove(activity)
        activity?.finish()
    }

    /**
     * 移除指定的Activity
     */
    fun removeActivity(activity: Activity?) {
        activityStack.remove(activity)
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        try {
            for (activity in activityStack) {
                if (activity.javaClass == cls) {
                    finishActivity(activity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 结束除当前的页面的其他页面
     */
    fun finishOtherActivity() {
        val current: Activity? = currentActivity()
        val tempActivityStack: Stack<Activity> = Stack()
        tempActivityStack.addAll(activityStack)

        for (activity in tempActivityStack) {
            if (current !== activity) {
                activity.finish()
            }
        }
        tempActivityStack.clear()
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        val tempActivityStack: Stack<Activity> = Stack()
        tempActivityStack.addAll(activityStack)

        for (activity in tempActivityStack) {
            activity.finish()
        }
        tempActivityStack.clear()
        activityStack.clear()
    }

    /**
     * 返回到指定的activity
     *
     * @param cls
     */
    fun returnToActivity(cls: Class<*>) {
        while (activityStack.size != 0)
            if (activityStack.peek().javaClass == cls) {
                break
            } else {
                finishActivity(activityStack.peek())
            }
    }


    /**
     * 是否已经打开指定的activity
     * @param cls
     * @return
     */
    fun isOpenActivity(cls: Class<*>): Boolean {
        if (activityStack != null) {
            var i = 0
            val size = activityStack.size
            while (i < size) {
                if (cls == activityStack.peek().javaClass) {
                    return true
                }
                i++
            }
        }
        return false
    }

    /**
     * 退出应用程序
     *
     * @param context      上下文
     * @param isBackground 是否开开启后台运行
     */
    fun appExit(context: Context, isBackground: Boolean) {
        try {
            finishAllActivity()
            val activityMgr =
                context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
            activityMgr.restartPackage(context.packageName)
        } catch (e: Exception) {

        } finally {
            // 注意，如果您有后台程序运行，请不要支持此句子
            if ((!isBackground)) {
                exitProcess(0)
            }
        }
    }

    /**
     * 从后台回到前台需要执行的逻辑
     *
     * @param activity
     */
    fun back2App(activity: Activity) {
        isRunInBackground = false
        KLog.d(
            TAG,
            "当前栈剩余：${activityStack.size} 已经重新回到App。"
        )
    }

    /**
     * 离开应用 压入后台或者退出应用
     *
     * @param activity
     */
    open fun leaveApp(activity: Activity) {
        isRunInBackground = true
        KLog.d(
            TAG,
            "当前栈剩余：${activityStack.size} 正在离开App。"
        )
    }

    companion object {
        private lateinit var activityStack: Stack<Activity>

        @JvmStatic
        val instance: ActivityManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            activityStack = Stack()
            ActivityManager()
        }

        const val TAG = "ActivityManager"
        const val ACTIVITY_LUNCHER = "com.hzsoft.arrow.ui.splash"
        private var activityCount = 0
        private var isRunInBackground = false
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        //检测异常重启
        if (isActivityStackEmpty) {
            val packageName: String = BaseApplication.getContext().packageName
            val launchIntent: Intent? =
                BaseApplication.getContext().packageManager.getLaunchIntentForPackage(packageName)
            if (launchIntent != null
                && launchIntent.component != null
                && !activity.javaClass.name.equals(
                    launchIntent.component?.className,
                    ignoreCase = true
                )
            ) {
                KLog.i(
                    "onActivityCreated",
                    activity.javaClass.name + "====" + launchIntent.component?.className
                )
                if (!activity.javaClass.name.contains(ACTIVITY_LUNCHER)) {
                    //说明当前是异常启动。
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    //重新打开App
                    activity.startActivity(launchIntent)
                    //关闭当前页面。
                    activity.finish()
                }
            }
        }
        addActivity(activity)
        KLog.d(
            TAG,
            "当前栈剩余：${activityStack.size} 页面 onCreated：${activity.javaClass.name}"
        )
    }

    override fun onActivityStarted(activity: Activity) {
        KLog.d(
            TAG,
            "当前栈剩余：${activityStack.size} 页面 onStarted：${activity.javaClass.name}"
        )
        activityCount++
        if (isRunInBackground) {
            //应用从后台回到前台 需要做的操作
            back2App(activity)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        KLog.d(
            TAG,
            "当前栈剩余：${activityStack.size} 页面 onResumed：${activity.javaClass.name}"
        )
    }

    override fun onActivityPaused(activity: Activity) {
        KLog.d(
            TAG,
            "当前栈剩余：${activityStack.size} 页面 onPaused：${activity.javaClass.name}"
        )
    }

    override fun onActivityStopped(activity: Activity) {
        KLog.d(
            TAG,
            "当前栈剩余：${activityStack.size} 页面 onStopped：${activity.javaClass.name}"
        )
        activityCount--
        if (activityCount == 0) {
            leaveApp(activity)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        removeActivity(activity)
        KLog.d(
            TAG,
            "当前栈剩余：${activityStack.size} 页面 onDestroyed：${activity.javaClass.name}"
        )
    }
}
