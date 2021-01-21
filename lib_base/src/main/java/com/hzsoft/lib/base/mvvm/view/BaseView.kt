package com.hzsoft.lib.base.mvvm.view

/**
 *
 * @author zhouhuan
 * @time 2020/12/2
 */
interface BaseView : ILoadView, INoDataView, ITransView, INetErrView {
    fun initListener()
    fun initData()
    fun finishActivity()
}
