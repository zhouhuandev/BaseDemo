package com.hzsoft.lib.base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.hzsoft.lib.base.R

class NoDataView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    private val mRlNoDataRoot: RelativeLayout
    private val mImgNoDataView: ImageView

    init {
        View.inflate(context, R.layout.view_no_data, this)
        mRlNoDataRoot = findViewById(R.id.rl_no_data_root)
        mImgNoDataView = findViewById(R.id.img_no_data)
    }

    fun setNoDataBackground(@ColorRes colorResId: Int) {
        mRlNoDataRoot.setBackgroundColor(resources.getColor(colorResId))
    }

    fun setNoDataView(@DrawableRes imgResId: Int) {
        mImgNoDataView.setImageResource(imgResId)
    }
}
