package com.hzsoft.module.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hzsoft.lib.base.adapter.BaseSkeletonAdapter
import com.hzsoft.lib.base.adapter.BaseViewBindingHolder
import com.hzsoft.lib.base.utils.ext.view.showToast
import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.module.home.R
import com.hzsoft.module.home.databinding.ItemRecipeBinding

/**
 * Describe:
 *
 *
 * @author zhouhuan
 * @Date 2020/12/17
 */
class MainHomeAdapter : BaseSkeletonAdapter<Demo, BaseViewBindingHolder<ItemRecipeBinding>>(R.layout.item_recipe) {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewBindingHolder<ItemRecipeBinding> {
        return BaseViewBindingHolder(ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun convert(holder: BaseViewBindingHolder<ItemRecipeBinding>, item: Demo) {
        holder.binding.apply {
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
                "您点击了${holder.absoluteAdapterPosition}".showToast()
            }
        }
    }
}
