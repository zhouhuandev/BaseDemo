package com.hzsoft.lib.base.module

import com.hzsoft.lib.base.BaseApplication
import com.hzsoft.lib.net.config.NetConfig
import com.hzsoft.lib.net.config.URL_EDITH
import com.hzsoft.lib.net.config.URL_MAIN
import com.hzsoft.lib.net.config.domainConfigsInit

/**
 * 初始化应用程序
 * @author zhouhuan
 * @time 2020/11/30 23:04
 */
open class ModuleApplication : BaseApplication() {

    override fun initOnlyMainProcessInLowPriorityThread() {
        super.initOnlyMainProcessInLowPriorityThread()
        initNet()
    }

    private fun initNet() {
        domainConfigsInit(this) {
            addConfig(URL_MAIN, NetConfig.Builder().apply {
                setDefaultTimeout(5_000)
                setHeads(hashMapOf("test" to "localhost_101"))
            })
            addConfig(URL_EDITH, NetConfig.Builder().apply {
                setDefaultTimeout(6_000)
                setHeads(hashMapOf("test" to "localhost_102"))
            })
        }
    }
}
