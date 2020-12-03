package com.hzsoft.lib.common.mvvm.view

/**
 *
 * @author zhouhuan
 * @time 2020/12/2
 */
interface BaseRefreshView {

    /**
     * 是否启用下拉刷新
     * @param b
     */
    fun enableRefresh(b: Boolean)

    /**
     * 是否启用上拉加载更多
     */
    fun enableLoadMore(b: Boolean)

    /**
     * 刷新回调
     * 向 ViewModel 发送刷新请求
     */
    fun onRefreshEvent()

    /**
     * 加载更多的回调
     * 向 ViewModel 发送加载更多请求
     */
    fun onLoadMoreEvent()

    /**
     * 自动加载的事件
     * 向 ViewModel 发送自动加载的请求
     */
    fun onAutoLoadEvent()

    /**
     * 停止刷新
     */
    fun stopRefresh()

    /**
     * 停止加载更多
     */
    fun stopLoadMore()

    /**
     * 自动加载数据
     */
    fun autoLoadData()
}