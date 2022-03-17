package com.hzsoft.lib.base.view

import android.view.ViewStub
import androidx.viewbinding.ViewBinding
import com.hzsoft.lib.base.mvvm.viewmodel.BaseViewModel
import com.hzsoft.lib.base.view.viewbinding.FragmentBinding
import com.hzsoft.lib.base.view.viewbinding.FragmentViewBinding

/**
 * 基础 ViewBinding 页面
 *
 * @author zhouhuan
 * @time 2022/3/17
 */
abstract class BaseMvvmViewBindingFragment<V : ViewBinding, VM : BaseViewModel> :
    BaseMvvmFragment<VM>(), FragmentViewBinding<V> by FragmentBinding() {

    override fun initContentView(mViewStubContent: ViewStub) {
        with(mViewStubContent) {
            layoutResource = onBindLayout()
            inflate(this, onBindingClass())
        }
    }

    abstract fun onBindingClass(): Class<V>
}
