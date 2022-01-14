package com.hzsoft.lib.base.view.databinding

import androidx.databinding.ViewDataBinding
import com.hzsoft.lib.base.view.viewbinding.IViewBindingHolder

/**
 *
 *
 * @author zhouhuan
 * @time 2022/1/13
 */
internal class ViewDataBindingHolder<VB : ViewDataBinding> : IViewBindingHolder.Holder<VB>() {

    override fun clearBinding(clear: VB.() -> Unit) {
        super.clearBinding {
            clear()
            unbind()
        }
    }
}