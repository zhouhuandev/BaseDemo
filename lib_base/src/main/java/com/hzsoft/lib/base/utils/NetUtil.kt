package com.hzsoft.lib.base.utils

import android.content.Context
import android.net.ConnectivityManager

import com.hzsoft.lib.base.BaseApplication


/**
 * 网络监测工具
 * @author zhouhuan
 * @time 2020/11/30 23:14
 */
object NetUtil {

    fun checkNet(): Boolean {
        val context = BaseApplication.instance
        return isWifiConnection(context!!) || isStationConnection(context!!)
    }

    fun checkNetToast(): Boolean {
        val isNet = checkNet()
        if (!isNet) {
            ToastUtil.showToast("网络不给力哦！")
        }
        return isNet
    }

    /**
     * 是否使用基站联网
     *
     * @param context
     * @return
     */
    fun isStationConnection(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                ?: return false
        val networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        return if (networkInfo != null) {
            networkInfo.isAvailable && networkInfo.isConnected
        } else false
    }

    /**
     * 是否使用WIFI联网
     *
     * @param context
     * @return
     */
    fun isWifiConnection(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                ?: return false
        val networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return if (networkInfo != null) {
            networkInfo.isAvailable && networkInfo.isConnected
        } else false
    }

    fun isNetWorkState(context: Context): NetType {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = manager.activeNetworkInfo
        if (activeNetwork != null) {
            if (activeNetwork.isConnected) {
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                    // Logger.v(TAG, "当前WiFi连接可用 ");
                    return NetType.WIFI
                } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                    // Logger.v(TAG, "当前移动网络连接可用 ");
                    return NetType.NET_4G
                }
            } else {
                // Logger.v(TAG, "当前没有网络连接，请确保你已经打开网络 ");
                return NetType.NO_NET
            }
        } else {
            // Logger.v(TAG, "当前没有网络连接，请确保你已经打开网络 ");
            return NetType.NO_NET
        }
        return NetType.NO_NET
    }

    enum class NetType {
        WIFI, NET_4G, NO_NET
    }
}
