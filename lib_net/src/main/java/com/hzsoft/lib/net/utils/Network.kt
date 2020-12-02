package com.hzsoft.lib.net.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import javax.inject.Inject

/**
 * Created by AhmedEltaher
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