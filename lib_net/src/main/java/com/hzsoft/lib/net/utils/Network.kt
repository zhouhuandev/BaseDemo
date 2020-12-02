package com.hzsoft.lib.net.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * 检测当前请求的网络状态信息
 * @author zhouhuan
 * @time 2020/12/2 21:08
 */
class Network constructor(val context: Context) : NetworkConnectivity {

    override fun isConnected(): Boolean {
        val info = getNetworkInfo()
        return info != null && info.isConnected
    }

    override fun getNetworkInfo(): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }
}

interface NetworkConnectivity {
    fun getNetworkInfo(): NetworkInfo?
    fun isConnected(): Boolean
}