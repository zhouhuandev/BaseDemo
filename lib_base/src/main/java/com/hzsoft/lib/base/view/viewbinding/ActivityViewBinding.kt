package com.hzsoft.lib.base.view.viewbinding

import android.view.ViewStub
import androidx.core.app.ComponentActivity
import androidx.viewbinding.ViewBinding

/**
 *
 *
 * @author zhouhuan
 * @time 2022/1/13
 */
interface ActivityViewBinding<T : ViewBinding> : IViewBindingHolder<T> {

    fun ComponentActivity.inflateBinding(
        inflate: () -> T,
        isRoot: Boolean? = true,
        onClear: ((T) -> Unit)? = null,
        init: ((T) -> Unit)? = null
    ): T

    fun ComponentActivity.inflateBinding(
        bindingClass: Class<T>,
        onClear: ((T) -> Unit)? = null,
        init: ((T) -> Unit)? = null
    )

    fun ComponentActivity.inflateBinding(
        viewStub: ViewStub,
        bindingClass: Class<T>,
        onClear: ((T) -> Unit)? = null,
        init: ((T) -> Unit)? = null
    )
}