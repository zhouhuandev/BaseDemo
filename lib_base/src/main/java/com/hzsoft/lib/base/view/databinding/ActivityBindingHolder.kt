package com.hzsoft.lib.base.view.databinding

import android.view.View
import android.view.ViewStub
import androidx.core.app.ComponentActivity
import androidx.databinding.ViewDataBinding
import com.hzsoft.lib.base.view.viewbinding.IViewBindingHolder

/**
 *
 *
 * @author zhouhuan
 * @time 2022/1/13
 */
interface ActivityBindingHolder<T : ViewDataBinding> : IViewBindingHolder<T> {

    fun inflateBinding(inflated: View, init: (binding: T) -> Unit)

    fun inflateBinding(activity: ComponentActivity, init: (binding: T) -> Unit)

    fun ComponentActivity.inflateBinding(
        viewStub: ViewStub? = null,
        onClear: ((binding: T) -> Unit)? = null,
        init: (binding: T) -> Unit
    )
}