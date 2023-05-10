package com.hzsoft.lib.base.view

import android.view.ViewStub
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.hzsoft.lib.base.mvvm.viewmodel.BaseViewModel
import com.hzsoft.lib.base.utils.ReflectUtils
import com.hzsoft.lib.base.view.databinding.FragmentBindingHolder
import com.hzsoft.lib.base.view.databinding.FragmentViewDataBindingHolder

/**
 * Describe:
 * 基础 DataBinding 页面
 *
 * @author zhouhuan
 * @Date 2020/12/17
 */
abstract class BaseMvvmDataBindingFragment<V : ViewDataBinding, VM : BaseViewModel> :
    BaseMvvmFragment<VM>(), FragmentBindingHolder<V> by FragmentViewDataBindingHolder() {

    override fun initContentView(mViewStubContent: ViewStub) {
        with(mViewStubContent) {
            layoutResource = onBindLayout()
            inflateBinding(viewStub = this) { binding ->
                binding.lifecycleOwner = this@BaseMvvmDataBindingFragment
                onBindVariableId().forEach { pair ->
                    binding.setVariable(pair.first, pair.second)
                }
            }
        }
    }

    override fun onBindViewModel(): Class<VM> {
        return ReflectUtils.getActualTypeArgument(ViewModel::class.java, this.javaClass) as? Class<VM>
            ?: throw IllegalArgumentException("找不到 ViewModelClass 实例，建议重写该方法")
    }

    abstract fun onBindVariableId(): MutableList<Pair<Int, Any>>
}
