package com.hzsoft.lib.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.hzsoft.lib.base.utils.ProcessUtils
import com.hzsoft.lib.base.utils.ThreadUtils
import com.hzsoft.lib.base.utils.ToastUtils
import com.hzsoft.lib.log.KLog
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * 初始化应用程序
 * @author zhouhuan
 * @time 2020/11/30 23:04
 */
open class BaseApplication : Application() {

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

    override fun onCreate() {
        super.onCreate()
        if (ProcessUtils.isMainProcess == true) {
            ThreadUtils.submit { initOnlyMainProcessInLowPriorityThread() }
            initOnlyMainProcess()
        }
        ThreadUtils.submit {
            initInLowPriorityThread()
        }

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
        ToastUtils.init(this)

        if (BuildConfig.IS_DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this) // 尽可能早，推荐在Application中初始化
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
}
