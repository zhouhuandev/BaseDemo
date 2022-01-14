package com.hzsoft.lib.common.utils.animation

import android.content.Context
import android.util.Log
import android.view.View
import android.view.animation.*
import androidx.annotation.AnimRes
import androidx.annotation.FloatRange
import com.hzsoft.lib.common.utils.func.property.ObservableProperty
import kotlin.math.max

internal interface AnimationBuilder {
    fun build(): Animation
}

abstract class ViewAnimationBuilder internal constructor() : AnimationBuilder {

    companion object {
        private const val TAG = "ViewAnimation"
    }

    var context: Context? = null

    @AnimRes
    var animRes: Int = 0

    var duration: Long = -1L
    var interpolator: Interpolator? = null

    /**
     * either [Animation.RESTART] or [Animation.REVERSE]
     */
    var repeatMode: Int = 0 // unavailable default

    /**
     * [Animation.INFINITE] or any repeat count expected
     */
    var repeatCount: Int = 0 // unavailable default

    var onAnimStartAction: ((animation: Animation?) -> Unit)? = null
    var onAnimEndAction: ((animation: Animation?) -> Unit)? = null
    var onAnimRepeatAction: ((animation: Animation?) -> Unit)? = null

    abstract val animFactory: AnimationFactory

    final override fun build(): Animation {
        val animation = loadFromRes() ?: animFactory.createAnimation()
        return animation.also { override(it) }
    }

    fun onAnimStart(onStart: (animation: Animation?) -> Unit) {
        onAnimStartAction = onStart
    }

    fun onAnimEnd(onEnd: (animation: Animation?) -> Unit) {
        onAnimEndAction = onEnd
    }

    fun onAnimRepeat(onRepeat: (animation: Animation?) -> Unit) {
        onAnimRepeatAction = onRepeat
    }

    fun factory(supplier: () -> Animation) = object : AnimationFactory {
        override fun createAnimation(): Animation = supplier()
    }

    protected open fun loadFromRes(): Animation? {
        return animRes
            .takeIf { it != 0 }
            ?.let {
                if (context == null) {
                    Log.w(TAG, "Load animation without context")
                    return@let null
                }
                AnimationUtils.loadAnimation(context, animRes)
            }
    }

    protected open fun genAnimListener(): Animation.AnimationListener? {
        if (onAnimStartAction == null && onAnimEndAction == null && onAnimRepeatAction == null) {
            return null
        }
        return object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                onAnimStartAction?.invoke(animation)
            }

            override fun onAnimationEnd(animation: Animation?) {
                onAnimEndAction?.invoke(animation)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                onAnimRepeatAction?.invoke(animation)
            }
        }
    }

    private fun override(anim: Animation) {
        anim.also {
            val shouldOverrideDuration = duration > 0 || it.duration == 0L
            if (shouldOverrideDuration) {
                it.duration = duration.takeIf { d -> d > 0 } ?: 300L
            }
            repeatMode.takeIf { mode -> mode > 0 }?.also { mode -> it.repeatMode = mode }
            it.repeatCount = max(Animation.INFINITE, repeatCount)
            interpolator?.also { interpolator -> it.interpolator = interpolator }
            genAnimListener()?.also { l -> it.setAnimationListener(l) }
        }
    }

    interface AnimationFactory {
        fun createAnimation(): Animation
    }

    class SimpleAnimationBuilder : ViewAnimationBuilder() {
        override val animFactory: AnimationFactory = factory {
            throw IllegalStateException("Must provide context and valid anim-res.")
        }
    }
}

class AlphaTranslationBuilder internal constructor() : ViewAnimationBuilder() {

    @FloatRange(from = 0.0, to = 1.0)
    var from: Float = 0F

    @FloatRange(from = 0.0, to = 1.0)
    var to: Float = 1F

    override val animFactory: AnimationFactory = factory { AlphaAnimation(from, to) }

    fun changeAlpha(
        @FloatRange(from = 0.0, to = 1.0) fromAlpha: Float,
        @FloatRange(from = 0.0, to = 1.0) toAlpha: Float
    ) {
        from = fromAlpha
        to = toAlpha
    }
}

class ScaleAnimationBuilder internal constructor() : ViewAnimationBuilder() {

    var fromX: Float = 1F
    var toX: Float = 1F
    var fromY: Float = 1F
    var toY: Float = 1F
    var pivotX: Float = Float.MIN_VALUE
    var pivotXType: Int = -1
    var pivotY: Float = Float.MIN_VALUE
    var pivotYType: Int = -1

    override val animFactory: AnimationFactory = factory {
        when {
            pivotX == Float.MIN_VALUE || pivotY == Float.MIN_VALUE -> {
                ScaleAnimation(fromX, toX, fromY, toY)
            }
            pivotXType < 0 || pivotYType < 0 -> {
                ScaleAnimation(fromX, toX, fromY, toY, pivotX, pivotY)
            }
            else -> {
                ScaleAnimation(fromX, toX, fromY, toY, pivotXType, pivotX, pivotYType, pivotY)
            }
        }
    }
}

class RotateAnimationBuilder internal constructor() : ViewAnimationBuilder() {

    /** from degrees */
    var from: Float = 0F

    /** to degrees */
    var to: Float = 0F

    var pivotX: Float = Float.MIN_VALUE
    var pivotXType: Int = -1
    var pivotY: Float = Float.MIN_VALUE
    var pivotYType: Int = -1

    override val animFactory: AnimationFactory = factory {
        when {
            pivotX == Float.MIN_VALUE || pivotY == Float.MIN_VALUE -> {
                RotateAnimation(from, to)
            }
            pivotXType < 0 || pivotYType < 0 -> {
                RotateAnimation(from, to, pivotX, pivotY)
            }
            else -> {
                RotateAnimation(from, to, pivotXType, pivotX, pivotYType, pivotY)
            }
        }
    }
}

class TranslationAnimationBuilder internal constructor() : ViewAnimationBuilder() {

    var fromX: Float = 0F
    var fromXType: Int = -1
    var toX: Float = 0F
    var toXType: Int = -1
    var fromY: Float = 0F
    var fromYType: Int = -1
    var toY: Float = 0F
    var toYType: Int = -1

    var xType: Int by ObservableProperty(-1) { fromXType = it; toXType = it }

    var yType: Int by ObservableProperty(-1) { fromYType = it; toYType = it }

    override val animFactory: AnimationFactory = factory {
        if (fromXType != -1 || toXType != -1 || fromYType != -1 || toYType != -1) {
            TranslateAnimation(
                fromXType.realType(),
                fromX,
                toXType.realType(),
                toX,
                fromYType.realType(),
                fromY,
                toYType.realType(),
                toY
            )
        } else {
            TranslateAnimation(fromX, toX, fromY, toY)
        }
    }

    private fun Int.realType(): Int = max(this, Animation.ABSOLUTE)
}

/* ========================== animation creations ========================== */

fun xmlAnimation(supplier: ViewAnimationBuilder.SimpleAnimationBuilder.() -> Unit): Animation {
    return ViewAnimationBuilder.SimpleAnimationBuilder()
        .apply(supplier)
        .build()
}

fun xmlAnimation(context: Context, @AnimRes animResId: Int): Animation {
    return xmlAnimation {
        this.context = context
        this.animRes = animResId
    }
}

/**
 * Fast create & play view animation
 * ```
 * val animation = alphaAnimation {
 *      // changeAlpha(0.3F, 1F)
 *      from = 0.3F
 *      to = 1.0F
 * } // create an AlphaAnimation
 * ```
 */
fun alphaAnimation(supplier: AlphaTranslationBuilder.() -> Unit): Animation {
    return AlphaTranslationBuilder()
        .apply(supplier)
        .build()
}

fun scaleAnimation(supplier: ScaleAnimationBuilder.() -> Unit): Animation {
    return ScaleAnimationBuilder()
        .apply(supplier)
        .build()
}

fun rotateAnimation(supplier: RotateAnimationBuilder.() -> Unit): Animation {
    return RotateAnimationBuilder()
        .apply(supplier)
        .build()
}

fun translateAnimation(supplier: TranslationAnimationBuilder.() -> Unit): Animation {
    return TranslationAnimationBuilder()
        .apply(supplier)
        .build()
}

operator fun Animation.plus(other: Animation): Animation {
    return animationSet {
        together(this@plus.elements() + other)
    }
}

private fun Animation.elements(): List<Animation> {
    return if (this is AnimationSet) this.animations else listOf(this)
}

/* ========================== View ext ========================== */

fun View.xmlAnim(@AnimRes animationId: Int): Animation {
    return xmlAnimation(context, animationId).also { startAnimation(it) }
}

/**
 * Fast create & play view animation
 * ```
 *  val animation = view.alpha {
 *      from = 0.3F
 *      to = 1.0F
 *  } // create and play animation directly
 * ```
 */
fun View.alpha(supplier: AlphaTranslationBuilder.() -> Unit): Animation {
    return alphaAnimation(supplier).also { startAnimation(it) }
}

fun View.scale(supplier: ScaleAnimationBuilder.() -> Unit): Animation {
    return scaleAnimation(supplier).also { startAnimation(it) }
}

fun View.rotate(supplier: RotateAnimationBuilder.() -> Unit): Animation {
    return rotateAnimation(supplier).also { startAnimation(it) }
}

fun View.translate(supplier: TranslationAnimationBuilder.() -> Unit): Animation {
    return translateAnimation(supplier).also { startAnimation(it) }
}
