package com.hzsoft.lib.common.mvvm.view

/**
 *
 * @author zhouhuan
 * @time 2020/12/2
 */
interface BaseRefreshView<T> {
    //刷新数据
    fun refreshData(data: List<T>)

    //加载更多
    fun loadMoreData(data: List<T>)
}