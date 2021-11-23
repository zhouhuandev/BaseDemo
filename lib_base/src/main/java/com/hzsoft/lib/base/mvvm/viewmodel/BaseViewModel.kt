package com.hzsoft.lib.base.mvvm.viewmodel

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.hzsoft.lib.base.event.SingleLiveEvent
import java.util.*
import kotlin.collections.set

/**
 * 基础 ViewModel
 * @author zhouhuan
 * @time 2020/12/2
 */
open class BaseViewModel : ViewModel(),
    IBaseViewModel {

    var mUIChangeLiveData = UIChangeLiveData()

    inner class UIChangeLiveData : SingleLiveEvent<Any>() {
        private var showToastViewEvent: SingleLiveEvent<String>? = null
        private var showInitLoadViewEvent: SingleLiveEvent<Boolean>? = null
        private var showTransLoadingViewEvent: SingleLiveEvent<Boolean>? = null
        private var showNoDataViewEvent: SingleLiveEvent<Boolean>? = null
        private var showNetWorkErrViewEvent: SingleLiveEvent<Boolean>? = null
        private var startActivityEvent: SingleLiveEvent<Map<String, Any>>? = null
        private var finishActivityEvent: SingleLiveEvent<Void>? = null
        private var onBackPressedEvent: SingleLiveEvent<Void>? = null

        fun getShowToastViewEvent(): SingleLiveEvent<String> {
            return createLiveData(showToastViewEvent).also { showToastViewEvent = it }
        }

        fun getShowInitLoadViewEvent(): SingleLiveEvent<Boolean> {
            return createLiveData(showInitLoadViewEvent).also { showInitLoadViewEvent = it }
        }

        fun getShowTransLoadingViewEvent(): SingleLiveEvent<Boolean> {
            return createLiveData(showTransLoadingViewEvent).also { showTransLoadingViewEvent = it }
        }

        fun getShowNoDataViewEvent(): SingleLiveEvent<Boolean> {
            return createLiveData(showNoDataViewEvent).also { showNoDataViewEvent = it }
        }

        fun getShowNetWorkErrViewEvent(): SingleLiveEvent<Boolean> {
            return createLiveData(showNetWorkErrViewEvent).also { showNetWorkErrViewEvent = it }
        }

        fun getStartActivityEvent(): SingleLiveEvent<Map<String, Any>> {
            return createLiveData(startActivityEvent).also { startActivityEvent = it }
        }

        fun getFinishActivityEvent(): SingleLiveEvent<Void> {
            return createLiveData(finishActivityEvent).also { finishActivityEvent = it }
        }

        fun getOnBackPressedEvent(): SingleLiveEvent<Void> {
            return createLiveData(onBackPressedEvent).also { onBackPressedEvent = it }
        }
    }

    open fun <T> createLiveData(liveData: SingleLiveEvent<T>?): SingleLiveEvent<T> {
        if (liveData == null) {
            return SingleLiveEvent()
        }
        return liveData
    }

    object ParameterField {
        var CLASS = "CLASS"
        var BUNDLE = "BUNDLE"
    }

    open fun postShowToastViewEvent(show: String) {
        mUIChangeLiveData.getShowToastViewEvent().postValue(show)
    }

    open fun postShowInitLoadViewEvent(show: Boolean) {
        mUIChangeLiveData.getShowInitLoadViewEvent().postValue(show)
    }

    open fun postShowNoDataViewEvent(show: Boolean) {
        mUIChangeLiveData.getShowNoDataViewEvent().postValue(show)
    }

    open fun postShowTransLoadingViewEvent(show: Boolean) {
        mUIChangeLiveData.getShowTransLoadingViewEvent().postValue(show)
    }

    open fun postShowNetWorkErrViewEvent(show: Boolean) {
        mUIChangeLiveData.getShowNetWorkErrViewEvent().postValue(show)
    }

    open fun postStartActivityEvent(clz: Class<*>, bundle: Bundle?) {
        val params: MutableMap<String, Any> = HashMap()
        params[ParameterField.CLASS] = clz
        if (bundle != null) {
            params[ParameterField.BUNDLE] = bundle
        }
        mUIChangeLiveData.getStartActivityEvent().postValue(params)
    }


    open fun postFinishActivityEvent() {
        mUIChangeLiveData.getFinishActivityEvent().call()
    }


    open fun postOnBackPressedEvent() {
        mUIChangeLiveData.getOnBackPressedEvent().call()
    }


    override fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {}

    override fun onCreate() {}

    override fun onStart() {}

    override fun onResume() {}

    override fun onPause() {}

    override fun onStop() {}

    override fun onDestroy() {}
}
