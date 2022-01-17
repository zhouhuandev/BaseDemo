package com.hzsoft.lib.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * RecyclerView空白区域点击监听
 *
 * @author zhouhuan
 * @time 2022/1/14
 */
class TouchyRecyclerView(context: Context, attrs: AttributeSet) : androidx.recyclerview.widget.RecyclerView(context, attrs) {
    private var listener: OnNoChildClickListener? = null

    interface OnNoChildClickListener {
        fun onNoChildClick()
    }

    fun setOnNoChildClickListener(listener: OnNoChildClickListener) {
        this.listener = listener
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && findChildViewUnder(event.x, event.y) == null) {
            if (listener != null) {
                listener!!.onNoChildClick()
            }
        }
        return super.dispatchTouchEvent(event)
    }
}
