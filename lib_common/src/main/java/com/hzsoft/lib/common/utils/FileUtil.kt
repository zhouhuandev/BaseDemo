package com.hzsoft.lib.common.utils

import android.text.TextUtils
import java.util.regex.Pattern

/**
 * 文件操作工具类
 * @author zhouhuan
 * @time 2020/11/30 23:12
 */
object FileUtil {
    fun isImageFile(url: String): Boolean {
        if (TextUtils.isEmpty(url)) {
            return false
        }
        val reg = ".+(\\.jpeg|\\.jpg|\\.gif|\\.bmp|\\.png).*"
        val pattern = Pattern.compile(reg)
        val matcher = pattern.matcher(url.toLowerCase())
        return matcher.find()
    }

    fun isVideoFile(url: String): Boolean {
        if (TextUtils.isEmpty(url)) {
            return false
        }
        val reg = ".+(\\.avi|\\.wmv|\\.mpeg|\\.mp4|\\.mov|\\.mkv|\\.flv|\\.f4v|\\.m4v|\\.rmvb|\\.rm|\\.rmvb|\\.3gp|\\.dat|\\.ts|\\.mts|\\.vob).*"
        val pattern = Pattern.compile(reg)
        val matcher = pattern.matcher(url.toLowerCase())
        return matcher.find()
    }

    fun isUrl(url: String): Boolean {
        if (TextUtils.isEmpty(url)) {
            return false
        }
        val reg = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]"
        return url.matches(reg.toRegex())
    }
}
