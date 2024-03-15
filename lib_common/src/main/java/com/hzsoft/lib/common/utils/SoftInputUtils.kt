package com.hzsoft.lib.common.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * 软键盘的显示与隐藏
 * * 1.显示软键盘
 * * 2.隐藏软键盘
 * @author zhouhuan
 * @time 2020/11/30 23:18
 */
object SoftInputUtils {

    /**
     * 显示软键盘
     *
     * @param context
     */
    fun showSoftInput(context: Context) {
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager // 显示软键盘
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 显示软键盘
     *
     * @param context
     */
    fun showSoftInput(context: Context, view: View) {
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager // 显示软键盘
        imm.showSoftInput(view, 0)
    }

    /**
     * 隐藏软键盘
     *
     * @param context
     * @param view
     */
    fun hideSoftInput(context: Context, view: View) {
        val immHide =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager // 隐藏软键盘
        immHide.hideSoftInputFromWindow(view.windowToken, 0)
    }
}