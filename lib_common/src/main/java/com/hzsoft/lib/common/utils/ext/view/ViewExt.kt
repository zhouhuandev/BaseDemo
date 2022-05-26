package com.hzsoft.lib.common.utils.ext.view

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import com.hzsoft.lib.base.BaseApplication
import com.hzsoft.lib.common.utils.ext.inputMethodManager

/**
 * 显示软键盘
 */
fun View.showKeyboard() {
    BaseApplication.getContext().inputMethodManager?.showSoftInput(this, 0)
}

/**
 * 隐藏软键盘
 */
fun View.hideKeyboard() {
    BaseApplication.getContext().inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
}

/**
 * View is visible?
 * @return {@code true} visible <br> {@false} gone or invisible
 */
val View.visible: Boolean get() = visibility == View.VISIBLE

/**
 * View set visible or gone
 * @param visible {@code true} visible {@false} gone
 */
fun View.gone(visible: Boolean) {
    if (visible) toVisible() else toGone()
}

/**
 * View set visible or invisible
 * @param visible {@code true} visible {@false} invisible
 */
fun View.visible(visible: Boolean) {
    if (visible) toVisible() else toInvisible()
}

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.toInvisible() {
    this.visibility = View.INVISIBLE
}


/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun AppCompatTextView.setTextFutureExt(text: String) =
    setTextFuture(
        PrecomputedTextCompat.getTextFuture(
            text,
            TextViewCompat.getTextMetricsParams(this),
            null
        )
    )

fun AppCompatEditText.setTextFutureExt(text: String) =
    setText(
        PrecomputedTextCompat.create(text, TextViewCompat.getTextMetricsParams(this))
    )
