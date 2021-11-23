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
            .observe(this, Observer { it.showToast() })
        mViewModel.mUIChangeLiveData.getShowInitLoadViewEvent()
            .observe(this, Observer {
                showInitLoadView(it)
            })
        mViewModel.mUIChangeLiveData.getShowTransLoadingViewEvent()
            .observe(this, Observer {
                v("MYTAG", "view postShowTransLoadingViewEvent start...")
                showTransLoadingView(it)
            })
        mViewModel.mUIChangeLiveData.getShowNoDataViewEvent()
            .observe(this, Observer {
                showNoDataView(it)
            })
        mViewModel.mUIChangeLiveData.getShowNetWorkErrViewEvent()
            .observe(this, Observer {
                showNetWorkErrView(it)
            })
        mViewModel.mUIChangeLiveData.getStartActivityEvent().observe(
            this,
            Observer {
                val clz =
                    it[BaseViewModel.ParameterField.CLASS] as Class<*>?
                val bundle = it[BaseViewModel.ParameterField.BUNDLE] as Bundle?
                startActivity(clz, bundle)
            })
        mViewModel.mUIChangeLiveData.getFinishActivityEvent()
            .observe(this, Observer { finish() })
        mViewModel.mUIChangeLiveData.getOnBackPressedEvent()
            .observe(this, Observer { onBackPressed() })
    }

}
