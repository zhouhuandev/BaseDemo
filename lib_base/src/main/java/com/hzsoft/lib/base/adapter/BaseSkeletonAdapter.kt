package com.hzsoft.lib.base.adapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.hzsoft.lib.base.R

/**
 * 骨架屏
 * @author zhouhuan
 * @time 2021/7/10
 */
abstract class BaseSkeletonAdapter<E, VH : BaseViewHolder>(
    @LayoutRes private val layoutResId: Int,
    data: MutableList<E>? = null
) :
    BaseQuickAdapter<E, VH>(layoutResId, data) {
    private var skeletonScreen: SkeletonScreen? = null

    /**
     * 骨架屏
     * @param recyclerView view
     * @param layoutId 骨架屏ui
     * @param itemCount 显示item数量
     */
    open fun bindSkeletonScreen(
        recyclerView: RecyclerView,
        @LayoutRes layoutId: Int, itemCount: Int
    ) {
        onAttachedToRecyclerView(recyclerView)
        skeletonScreen = Skeleton.bind(recyclerView) //设置实际adapter
            .adapter(this) //是否开启动画
            .shimmer(true) //shimmer的倾斜角度 闪过去得阴影
            .angle(30) //shimmer的颜色
            .color(R.color.color_88FFFFFF) //true则表示显示骨架屏时，RecyclerView不可滑动，否则可以滑动
            .frozen(true) //动画时间，以毫秒为单位
            .duration(800) //显示骨架屏时item的个数 默认十个
            .count(itemCount) //骨架屏UI
            .load(layoutId)
            .show()
    }

    override fun setNewInstance(list: MutableList<E>?) {
        super.setNewInstance(list)
        hideSkeleton()
    }

    override fun setList(list: Collection<E>?) {
        super.setList(list)
        hideSkeleton()
    }

    @Deprecated(
        "Please use setNewInstance(), This method will be removed in the next version",
        replaceWith = ReplaceWith("setNewInstance(data)")
    )
    override fun setNewData(data: MutableList<E>?) {
        setNewInstance(data)
    }

    @Deprecated("Please use setData()", replaceWith = ReplaceWith("setList(newData)"))
    override fun replaceData(newData: Collection<E>) {
        setList(newData)
    }

    open fun hideSkeleton() {
        skeletonScreen?.hide()
    }
}