package com.hzsoft.lib.common.utils.ext.view

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.hzsoft.lib.common.R

/**
 * Describe:
 * <p></p>
 *
 * @author zhouhuan
 * @Date 2020/12/7
 */

/**
 * 加载图片 By 资源 Id
 */
fun ImageView.loadImage(@DrawableRes resId: Int) =
    Glide.with(this)
        .load(resId)
        .placeholder(R.drawable.loading_anim)
        .error(R.drawable.empty_pic_net).into(this)

/**
 * 加载图片 By Url
 */
fun ImageView.loadImage(url: String) =
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.loading_anim)
        .error(R.drawable.empty_pic_net)
        .into(this)

/**
 * 加载动图 By Url
 */
fun ImageView.loadAsGifImage(url: String) =
    Glide.with(this)
        .asGif()
        .load(url)
        .placeholder(R.drawable.loading_anim)
        .error(R.drawable.empty_pic_net)
        .into(this)