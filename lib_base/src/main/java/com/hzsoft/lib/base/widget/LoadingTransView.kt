package com.hzsoft.lib.base.widget

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.hzsoft.lib.base.R

/**
 * Description: <LoadingView><br>
 * Author:      mxdl<br>
 * Date:        2019/3/25<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
</LoadingView> */
class LoadingTransView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    private val mAnimationDrawable: AnimationDrawable

    init {
        View.inflate(context, R.layout.view_trans_loading, this)
        val imgLoading = findViewById<ImageView>(R.id.img_trans_loading)
        mAnimationDrawable = imgLoading.drawable as AnimationDrawable
    }

    fun startLoading() {
        mAnimationDrawable.start()
    }

    fun stopLoading() {
        mAnimationDrawable.stop()
    }

    fun loading(b: Boolean) {
        if (b) {
            startLoading()
        } else {
            stopLoading()
        }
    }
}
