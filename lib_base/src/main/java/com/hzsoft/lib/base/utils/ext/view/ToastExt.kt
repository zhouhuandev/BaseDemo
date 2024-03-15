package com.hzsoft.lib.base.utils.ext.view

import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.hzsoft.lib.base.utils.ToastUtils

/**
 * Describe:
 * <p></p>
 *
 * @author zhouhuan
 * @Date 2020/12/7
 */


/**
 * 使用方式
 * "This is Toast".showToast(context,Toast.LENGTH_SHORT)
 */
fun String.showToast(duration: Int = Toast.LENGTH_SHORT) {
    ToastUtils.showToastCenter(this, duration)
}

/**
 * 使用方式
 * "This is Toast".showToast(context,Toast.LENGTH_SHORT)
 */
fun Int.showToast(duration: Int = Toast.LENGTH_SHORT) {
    ToastUtils.showToastCenter(this, duration)
}

/**
 * 使用方式
 * view.showSnackbar("This is SnackBar","Action"){
 * // 处理具体的业务
 * }
 */
fun View.showSnackbar(
    text: String,
    actionText: String? = null,
    duration: Int = Snackbar.LENGTH_SHORT,
    block: (() -> Unit)? = null
) {
    ToastUtils.showSnackbar(this, text, actionText, duration, block)
}

/**
 * 使用方式
 * view.showSnackbar(R.id.string,"Action"){
 * // 处理具体的业务
 * }
 */
fun View.showSnackbar(
    resid: Int,
    actionText: String? = null,
    duration: Int = Snackbar.LENGTH_SHORT,
    block: (() -> Unit)? = null
) {
    ToastUtils.showSnackbar(this, resid, actionText, duration, block)
}
