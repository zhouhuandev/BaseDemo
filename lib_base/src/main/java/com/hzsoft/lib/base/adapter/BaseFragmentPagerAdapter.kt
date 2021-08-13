package com.hzsoft.lib.base.adapter

import android.util.SparseArray
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hzsoft.lib.base.view.BaseFragment

/**
 * FragmentPagerAdapter
 * @author zhouhuan
 * @time 2021/7/4 20:32
 */
class BaseFragmentPagerAdapter<T : BaseFragment>(
    private val mFragmentManager: FragmentManager,
    private val mFragmentList: MutableList<T>
) : FragmentPagerAdapter(
    mFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    /**
     * 下面两个值用来保存Fragment的位置信息，用以判断该位置是否需要更新
     */
    private val mFragmentPositionMap: SparseArray<String> = SparseArray()
    private val mFragmentPositionMapAfterUpdate: SparseArray<String> = SparseArray()

    /**
     * 保存更新之前的位置信息，用<hashCode></hashCode>, position>的键值对结构来保存
     */
    private fun setFragmentPositionMap() {
        mFragmentPositionMap.clear()
        for (i in mFragmentList.indices) {
            mFragmentPositionMap.put(getItemId(i).toInt(), i.toString())
        }
    }

    /**
     * 保存更新之后的位置信息，用<hashCode></hashCode>, position>的键值对结构来保存
     */
    private fun setFragmentPositionMapForUpdate() {
        mFragmentPositionMapAfterUpdate.clear()
        for (i in mFragmentList.indices) {
            mFragmentPositionMapAfterUpdate.put(getItemId(i).toInt(), i.toString())
        }
    }

    /**
     * 在此方法中找到需要更新的位置返回POSITION_NONE，否则返回POSITION_UNCHANGED即可
     */
    override fun getItemPosition(`object`: Any): Int {
        val hashCode = `object`.hashCode()
        //查找object在更新后的列表中的位置
        val position = mFragmentPositionMapAfterUpdate[hashCode]
        //更新后的列表中不存在该object的位置了
        if (position == null) {
            return POSITION_NONE
        } else {
            //如果更新后的列表中存在该object的位置, 查找该object之前的位置并判断位置是否发生了变化
            val size = mFragmentPositionMap.size()
            for (i in 0 until size) {
                val key = mFragmentPositionMap.keyAt(i)
                if (key == hashCode) {
                    val index = mFragmentPositionMap[key]
                    return if (position == index) {
                        //位置没变依然返回POSITION_UNCHANGED
                        POSITION_UNCHANGED
                    } else {
                        //位置变了
                        POSITION_NONE
                    }
                }
            }
        }
        return POSITION_UNCHANGED
    }

    /**
     * 将指定位置的Fragment替换/更新为新的Fragment
     *
     * @param position    旧Fragment的位置
     * @param newFragment 新Fragment
     */
    fun replaceFragment(position: Int, newFragment: T) {
        removeFragmentInternal(position)
        mFragmentList[position] = newFragment
        notifyItemChanged()
    }

    /**
     * 移除指定位置的Fragment
     *
     * @param position 索引
     */
    fun removeFragment(position: Int) {
        //然后从List中移除
        mFragmentList.removeAt(position)
        //先从Transaction移除
        removeFragmentInternal(position)
        //最后刷新Adapter
        notifyItemChanged()
    }

    /**
     * 添加Fragment
     *
     * @param fragment 目标Fragment
     */
    fun addFragment(fragment: T) {
        mFragmentList.add(fragment)
        notifyItemChanged()
    }

    /**
     * 在指定位置插入一个Fragment
     *
     * @param position 插入位置
     * @param fragment 目标Fragment
     */
    fun insertFragment(position: Int, fragment: T) {
        mFragmentList.add(position, fragment)
        notifyItemChanged()
    }

    private fun notifyItemChanged() {
        //刷新之前重新收集位置信息
        setFragmentPositionMapForUpdate()
        notifyDataSetChanged()
        setFragmentPositionMap()
    }

    /**
     * 从Transaction移除Fragment
     *
     * @param position 目标Fragment position
     */
    private fun removeFragmentInternal(position: Int) {
        val transaction = mFragmentManager.beginTransaction()
        transaction.remove(mFragmentList[position])
        transaction.commitNow()
    }

    /**
     * 此方法不用position做返回值即可破解fragment tag异常的错误
     */
    override fun getItemId(position: Int): Long {
        // 获取当前数据的hashCode，其实这里不用hashCode用自定义的可以关联当前Item对象的唯一值也可以，只要不是直接返回position
        return mFragmentList[position].hashCode().toLong()
    }

    override fun getItem(position: Int): BaseFragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    val fragments: List<T>
        get() = mFragmentList

    init {
        setFragmentPositionMap()
        setFragmentPositionMapForUpdate()
    }
}