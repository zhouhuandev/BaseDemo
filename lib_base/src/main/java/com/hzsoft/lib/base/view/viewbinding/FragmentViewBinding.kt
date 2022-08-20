package com.hzsoft.lib.base.view.viewbinding

import android.view.View
import android.view.ViewStub
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 *
 *
 * @author zhouhuan
 * @time 2022/1/13
 */
interface FragmentViewBinding<T : ViewBinding> : IViewBindingHolder<T> {

    fun Fragment.inflateBinding(
        inflate: () -> T,
        onClear: ((binding: T) -> Unit)? = null,
        init: ((binding: T) -> Unit)? = null
    ): View

    fun Fragment.inflateBinding(
        viewStub: ViewStub,
        bindingClass: Class<T>,
        onClear: ((binding: T) -> Unit)? = null,
        init: ((binding: T) -> Unit)? = null
    )
}