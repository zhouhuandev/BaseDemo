package com.hzsoft.basedemo.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.hzsoft.basedemo.R
import com.hzsoft.basedemo.databinding.ItemRecipeBinding
import com.hzsoft.lib.base.adapter.BaseDataBindAdapter
import com.hzsoft.lib.base.adapter.BaseViewHolder
import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.utils.ext.view.showToast

/**
 * Describe:
 *
 *
 * @author zhouhuan
 * @Date 2020/12/17
 */
class MainHomeAdapter(private val mContext: Context) :
    BaseDataBindAdapter<Demo, ItemRecipeBinding, BaseViewHolder>(mContext) {

    override fun onBindItem(binding: ItemRecipeBinding?, item: Demo, position: Int) {
        binding?.let {
            binding.tvCaption.text = item.description
            binding.tvName.text = item.name
            Glide.with(mContext)
                .load(item.thumb)
                .placeholder(R.drawable.ic_healthy_food)
                .error(R.drawable.ic_healthy_food)
                .into(binding.ivRecipeItemImage)
            binding.rlRecipeItem.setOnClickListener { "您点击了$position".showToast(mContext) }
        }
    }

    override fun onBindLayout(viewType: Int): Int = R.layout.item_recipe

}
