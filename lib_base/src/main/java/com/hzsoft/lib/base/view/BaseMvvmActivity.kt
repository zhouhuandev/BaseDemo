package com.hzsoft.lib.base.view

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hzsoft.lib.base.mvvm.viewmodel.BaseViewModel
import com.hzsoft.lib.base.utils.ext.view.showToast
import com.hzsoft.lib.log.KLog.v

/**
 * Describe:
 * 基础 MVVM 页面
 *
 * @author zhouhuan
 * @Date 2020/12/3
 */
abstract class BaseMvvmActivity<VM : BaseViewModel> : BaseActivity() {

    protected val mViewModel: VM by lazy { ViewModelProvider(this).get(onBindViewModel()) }

    override fun initCommonView() {
        super.initCommonView()
        initBaseViewObservable()
        initViewObservable()
    }

    /**
     * 绑定 ViewModel
     */
    abstract fun onBindViewModel(): Class<VM>

    /**
     * 放置 观察者对象
     */
    abstract fun initViewObservable()

    /**
     * 初始化页面观察 变更相应的展示
     */
    protected open fun initBaseViewObservable() {
        // 将 Activty 的生命周期同步到 ViewModel 中
        lifecycle.addObserver(mViewModel)

        mViewModel.mUIChangeLiveData.getShowToastViewEvent()
            .observe(this) { it.showToast() }
        mViewModel.mUIChangeLiveData.getShowInitLoadViewEvent()
            .observe(this) {
                showInitLoadView(it)
            }
        mViewModel.mUIChangeLiveData.getShowTransLoadingViewEvent()
            .observe(this) {
                showTransLoadingView(it)
            }
        mViewModel.mUIChangeLiveData.getShowNoDataViewEvent()
            .observe(this) {
                showNoDataView(it)
            }
        mViewModel.mUIChangeLiveData.getShowNetWorkErrViewEvent()
            .observe(this) {
                showNetWorkErrView(it)
            }
        mViewModel.mUIChangeLiveData.getStartActivityEvent()
            .observe(this) {
                val clz =
                    it[BaseViewModel.ParameterField.CLASS] as Class<*>?
                val bundle = it[BaseViewModel.ParameterField.BUNDLE] as Bundle?
                startActivity(clz, bundle)
            }
        mViewModel.mUIChangeLiveData.getFinishActivityEvent()
            .observe(this) { finish() }
        mViewModel.mUIChangeLiveData.getOnBackPressedEvent()
            .observe(this) { onBackPressed() }
    }

}
