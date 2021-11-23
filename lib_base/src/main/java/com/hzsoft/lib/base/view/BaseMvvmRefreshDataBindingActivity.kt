package com.hzsoft.lib.base.view

import androidx.databinding.ViewDataBinding
import com.hzsoft.lib.base.mvvm.view.BaseRefreshView
import com.hzsoft.lib.base.mvvm.viewmodel.BaseRefreshViewModel
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * Describe:
 * 基于 MVVM 刷新页面
 *
 * @author zhouhuan
 * @Date 2020/12/3
 */
abstract class BaseMvvmRefreshDataBindingActivity<T, V : ViewDataBinding, VM : BaseRefreshViewModel<T>> :
    BaseMvvmDataBindingActivity<V, VM>(),
    BaseRefreshView {

    protected lateinit var mRefreshLayout: SmartRefreshLayout
    protected var isRefresh = true

    override fun initCommonView() {
        super.initCommonView()
        initRefreshView()
        initBaseViewRefreshObservable()
    }

    protected abstract fun onBindRefreshLayout(): Int

    protected abstract fun enableRefresh(): Boolean

    protected abstract fun enableLoadMore(): Boolean

    /**
     * 初始化刷新组件
     */
    private fun initRefreshView() {
        // 绑定组件
        mRefreshLayout = findViewById(onBindRefreshLayout())
        // 是否开启刷新
        enableRefresh(enableRefresh())
        // 是否开启加载更多
        enableLoadMore(enableLoadMore())

        // 下拉刷新
        mRefreshLayout.setOnRefreshListener {
            isRefresh = true
            isRefresh = true
                onRefreshEvent()
            }

        // 上拉加载
        mRefreshLayout.setOnLoadMoreListener {
            isRefresh = false
            isRefresh = false
                onLoadMoreEvent()
            }

    }

    /**
     * 初始化观察者 ViewModel 层加载完数据的回调通知当前页面事件已完成
     */
    private fun initBaseViewRefreshObservable() {
        mViewModel.mUIChangeRefreshLiveData.autoRefreshLiveEvent.observe(this) {
            autoLoadData()
        }
        mViewModel.mUIChangeRefreshLiveData.stopRefreshLiveEvent.observe(this) {
            stopRefresh(it)
        }
        mViewModel.mUIChangeRefreshLiveData.stopLoadMoreLiveEvent.observe(this) {
            stopLoadMore(it)
        }
    }

    override fun enableRefresh(b: Boolean) {
        mRefreshLayout.setEnableRefresh(b)
    }

    override fun enableLoadMore(b: Boolean) {
        mRefreshLayout.setEnableLoadMore(b)
    }

    override fun enableAutoLoadMore(b: Boolean) {
        mRefreshLayout.setEnableAutoLoadMore(b)
    }

    override fun onAutoLoadEvent() {

    }

    override fun autoLoadData() {
        mRefreshLayout.autoRefresh()
    }

    override fun stopRefresh(boolean: Boolean) {
        mRefreshLayout.finishRefresh(boolean)
    }

    override fun stopLoadMore(boolean: Boolean) {
        mRefreshLayout.finishLoadMore(boolean)
    }

}
