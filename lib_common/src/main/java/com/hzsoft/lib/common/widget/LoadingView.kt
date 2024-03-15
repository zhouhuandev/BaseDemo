package com.hzsoft.lib.common.widget

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.hzsoft.lib.common.utils.DisplayUtils.dip2px
import kotlin.jvm.JvmOverloads

/**
 * 加载View
 *
 * @author zhouhuan
 * @time 2021/9/15
 */
class LoadingView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var radius = 0f
    private var radiusOffset = 0f

    // 不是固定不变的，当width为30dp时，它为2dp，当宽度变大，这个也会相应的变大
    private var stokeWidth = 2f
    private val argbEvaluator = ArgbEvaluator()
    private val startColor = Color.parseColor("#EEEEEE")
    private val endColor = Color.parseColor("#111111")
    var lineCount = 10 // 线的数量
    var avgAngle = 360f / lineCount
    // 重复次数
    var time = 0
    var centerX = 0f
    // 中心x，y
    var centerY = 0f
    var startX = 0f
    var endX = 0f


    init {
        stokeWidth = dip2px(stokeWidth).toFloat()
        paint.strokeWidth = stokeWidth
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = (measuredWidth / 2).toFloat()
        radiusOffset = radius / 2.5f
        centerX = (measuredWidth / 2).toFloat()
        centerY = (measuredHeight / 2).toFloat()
        stokeWidth = dip2px(2f).toFloat()
        //        stokeWidth *= getMeasuredWidth() * 1f / XPopupUtils.dp2px(getContext(), 40);
        paint.strokeWidth = stokeWidth
        startX = centerX + radiusOffset
        endX = startX + radius / 3f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 1 2 3 4 5
        // 2 3 4 5 1
        // 3 4 5 1 2
        // ...
        for (i in lineCount - 1 downTo 0) {
            val temp = Math.abs(i + time) % lineCount
            val fraction = (temp + 1) * 1f / lineCount
            val color = argbEvaluator.evaluate(fraction, startColor, endColor) as Int
            paint.color = color
            canvas.drawLine(startX, centerY, endX, centerY, paint)
            // 线的两端画个点，看着圆滑
            canvas.drawCircle(startX, centerY, stokeWidth / 2, paint)
            canvas.drawCircle(endX, centerY, stokeWidth / 2, paint)
            canvas.rotate(avgAngle, centerX, centerY)
        }
    }

    fun start() {
        removeCallbacks(increaseTask)
        postDelayed(increaseTask, 80)
    }

    private val increaseTask: Runnable = object : Runnable {
        override fun run() {
            time++
            postInvalidate(0, 0, measuredWidth, measuredHeight)
            postDelayed(this, 80)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(increaseTask)
    }
}