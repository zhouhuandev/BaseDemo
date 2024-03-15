package com.hzsoft.basedemo.adapter

import android.view.View
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hzsoft.basedemo.R
import com.hzsoft.lib.base.adapter.BaseSkeletonAdapter
import com.hzsoft.lib.base.utils.ext.view.showToast
import com.hzsoft.lib.domain.entity.Demo

/**
 *
 *
 * @author <a href="mailto:zhouhuandev@gmail.com" rel="nofollow">zhouhuan</a>
 * @since 2024/3/15 14:19
 */
class MoreRequestDemoAdapter : BaseSkeletonAdapter<Demo, BaseViewHolder>(R.layout.item_user) {
    override fun convert(holder: BaseViewHolder, item: Demo) {
        holder.setText(R.id.tv_name, item.name)
            .setText(R.id.tv_caption, item.description)
        Glide.with(context)
            .load(item.thumb)
            .placeholder(R.drawable.loading_anim)
            .error(R.drawable.loading_bg)
            .into(holder.getView(R.id.iv_recipe_item_image))

        holder.getView<View>(R.id.rl_recipe_item).setOnClickListener {
            "您点击了${holder.absoluteAdapterPosition}".showToast()
        }
    }
}