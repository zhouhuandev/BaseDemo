package com.hzsoft.lib.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * Describe:
 * BaseAdapter
 *
 * @author zhouhuan
 * @Data 2020/11/19
 */
abstract class BaseAdapter<E, VH : BaseViewHolder>(protected var mContext: Context) :
    RecyclerView.Adapter<VH>() {
    protected var mList: MutableList<E>
    protected var mOnItemClickListener: OnItemClickListener? = null
    protected var mOnItemLongClickListener: OnItemLongClickListener? = null
    protected var mOnItemChildClickListener: OnItemChildClickListener? = null
    protected var mOnItemChildLongClickListener: OnItemChildLongClickListener? = null

    val listData: List<E>
        get() = mList

    init {
        mList = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(mContext).inflate(onBindLayout(viewType), parent, false)
        val onCreateHolder = onCreateHolder(view)
        onCreateHolder.adapter = this
        return onCreateHolder
    }

    /**
     * 绑定布局文件
     */
    protected abstract fun onBindLayout(viewType: Int): Int

    /**
     * 创建一个holder
     */
    protected fun onCreateHolder(view: View): VH = BaseViewHolder(itemView = view) as VH

    /**
     * 绑定数据
     */
    protected abstract fun onBindItem(holder: VH, item: E, positon: Int)

    override fun onBindViewHolder(holder: VH, position: Int) {
        val e = mList[position]
        mOnItemClickListener?.apply {
            holder.itemView.setOnClickListener {
                onItemClick(
                    this@BaseAdapter,
                    it,
                    position
                )
            }
        }
        mOnItemLongClickListener?.apply {
            holder.itemView.setOnLongClickListener {
                onItemLongClick(
                    this@BaseAdapter,
                    it,
                    position
                )
            }
        }
        onBindItem(holder, e, position)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setNewList(list: List<E>) {
        refresh(list)
    }

    fun addAll(list: List<E>) {
        if (list.isNotEmpty()) {
            mList.addAll(list)
            notifyDataSetChanged()
        }
    }

    fun refresh(list: List<E>) {
        mList.clear()
        if (list.isNotEmpty()) {
            mList.addAll(list)
        }
        notifyDataSetChanged()
    }

    fun remove(positon: Int) {
        mList.removeAt(positon)
        notifyDataSetChanged()
    }

    fun remove(e: E) {
        mList.remove(e)
        notifyDataSetChanged()
    }

    fun add(e: E) {
        mList.add(e)
        notifyDataSetChanged()
    }

    fun addLast(e: E) {
        add(e)
    }

    fun addFirst(e: E) {
        mList.add(0, e)
        notifyDataSetChanged()
    }

    fun clear() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        mOnItemClickListener = itemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener
    }

    fun setOnItemChildClickListener(onItemChildClickListener: OnItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener
    }

    fun setOnItemChildLongClickListener(onItemChildLongClickListener: OnItemChildLongClickListener) {
        mOnItemChildLongClickListener = onItemChildLongClickListener
    }


    fun getOnItemClickListener(): OnItemClickListener? {
        return mOnItemClickListener
    }

    fun getOnItemLongClickListener(): OnItemLongClickListener? {
        return mOnItemLongClickListener
    }

    fun getOnItemChildClickListener(): OnItemChildClickListener? {
        return mOnItemChildClickListener
    }

    fun getOnItemChildLongClickListener(): OnItemChildLongClickListener? {
        return mOnItemChildLongClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(adapter: BaseAdapter<*, *>?, view: View, position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(adapter: BaseAdapter<*, *>?, view: View, position: Int): Boolean
    }

    interface OnItemChildClickListener {
        fun onItemChildClick(adapter: BaseAdapter<*, *>?, view: View, position: Int)
    }

    interface OnItemChildLongClickListener {
        fun onItemChildLongClick(adapter: BaseAdapter<*, *>?, view: View, position: Int): Boolean
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
}
