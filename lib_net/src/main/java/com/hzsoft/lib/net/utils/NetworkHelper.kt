package com.hzsoft.lib.net.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkInfo
import com.hzsoft.lib.net.config.NetAppContext

/**
 * 检测当前请求的网络状态信息
 * @author zhouhuan
 * @time 2020/12/2 21:08
 */
class NetworkHelper constructor(val context: Context) : NetworkConnectivity {

    companion object {
        @JvmStatic
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { NetworkHelper(NetAppContext.getContext()) }
    }

    override fun isConnected(): Boolean {
        val info = getNetworkInfo()
        return info != null && info.isConnected
    }

    override fun isOnline(): Boolean {
        val info = getNetworkInfo()
        return info?.isAvailable ?: false
    }

    override fun isWifiConn(): Boolean {
        val info = getNetworkInfo()
        return info?.type == TYPE_WIFI && info.isConnected
    }

    override fun isMobileConn(): Boolean {
        val info = getNetworkInfo()
        return info?.type == TYPE_MOBILE && info.isConnected
    }

    override fun getNetworkInfo(): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }
}

interface NetworkConnectivity {
    fun getNetworkInfo(): NetworkInfo?

    /**
     * 网络是否连接
     */
    fun isConnected(): Boolean

    /**
     * 判断是否有网络，手机或者WiFi都行
     */
    fun isOnline(): Boolean

    /**
     * 是否为Wifi连接
     */
    fun isWifiConn(): Boolean

    /**
     * 是否为手机网络连接
     * 小米三星判断有问题
     */
    fun isMobileConn(): Boolean
}