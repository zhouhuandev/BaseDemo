package com.hzsoft.lib.base

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.hzsoft.lib.log.KLog

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
        const val TAG = "BaseApplication"

        lateinit var instance: BaseApplication
            private set

        @JvmStatic
        fun getContext(): Context = instance.applicationContext
    }
}
