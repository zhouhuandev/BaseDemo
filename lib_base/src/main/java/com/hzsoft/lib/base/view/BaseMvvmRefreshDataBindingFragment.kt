package com.hzsoft.lib.base.view

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.hzsoft.lib.base.mvvm.view.BaseRefreshView
import com.hzsoft.lib.base.mvvm.viewmodel.BaseRefreshViewModel
import com.refresh.lib.BaseRefreshLayout
import com.refresh.lib.DaisyRefreshLayout

/**
 * Describe:
 * 基于 MVVM 刷新页面
 *
 * @author zhouhuan
 * @Date 2020/12/17
 */
abstract class BaseMvvmRefreshDataBindingFragment<T, V : ViewDataBinding, VM : BaseRefreshViewModel<T>> :
    BaseMvvmDataBindingFragment<V, VM>(),
    BaseRefreshView {

    protected lateinit var mRefreshLayout: DaisyRefreshLayout

    override fun initCommonView(view: View) {
        super.initCommonView(view)
        initRefreshView(view)
        initBaseViewRefreshObservable()
    }

    protected abstract fun onBindRreshLayout(): Int

    protected abstract fun enableRefresh(): Boolean

    protected abstract fun enableLoadMore(): Boolean


    /**
     * 初始化刷新组件
     */
    private fun initRefreshView(view: View) {
        // 绑定组件
        mRefreshLayout = view.findViewById(onBindRreshLayout())
        // 是否开启刷新
        enableRefresh(enableRefresh())
        // 是否开启加载更多
        enableLoadMore(enableLoadMore())

        // 下拉刷新
        mRefreshLayout.setOnRefreshListener(object : BaseRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                onRefreshEvent()
            }
        })
        // 上拉加载
        mRefreshLayout.setOnLoadMoreListener(object : BaseRefreshLayout.OnLoadMoreListener {
            override fun onLoadMore() {
                onLoadMoreEvent()
            }
        })
        // 自动加载
        mRefreshLayout.setOnAutoLoadListener(object : BaseRefreshLayout.OnAutoLoadListener {
            override fun onAutoLoad() {
                onAutoLoadEvent()
            }
        })
    }

    /**
     * 初始化观察者 ViewModel 层加载完数据的回调通知当前页面事件已完成
     */
    private fun initBaseViewRefreshObservable() {
        mViewModel.mUIChangeRefreshLiveData.autoRefresLiveEvent.observe(
            this,
            Observer { autoLoadData() })
        mViewModel.mUIChangeRefreshLiveData.stopRefresLiveEvent.observe(
            this,
            Observer { stopRefresh() })
        mViewModel.mUIChangeRefreshLiveData.stopLoadMoreLiveEvent.observe(
            this,
            Observer { stopLoadMore() })
    }


    override fun enableRefresh(b: Boolean) {
        mRefreshLayout.setEnableRefresh(b)
    }

    override fun enableLoadMore(b: Boolean) {
        mRefreshLayout.setEnableLoadMore(b)
    }

    override fun autoLoadData() {
        mRefreshLayout.autoRefresh()
    }

    override fun stopRefresh() {
        mRefreshLayout.isRefreshing = false
    }

    override fun stopLoadMore() {
        mRefreshLayout.setLoadMore(false)
    }

}
