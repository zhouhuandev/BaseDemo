package com.hzsoft.lib.base.view

import android.view.ViewStub
import androidx.databinding.ViewDataBinding
import com.hzsoft.lib.base.mvvm.viewmodel.BaseViewModel
import com.hzsoft.lib.base.utils.ReflectUtils
import com.hzsoft.lib.base.view.databinding.ActivityBindingHolder
import com.hzsoft.lib.base.view.databinding.ActivityViewDataBindingHolder

/**
 * Describe:
 * 基础 DataBinding 页面
 *
 * @author zhouhuan
 * @Date 2020/12/17
 */
abstract class BaseMvvmDataBindingActivity<V : ViewDataBinding, VM : BaseViewModel> :
    BaseMvvmActivity<VM>(), ActivityBindingHolder<V> by ActivityViewDataBindingHolder() {

    override fun initContentView(mViewStubContent: ViewStub) {
        with(mViewStubContent) {
            layoutResource = onBindLayout()
            inflateBinding(viewStub = this) { binding ->
                onBindVariableId().forEach { pair ->
                    binding.setVariable(pair.first, pair.second)
                }
            }
        }
    }

    override fun onBindViewModel(): Class<VM> {
        return ReflectUtils.getActualTypeArgument(1, this.javaClass) as? Class<VM>
            ?: throw IllegalArgumentException("找不到 ViewModelClass 实例，建议重写该方法")
    }

    abstract fun onBindVariableId(): MutableList<Pair<Int, Any>>
}
