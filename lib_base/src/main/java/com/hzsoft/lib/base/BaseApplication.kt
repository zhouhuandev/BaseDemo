package com.hzsoft.lib.base

import android.app.Application
import android.content.Context
import com.hzsoft.lib.log.KLog
import android.content.Context
import android.os.Process
import android.text.TextUtils
import androidx.multidex.MultiDex
import com.hzsoft.lib.base.utils.ThreadUtils
import com.hzsoft.lib.base.utils.ToastUtil
import com.hzsoft.lib.log.KLog
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.io.BufferedReader
import java.io.FileReader

/**
 * 初始化应用程序
 * @author zhouhuan
 * @time 2020/11/30 23:04
 */
open class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        if (isMainProcess()) {
            ThreadUtils.submit { initOnlyMainProcessInLowPriorityThread() }
            initOnlyMainProcess()
        }
        ThreadUtils.submit {
            initInLowPriorityThread()
        }

    }

    companion object {
        const val TAG = "BaseApplication"

        lateinit var instance: BaseApplication
            private set

        @JvmStatic
        fun getContext(): Context = instance.applicationContext
    }

    override fun attachBaseContext(base: Context?) {
        instance = this
        MultiDex.install(this)
        super.attachBaseContext(base)
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    override fun onTrimMemory(level: Int) {
        // EventBus.getDefault().post(LowMemeryEvent(level))
        super.onTrimMemory(level)
    }


    /**
     * 主线程中初始化内容
     */
    protected open fun initOnlyMainProcess() {
        KLog.init(BuildConfig.IS_DEBUG)
        ToastUtil.init(this)
    }

    /**
     * 在主进程，但是优先级较低的子线程初始化
     */
    protected open fun initOnlyMainProcessInLowPriorityThread() {}

    /**
     * 在任何进程并且优先级较低的子线程初始化
     */
    protected open fun initInLowPriorityThread() {
        initSmartRefreshLayout()
    }

    open fun isMainProcess(): Boolean {
        // 获取当前进程名
        val processName = getProcessName(Process.myPid())
        return processName == null || processName.isEmpty() || processName == applicationContext.packageName
    }

    open fun initSmartRefreshLayout() {
        //全局设置默认的 Header
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.white, R.color.color_666) //全局设置主题颜色
            ClassicsHeader(
                context
            )
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
                .setDrawableSize(20F)
        }
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    open fun getProcessName(pid: Int): String? {
        try {
            BufferedReader(FileReader("/proc/$pid/cmdline")).use { reader ->
                var processName = reader.readLine()
                if (!TextUtils.isEmpty(processName)) {
                    processName = processName.trim { it <= ' ' }
                }
                return processName
            }
        } catch (e: Exception) {
            KLog.w(TAG, "获取当前进程名称", e)
        }
        return null
    }
}
