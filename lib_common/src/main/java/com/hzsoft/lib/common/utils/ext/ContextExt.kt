package com.hzsoft.lib.common.utils.ext

import android.app.Activity
import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import java.io.File

/**
 *
 *
 * @author <a href="mailto:zhouhuandev@gmail.com" rel="nofollow">zhouhuan</a>
 * @since 2022/8/20 14:57
 */

fun Context.loadImgFile(url: String): File? {
    if (isDestroy()) return null
    return try {
        val target = Glide.with(this)
            .asFile()
            .load(url)
            .submit()
        target.get()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Context?.isDestroy(): Boolean {
    if (this == null) return true
    if (this is Activity) {
        return this.isFinishing || this.isDestroyed
    } else if (this is FragmentActivity) {
        return this.isFinishing || this.isDestroyed
    }
    return false
}

fun Context.getCompatColor(@ColorRes color: Int) = ContextCompat.getColor(this, color)

fun Context.colorIntToHex(@ColorInt color: Int): String {
    val intColorRGB: Int = color and 0x00ffffff
    val hex = Integer.toHexString(intColorRGB)
    return "#$hex"
}