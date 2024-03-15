package com.hzsoft.lib.net.config

import android.app.Application
import android.content.Context

/**
 * Describe:
 * <p>网络架构工具上下文</p>
 *
 * @author zhouhuan
 * @Date 2020/12/1
 */
object NetAppContext {
    private var mApplication: Application? = null

    /**
     * 初始化工具上下文
     */
    fun init(application: Application) {
        this.mApplication = application
    }

    fun getContext(): Context = mApplication?.applicationContext ?: throw NullPointerException("Net Not init")
}