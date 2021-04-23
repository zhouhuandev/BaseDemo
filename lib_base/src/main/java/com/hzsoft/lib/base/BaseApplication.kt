package com.hzsoft.lib.base

import android.app.Application
import android.content.Context
import com.hzsoft.lib.base.utils.log.KLog

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
    }

    companion object {
        lateinit var instance: BaseApplication
            private set

        @JvmStatic
        fun getContext(): Context = instance.applicationContext
    }
}
