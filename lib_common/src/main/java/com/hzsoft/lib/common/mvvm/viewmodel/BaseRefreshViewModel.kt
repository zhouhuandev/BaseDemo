package com.hzsoft.lib.common.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.hzsoft.lib.common.event.SingleLiveEvent

/**
 *
 * @author zhouhuan
 * @time 2020/12/2
 */
abstract class BaseRefreshViewModel<T>(application: Application) : BaseViewModel(application) {
    protected var mList = MutableLiveData<MutableList<T>>()
    var orientation = MutableLiveData<Boolean>()
    var enableLoadMore = MutableLiveData<Boolean>()
    var enableRefresh = MutableLiveData<Boolean>()

    init {
        enableLoadMore.value = enableLoadMore()
        enableRefresh.value = enableRefresh()
    }

    open fun enableLoadMore(): Boolean = true

    open fun enableRefresh(): Boolean = true

    protected var mUIChangeRefreshLiveData: UIChangeRefreshLiveData? = null

    open fun getUCRefresh(): UIChangeRefreshLiveData {
        if (mUIChangeRefreshLiveData == null) {
            return UIChangeRefreshLiveData()
        }
        return mUIChangeRefreshLiveData!!
    }

    inner class UIChangeRefreshLiveData : SingleLiveEvent<Any>() {
        private var mStopRefresLiveEvent: SingleLiveEvent<Void>? = null
        private var mAutoRefresLiveEvent: SingleLiveEvent<Void>? = null
        private var mStopLoadMoreLiveEvent: SingleLiveEvent<Void>? = null

        val stopRefresLiveEvent: SingleLiveEvent<Void>
            get() = createLiveData(mStopRefresLiveEvent).also({ mStopRefresLiveEvent = it })
        val autoRefresLiveEvent: SingleLiveEvent<Void>
            get() = createLiveData(mAutoRefresLiveEvent).also({ mAutoRefresLiveEvent = it })
        val stopLoadMoreLiveEvent: SingleLiveEvent<Void>
            get() = createLiveData(mStopLoadMoreLiveEvent).also({ mStopLoadMoreLiveEvent = it })
    }

    open fun postStopRefreshEvent() {
        mUIChangeRefreshLiveData?.stopRefresLiveEvent?.call()
    }

    open fun postAutoRefreshEvent() {
        mUIChangeRefreshLiveData?.autoRefresLiveEvent?.call()
    }

    open fun postStopLoadMoreEvent() {
        mUIChangeRefreshLiveData?.stopLoadMoreLiveEvent?.call()
    }

    open fun getList(): MutableList<T>? {
        return mList.value
    }

    abstract fun refreshData()

    abstract fun loadMore()
}