package com.hzsoft.lib.base.view.viewbinding

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

/**
 *
 *
 * @author zhouhuan
 * @time 2022/1/13
 */
class ActivityViewBindingHolder<T : ViewBinding> : ActivityViewBinding<T> {

    companion object {
        private const val TAG = "ActivityViewBindingHold"
    }

    private val _bindingHolder = IViewBindingHolder.Holder<T>()

    override fun ComponentActivity.inflate(
        inflate: () -> T,
        isRoot: Boolean?,
        onClear: ((T) -> Unit)?,
        init: ((T) -> Unit)?
    ): T {
        return inflate()
            .also {
                if (isRoot == true) {
                    setContentView(it.root)
                }
                handleBinding(this, it, onClear, init)
            }
    }

    override fun ComponentActivity.inflate(
        bindingClass: Class<T>,
        onClear: ((T) -> Unit)?,
        init: ((T) -> Unit)?
    ) {
        try {
            bindingClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
                .invoke(null, layoutInflater) as? T
        } catch (ex: NoSuchMethodException) {
            Log.e(
                TAG,
                "Fail to inflate binding<${bindingClass::javaClass.name}> in ${this::javaClass.name}",
                ex
            )
            null
        }?.also {
            setContentView(it.root)
            handleBinding(this, it, onClear, init)
        }
    }

    override fun ComponentActivity.inflate(
        viewStub: ViewStub,
        bindingClass: Class<T>,
        onClear: ((T) -> Unit)?,
        init: ((T) -> Unit)?
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
 * Creates the [IViewBindingHolder] for [ComponentActivity]s.
 *
 * Example for use:
 * ```
 * class MyActivity : ComponentActivity(), ActivityViewBinding<MyActivityBinding> by ActivityBinding() {
 *
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         …
 *         // replace setContentView(), and hold binding instance
 *         inflate(
 *             inflate = { MyActivityBinding.inflate(layoutInflater) },
 *             /* option: */onClear = { it.onClear() },
 *         ) { binding ->
 *             // init with binding
 *             …
 *         }
 *         …
 *     }
 *
 *     // Optional: perform clear binding
 *     private fun MyActivityBinding.onClear() {
 *         …
 *     }
 *
 *     …
 * }
 * ```
 */
@Suppress("FunctionName") // delegate ActivityViewBindingHolder implementation
fun <T : ViewBinding> ActivityBinding(): ActivityViewBinding<T> =
    ActivityViewBindingHolder()