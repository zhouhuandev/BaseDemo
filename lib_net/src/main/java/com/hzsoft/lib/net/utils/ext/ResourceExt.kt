package com.hzsoft.lib.net.utils.ext

import android.util.Log
import com.hzsoft.lib.log.KLog
import com.hzsoft.lib.net.BuildConfig
import com.hzsoft.lib.net.dto.Resource

/**
 * Describe:
 * 结果包装类拓展
 *
 * @Author zhouhuan
 * @Date 2021/7/5
 */


/**
 * 快捷拓展获取到相应的数据结果
 * @loading 加载中回调
 * @fail 失败回调
 * @success 成功回调
 */
fun <T> Resource<T>.launch(
    loading: (() -> Unit)? = null,
    fail: ((code: Int?) -> Unit)? = null,
    success: (data: T?) -> Unit
) {
    when (this) {
        is Resource.Success -> success(data)
        is Resource.Loading -> loading?.invoke()
        else -> {
            if (BuildConfig.DEBUG) {
                KLog.e(
                    "Resource", "---------> errorCode: $errorCode " + Log.getStackTraceString(
                        Throwable("Just print")
                    )
                )
            } else {
                KLog.e("Resource", "---------> errorCode: $errorCode")
            }
            fail?.let { it(errorCode) }
        }
    }
}