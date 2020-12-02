package com.hzsoft.basedemo

import com.hzsoft.lib.common.BaseApplication
import com.hzsoft.lib.net.config.NetConfig

/**
 * Describe:
 * App
 *
 * @author zhouhuan
 * @Date 2020/12/1
 */
class MyApp : BaseApplication() {

    companion object {
        lateinit var instance: MyApp

        fun getContext() = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initNet()
    }


    private fun initNet() {
        val config = NetConfig.Builder()
            .setBaseUrl(URL_BASE)
            .build()
        config.initContext(this)
    }

}