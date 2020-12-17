package com.hzsoft.lib.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Describe:
 * BaseDataBindAdapter
 *
 * @author zhouhuan
 * @Date 2020/12/17
 */
abstract class BaseDataBindAdapter<E, V : ViewDataBinding, VH : BaseViewHolder>(mContext: Context) :
    BaseAdapter<E, VH>(mContext) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding: V = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            onBindLayout(viewType),
            parent,
            false
        )
        val onCreateHolder = onCreateHolder(binding.root)
        onCreateHolder.adapter = this
        return onCreateHolder
    }

    override fun onBindItem(holder: VH, e: E, positon: Int) {
        val binding = DataBindingUtil.getBinding<V>(holder.itemView)
        onBindItem(binding, e, positon)
    }

    protected abstract fun onBindItem(binding: V?, item: E, position: Int)

}
