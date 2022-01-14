package com.hzsoft.lib.base.view

import android.view.ViewStub
import androidx.databinding.ViewDataBinding
import com.hzsoft.lib.base.mvvm.viewmodel.BaseViewModel
import com.hzsoft.lib.base.view.databinding.ActivityBinding
import com.hzsoft.lib.base.view.databinding.ActivityBindingHolder

/**
 * Describe:
 * 基础 DataBinding 页面
 *
 * @author zhouhuan
 * @Date 2020/12/17
 */
abstract class BaseMvvmDataBindingActivity<V : ViewDataBinding, VM : BaseViewModel> :
    BaseMvvmActivity<VM>(), ActivityBindingHolder<V> by ActivityBinding() {

    override fun initContentView(mViewStubContent: ViewStub) {
        with(mViewStubContent) {
            layoutResource = onBindLayout()
            inflate(this) { binding ->
                onBindVariableId().forEach { pair ->
                    binding.setVariable(pair.first, pair.second)
                }
            }
        }
    }

    abstract fun onBindVariableId(): MutableList<Pair<Int, Any>>
}
