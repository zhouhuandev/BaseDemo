package com.hzsoft.lib.base.mvvm.view

/**
 *
 * @author zhouhuan
 * @time 2020/12/2
 */
interface ILoadView {
    //显示初始加载的View，初始进来加载数据需要显示的View
    fun showInitLoadView()

    //隐藏初始加载的View
    fun hideInitLoadView()
}
