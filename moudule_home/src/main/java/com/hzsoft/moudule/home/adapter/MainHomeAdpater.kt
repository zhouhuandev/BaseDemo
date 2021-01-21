package com.hzsoft.moudule.home.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.hzsoft.lib.base.adapter.BaseDataBindAdapter
import com.hzsoft.lib.base.adapter.BaseViewHolder
import com.hzsoft.lib.net.utils.ext.view.showToast
import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.moudule.home.R
import com.hzsoft.moudule.home.databinding.ItemRecipeBinding

/**
 * Describe:
 *
 *
 * @author zhouhuan
 * @Date 2020/12/17
 */
class MainHomeAdpater(mContext: Context) :
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
