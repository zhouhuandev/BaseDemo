package com.hzsoft.basedemo.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.hzsoft.basedemo.R
import com.hzsoft.basedemo.databinding.ItemRecipeBinding
import com.hzsoft.lib.base.adapter.BaseSkeletonAdapter
import com.hzsoft.lib.base.utils.ext.view.showToast
import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.utils.ext.view.showToast
import com.hzsoft.module.home.R
import com.hzsoft.module.home.databinding.ItemRecipeBinding

/**
 * Describe:
 *
 *
 * @author zhouhuan
 * @Date 2020/12/17
 */
class MainHomeAdapter :
    BaseSkeletonAdapter<Demo, BaseDataBindingHolder<ItemRecipeBinding>>(R.layout.item_recipe) {
    override fun convert(holder: BaseDataBindingHolder<ItemRecipeBinding>, item: Demo) {
        holder.dataBinding?.apply {
            tvCaption.text = item.description
            tvName.text = item.name
            ivRecipeItemImage.apply {
                Glide.with(context)
                    .load(item.thumb)
                    .placeholder(R.drawable.ic_healthy_food)
                    .error(R.drawable.ic_healthy_food)
                    .into(this)
            }

            rlRecipeItem.setOnClickListener {
                "您点击了${this@MainHomeAdapter.getItemPosition(item)}".showToast()
            }
        }
    }
}
