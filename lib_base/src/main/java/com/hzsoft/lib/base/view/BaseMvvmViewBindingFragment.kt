package com.hzsoft.lib.base.view

import android.view.ViewStub
import androidx.viewbinding.ViewBinding
import com.hzsoft.lib.base.mvvm.viewmodel.BaseViewModel
import com.hzsoft.lib.base.utils.ReflectUtils
import com.hzsoft.lib.base.view.viewbinding.FragmentViewBinding
import com.hzsoft.lib.base.view.viewbinding.FragmentViewBindingHolder

/**
 * 基础 ViewBinding 页面
 *
 * @author zhouhuan
 * @time 2022/3/17
 */
abstract class BaseMvvmViewBindingFragment<V : ViewBinding, VM : BaseViewModel> :
    BaseMvvmFragment<VM>(), FragmentViewBinding<V> by FragmentViewBindingHolder() {

    override fun initContentView(mViewStubContent: ViewStub) {
        with(mViewStubContent) {
            layoutResource = onBindLayout()
            inflateBinding(this, onBindingClass())
        }
    }

    override fun onBindViewModel(): Class<VM> {
        return ReflectUtils.getActualTypeArgument(1, this.javaClass) as? Class<VM>
            ?: throw IllegalArgumentException("找不到 ViewModelClass 实例，建议重写该方法")
    }

    open fun onBindingClass(): Class<V> {
        return ReflectUtils.getActualTypeArgument(0, this.javaClass) as? Class<V>
            ?: throw IllegalArgumentException("找不到 BindingClass 实例，建议重写该方法")
    }
}
