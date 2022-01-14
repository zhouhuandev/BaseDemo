package com.hzsoft.lib.base.view.viewbinding

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 *
 *
 * @author zhouhuan
 * @time 2022/1/13
 */
class FragmentViewBindingHolder<T : ViewBinding> : FragmentViewBinding<T> {

    private val _bindingHolder = IViewBindingHolder.Holder<T>()

    override fun Fragment.inflate(
        inflate: () -> T,
        onClear: ((binding: T) -> Unit)?,
        init: ((binding: T) -> Unit)?
    ): View {
        return inflate().also {
            _bindingHolder.bind(it)
            init?.invoke(it)
            ObserverWrapper(viewLifecycleOwner) {
                clearBinding { onClear?.invoke(this) }
            }.attach()
        }.root
    }

    override val binding: T? get() = _bindingHolder.binding

    override fun requireBinding(): T = _bindingHolder.requireBinding()

    override fun clearBinding(clear: T.() -> Unit) {
        _bindingHolder.clearBinding(clear)
    }
}

/**
 * Creates the [IViewBindingHolder] for [Fragment]s.
 *
 * Example for use:
 * ```
 * class MyFragment : Fragment(), FragmentViewBinding<MyFragmentBinding> by FragmentBinding() {
 *
 *     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
 *         inflate(
 *             inflate = { MyFragmentBinding.inflate(inflater, container, false) },
 *             /* option: */onClear = { it.onClear() },
 *         ) {
 *             // init binding, views and states here
 *         }
 *
 *     // Optional: perform clear binding
 *     private fun MyFragmentBinding.onClear() {
 *         …
 *     }
 *
 *     …
 * }
 * ```
 */
@Suppress("FunctionName") // delegate FragmentViewBindingHolder implementation
inline fun <reified T : ViewBinding> FragmentBinding(): FragmentViewBinding<T> =
    FragmentViewBindingHolder()