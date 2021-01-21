package com.hzsoft.lib.common

import com.hzsoft.lib.base.BaseApplication
import com.hzsoft.lib.net.config.NetConfig

/**
 * Describe:
 *
 *
 * @author zhouhuan
 * @Date 2021/1/21
 */
open class CommonApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        initNet()
    }

    private fun initNet() {
        val config = NetConfig.Builder()
            .setBaseUrl(URL_BASE)
            .build()
        config.initContext(this)
    }
}
