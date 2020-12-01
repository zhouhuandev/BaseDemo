package com.hzsoft.lib.net.config

import android.content.Context
import java.lang.NullPointerException

/**
 * Describe:
 * <p>网络架构工具上下文</p>
 *
 * @author zhouhuan
 * @Date 2020/12/1
 */
object NetAppContext {
    private var mContext: Context? = null

    /**
     * 初始化工具上下文
     */
    fun init(context: Context) {
        this.mContext = context.applicationContext
    }

    fun getContext(): Context = mContext ?: throw NullPointerException("Net Not init")
}