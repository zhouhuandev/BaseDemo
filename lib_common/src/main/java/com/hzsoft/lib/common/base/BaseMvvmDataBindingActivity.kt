package com.hzsoft.lib.common.base

import android.view.ViewStub
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.hzsoft.lib.common.mvvm.viewmodel.BaseViewModel

/**
 * Describe:
 * 基础 DataBinding 页面
 *
 * @author zhouhuan
 * @Date 2020/12/17
 */
abstract class BaseMvvmDataBindingActivity<V : ViewDataBinding, VM : BaseViewModel> :
    BaseMvvmActivity<VM>() {
    protected lateinit var mBinding: V
    private var viewModelId = 0

    override fun initConentView(mViewStubContent: ViewStub) {
        mViewStubContent.layoutResource = onBindLayout()
        initViewDataBinding(mViewStubContent)
        mViewStubContent.inflate()
    }

    private fun initViewDataBinding(mViewStubContent: ViewStub) {
        mViewStubContent.setOnInflateListener { _, inflated ->
            mBinding = DataBindingUtil.bind<V>(inflated)!!
            mBinding.setVariable(viewModelId, mViewModel)
        }
        viewModelId = onBindVariableId()
    }

    abstract fun onBindVariableId(): Int

    override fun onDestroy() {
        super.onDestroy()
        mBinding?.unbind()
    }
}
