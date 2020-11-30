package com.hzsoft.lib.common.utils

import android.view.animation.Animation
import android.view.animation.TranslateAnimation

/**
 * 转场动画
 * @author zhouhuan
 * @time 2020/11/30 23:08
 */
object AnimationUtils {
    /**
     * 从控件所在位置移动到控件的底部
     *
     * @return
     */
    fun moveToBottom(): TranslateAnimation {

        val hiddenAction = TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f)
        hiddenAction.duration = 300
        return hiddenAction
    }

    fun moveToLocation(): TranslateAnimation {
        val visibleAction = TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f)
        visibleAction.duration = 300
        return visibleAction

    }
}
