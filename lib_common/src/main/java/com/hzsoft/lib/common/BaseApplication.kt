package com.hzsoft.lib.common

import android.app.Application

import com.alibaba.android.arouter.launcher.ARouter
import com.fly.tour.common.util.log.KLog

/**
 * 初始化应用程序
 * @author zhouhuan
 * @time 2020/11/30 23:04
 */
open class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        KLog.init(BuildConfig.IS_DEBUG)

        if (BuildConfig.IS_DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this) // 尽可能早，推荐在Application中初始化
    }

    companion object {
        lateinit var instance: BaseApplication
            private set
    }
}
