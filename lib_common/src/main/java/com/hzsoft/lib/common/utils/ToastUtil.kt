package com.hzsoft.lib.common.utils

import android.widget.Toast
import com.hzsoft.lib.common.BaseApplication

/**
 * 吐司工具类
 * @author zhouhuan
 * @time 2020/11/30 23:19
 */
object ToastUtil {

    fun showToast(message: String) {
        Toast.makeText(BaseApplication.instance, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(resid: Int) {
        Toast.makeText(BaseApplication.instance, BaseApplication.instance?.getString(resid), Toast.LENGTH_SHORT)
                .show()
    }
}