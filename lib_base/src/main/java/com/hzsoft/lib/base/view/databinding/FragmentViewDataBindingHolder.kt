package com.hzsoft.lib.base.view.databinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.hzsoft.lib.base.view.viewbinding.IViewBindingHolder
import com.hzsoft.lib.base.view.viewbinding.ObserverWrapper
import java.lang.IllegalArgumentException

/**
 *
 *
 * @author zhouhuan
 * @time 2022/1/13
 */
class FragmentViewDataBindingHolder<T : ViewDataBinding>(@LayoutRes private val layoutRes: Int = 0) :
    FragmentBindingHolder<T> {

    private val _bindingHolder = ViewDataBindingHolder<T>()

    override fun inflateBinding(
        inflater: LayoutInflater,
        root: ViewGroup?,
        attachToRoot: Boolean,
        block: (binding: T) -> Unit
    ): View {
        if (layoutRes == 0) throw IllegalArgumentException("layout file is empty!")
        return DataBindingUtil.inflate<T>(inflater, layoutRes, root, attachToRoot)
            .also {
                _bindingHolder.bind(it)
                block(it)
            }.root
    }

    override fun Fragment.inflateBinding(
        inflater: LayoutInflater,
        root: ViewGroup?,
        attachToRoot: Boolean,
        onClear: ((binding: T) -> Unit)?,
        init: (binding: T) -> Unit
    ): View {
        return inflateBinding(inflater, root, attachToRoot) {
            init(it)
            ObserverWrapper(viewLifecycleOwner) {
                clearBinding {
                    onClear?.invoke(this)
                }
            }.attach()
        }
    }

    override fun inflateBinding(inflated: View, init: (binding: T) -> Unit) {
        DataBindingUtil.bind<T>(inflated)
            ?.also { binding ->
                _bindingHolder.bind(binding)
                init(binding)
            }
    }

    override fun Fragment.inflateBinding(
        viewStub: ViewStub?,
        onClear: ((binding: T) -> Unit)?,
        init: (binding: T) -> Unit
    ) {
        viewStub?.also {
            it.setOnInflateListener { _, inflated ->
                inflateBinding(inflated, init)
            }
            it.inflate()
        } ?: throw IllegalArgumentException("ViewStub is not null!")
        ObserverWrapper(viewLifecycleOwner) {
            clearBinding {
                onClear?.invoke(this)
            }
        }.attach()
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
 * class MyFragment : Fragment(), FragmentBindingHolder<MyFragmentBinding> by FragmentBindingHolder(R.layout.my_fragment) {
 *
 *     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
 *         return inflate(inflater, container, false, /* option: */onClear = { it.onClear() }) { binding ->
 *             // init with binding
 *             …
 *         }
 *     }
 *
 *     // Optional: perform clear binding
 *     private fun MyFragmentBinding.onClear() {
 *         …
 *     }
 * }
 * ```
 */
@Suppress("FunctionName") // delegate FragmentBindingHolder create
inline fun <reified T : ViewDataBinding> FragmentBinding(@LayoutRes layoutRes: Int = 0): FragmentBindingHolder<T> =
    FragmentViewDataBindingHolder(layoutRes)