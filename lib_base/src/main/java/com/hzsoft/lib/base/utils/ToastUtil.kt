package com.hzsoft.lib.base.utils

import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.hzsoft.lib.base.BaseApplication


/**
 * 吐司工具类
 * @author zhouhuan
 * @time 2020/11/30 23:19
 */
object ToastUtil {

    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(BaseApplication.instance, message, duration).show()
    }

    fun showToast(resid: Int, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(
            BaseApplication.instance,
            BaseApplication.instance?.getString(resid),
            duration
        )
            .show()
    }


    fun showSnackbar(
        view: View,
        text: String,
        actionText: String? = null,
        duration: Int = Snackbar.LENGTH_SHORT,
        block: (() -> Unit)? = null
    ) {
        val snackbar = Snackbar.make(view, text, duration)
        if (actionText != null && block != null) {
            snackbar.setAction(actionText) {
                block()
            }
        }
        snackbar.show()
    }

    fun showSnackbar(
        view: View,
        resid: Int,
        actionText: String? = null,
        duration: Int = Snackbar.LENGTH_SHORT,
        block: (() -> Unit)? = null
    ) {
        val snackbar = Snackbar.make(view, resid, duration)
        if (actionText != null && block != null) {
            snackbar.setAction(actionText) {
                block()
            }
        }
        snackbar.show()
    }
}
