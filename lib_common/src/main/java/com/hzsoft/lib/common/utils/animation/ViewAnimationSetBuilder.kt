package com.hzsoft.lib.common.utils.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet

class ViewAnimationSetBuilder internal constructor() : ViewAnimationBuilder() {

    var shareInterpolator: Boolean = true

    private val _list = mutableListOf<Animation>()

    override val animFactory: AnimationFactory = factory {
        AnimationSet(shareInterpolator).apply {
            _list.takeIf { it.isNotEmpty() }
                ?.forEach { el ->
                    addAnimation(el)
                }
        }
    }

    fun add(animation: Animation) {
        _list.add(animation)
    }

    fun together(vararg animations: Animation) {
        animations.forEach { el -> _list.add(el) }
    }

    fun together(animationList: List<Animation>) {
        _list.addAll(animationList)
    }
}

fun animationSet(init: ViewAnimationSetBuilder.() -> Unit): AnimationSet {
    return ViewAnimationSetBuilder()
        .apply(init)
        .build() as AnimationSet
}

fun View.animations(builder: ViewAnimationSetBuilder.() -> Unit): AnimationSet {
    return animationSet(builder).also { startAnimation(it) }
}
