package com.hzsoft.lib.base.view.viewbinding

import androidx.annotation.MainThread
import androidx.annotation.RestrictTo
import androidx.viewbinding.ViewBinding

/**
 *
 *
 * @author zhouhuan
 * @time 2022/1/13
 */
interface IViewBindingHolder<T : ViewBinding> {

    val binding: T?

    @Throws(IllegalStateException::class)
    fun requireBinding(): T

    fun clearBinding(clear: T.() -> Unit = {})

    open class Holder<VB : ViewBinding> : IViewBindingHolder<VB> {

        private var _binding: VB? = null
        private var _inflated: Boolean = false
        private var _cleared: Boolean = false

        override val binding: VB?
            get() = _binding

        override fun requireBinding(): VB {
            if (!_inflated) {
                throw IllegalStateException("No binding inflated.")
            }
            if (_cleared) {
                throw IllegalStateException("Binding instance cleared.")
            }
            return _binding!!
        }

        override fun clearBinding(clear: VB.() -> Unit) {
            binding?.apply { clear() }
            _cleared = true
            _binding = null
        }

        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
        @Throws(IllegalStateException::class)
        @MainThread
        fun bind(binding: VB) {
            if (_inflated) {
                throw IllegalStateException("ViewDataBinding already inflated.")
            }
            _binding = binding
            _inflated = true
        }
    }
}