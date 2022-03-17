package com.hzsoft.lib.base.view

import android.view.ViewStub
import androidx.viewbinding.ViewBinding
import com.hzsoft.lib.base.mvvm.viewmodel.BaseViewModel
import com.hzsoft.lib.base.view.viewbinding.ActivityBinding
import com.hzsoft.lib.base.view.viewbinding.ActivityViewBinding

/**
 * 基础 ViewBinding 页面
 *
 * @author zhouhuan
 * @time 2022/3/17
 */
abstract class BaseMvvmViewBindingActivity<V : ViewBinding, VM : BaseViewModel> :
    BaseMvvmActivity<VM>(), ActivityViewBinding<V> by ActivityBinding() {

    override fun initContentView(mViewStubContent: ViewStub) {
        with(mViewStubContent) {
            layoutResource = onBindLayout()
            inflate(this, onBindingClass())
        }
    }

    abstract fun onBindingClass(): Class<V>
}
