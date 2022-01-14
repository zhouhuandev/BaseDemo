package com.hzsoft.lib.common.utils.func.property

import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

@Suppress("FunctionName") // ignore fun-name
fun <T> NullableProperty(initialValue: T? = null, ignoreSameValue: Boolean = true, onAfterChange: (T?) -> Unit) =
    object : ObservableProperty<T?>(initialValue) {

        override fun afterChange(property: KProperty<*>, oldValue: T?, newValue: T?) {
            super.afterChange(property, oldValue, newValue)
            onAfterChange(newValue)
        }

        override fun beforeChange(property: KProperty<*>, oldValue: T?, newValue: T?): Boolean {
            if (oldValue === newValue && ignoreSameValue) {
                return false
            }
            return true
        }
    }

@Suppress("FunctionName") // ignore fun-name
fun <T> ObservableProperty(initialValue: T, ignoreSameValue: Boolean = true, get: (() -> T)? = null, onAfterChange: (T) -> Unit) =
    object : ObservableProperty<T>(initialValue) {

        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
            super.afterChange(property, oldValue, newValue)
            onAfterChange(newValue)
        }

        override fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Boolean {
            if (oldValue == newValue && ignoreSameValue) {
                return false
            }
            return true
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return get?.invoke() ?: super.getValue(thisRef, property)
        }
    }
