package com.hzsoft.lib.base.view.viewbinding

import android.util.Log
import android.view.View
import android.view.ViewStub
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

/**
 *
 *
 * @author zhouhuan
 * @time 2022/1/13
 */
class FragmentViewBindingHolder<T : ViewBinding> : FragmentViewBinding<T> {

    companion object {
        private const val TAG = "FragmentViewBindingHold"
    }

    private val _bindingHolder = IViewBindingHolder.Holder<T>()

    override fun Fragment.inflateBinding(
        inflate: () -> T,
        onClear: ((binding: T) -> Unit)?,
        init: ((binding: T) -> Unit)?
    ): View {
        return inflate().also {
            handleBinding(this, it, onClear, init)
        }.root
    }

    override fun Fragment.inflateBinding(
        viewStub: ViewStub,
        bindingClass: Class<T>,
        onClear: ((binding: T) -> Unit)?,
        init: ((binding: T) -> Unit)?
    ) {
        viewStub.setOnInflateListener { _, inflated ->
            try {
                bindingClass.getDeclaredMethod("bind", View::class.java)
                    .invoke(null, inflated) as? T
            } catch (ex: NoSuchMethodException) {
                Log.e(
                    TAG,
                    "Fail to inflate binding<${bindingClass::javaClass.name}> in ${this::javaClass.name}",
                    ex
                )
                null
            }?.also {
                handleBinding(this, it, onClear, init)
            }
        }
        viewStub.inflate()
    }

    override val binding: T? get() = _bindingHolder.binding

    override fun requireBinding(): T = _bindingHolder.requireBinding()

    override fun clearBinding(clear: T.() -> Unit) {
        _bindingHolder.clearBinding(clear)
    }

    private fun handleBinding(
        lifecycleOwner: LifecycleOwner,
        binding: T,
        onClear: ((T) -> Unit)?,
        init: ((T) -> Unit)?
    ) {
        _bindingHolder.bind(binding)
        init?.invoke(binding)
        ObserverWrapper(lifecycleOwner) {
            clearBinding { onClear?.invoke(this) }
        }.attach()
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