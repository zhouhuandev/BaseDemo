package com.hzsoft.lib.common.utils


import android.content.Context

/**
 * 动态获取资源id
 * @author zhouhuan
 * @time 2020/11/30 23:14
 */
object ResIdUtil {


    /**
     * 获取id
     *
     * @param resName 资源名称
     * @return 资源id
     */
    fun id(context: Context, resName: String): Int {
        return context.resources.getIdentifier(resName, "id", context.packageName)
    }

    /**
     * 获取anim类型资源id
     *
     * @param resName 资源名称
     * @return 资源id
     */
    fun anim(context: Context, resName: String): Int {
        return context.resources.getIdentifier(resName, "anim", context.packageName)
    }

    /**
     * 获取layout类型资源id
     *
     * @param resName 资源名称
     * @return 资源id
     */
    fun layout(context: Context, resName: String): Int {
        return context.resources.getIdentifier(resName, "layout", context.packageName)
    }

    /**
     * 获取drawable类型资源id
     *
     * @param resName 资源名称
     * @return 资源id
     */
    fun drawable(context: Context, resName: String): Int {
        return context.resources.getIdentifier(resName, "drawable", context.packageName)
    }

    /**
     * 获取string类型资源id
     *
     * @param resName 资源名称
     * @return 资源id
     */
    fun string(context: Context, resName: String): Int {
        return context.resources.getIdentifier(resName, "string", context.packageName)
    }

    /**
     * 获取raw类型资源id
     *
     * @param resName 资源名称
     * @return 资源id
     */
    fun raw(context: Context, resName: String): Int {
        return context.resources.getIdentifier(resName, "raw", context.packageName)
    }

    /**
     * 获取style类型资源id
     *
     * @param resName 资源名称
     * @return 资源id
     */
    fun style(context: Context, resName: String): Int {
        return context.resources.getIdentifier(resName, "style", context.packageName)
    }
}