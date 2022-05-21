package com.hzsoft.lib.base.view.viewbinding

import androidx.annotation.RestrictTo
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Lifecycle observer wrapper
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class ObserverWrapper(private val owner: LifecycleOwner, private var onClear: (() -> Unit)?) : LifecycleEventObserver {

    private var attached: Boolean = false

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (source == owner && event == Lifecycle.Event.ON_DESTROY) {
            detach()
        }
    }

    fun attach() {
        if (attached) {
            return
        }
        if (owner.lifecycle.currentState <= Lifecycle.State.STARTED) {
            owner.lifecycle.addObserver(this)
            attached = true
        }
    }

    private fun detach() {
        owner.lifecycle.removeObserver(this)
        attached = false
        onClear?.invoke()
        onClear = null
    }
}
