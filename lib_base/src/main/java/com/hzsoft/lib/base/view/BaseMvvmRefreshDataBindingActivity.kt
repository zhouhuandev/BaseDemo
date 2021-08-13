package com.hzsoft.lib.base.view

import androidx.databinding.ViewDataBinding
import com.hzsoft.lib.base.mvvm.view.BaseRefreshView
import com.hzsoft.lib.base.mvvm.viewmodel.BaseRefreshViewModel
import com.refresh.lib.BaseRefreshLayout
import com.refresh.lib.DaisyRefreshLayout

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

    protected lateinit var mRefreshLayout: DaisyRefreshLayout
    protected var isRefresh = true

    override fun initCommonView() {
        super.initCommonView()
        initRefreshView()
        initBaseViewRefreshObservable()
    }

    protected abstract fun onBindRreshLayout(): Int

    protected abstract fun enableRefresh(): Boolean

    protected abstract fun enableLoadMore(): Boolean

    /**
     * 初始化刷新组件
     */
    private fun initRefreshView() {
        // 绑定组件
        mRefreshLayout = findViewById(onBindRreshLayout())
        // 是否开启刷新
        enableRefresh(enableRefresh())
        // 是否开启加载更多
        enableLoadMore(enableLoadMore())

        // 下拉刷新
        mRefreshLayout.setOnRefreshListener(object : BaseRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                isRefresh = true
                onRefreshEvent()
            }
        })
        // 上拉加载
        mRefreshLayout.setOnLoadMoreListener(object : BaseRefreshLayout.OnLoadMoreListener {
            override fun onLoadMore() {
                isRefresh = false
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
        mViewModel.mUIChangeRefreshLiveData.autoRefreshLiveEvent.observe(this) {
            autoLoadData()
        }
        mViewModel.mUIChangeRefreshLiveData.stopRefreshLiveEvent.observe(this) {
            stopRefresh()
        }
        mViewModel.mUIChangeRefreshLiveData.stopLoadMoreLiveEvent.observe(this) {
            stopLoadMore()
        }
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
