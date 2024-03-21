package com.hzsoft.module.me.adapter

import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hzsoft.lib.base.adapter.BaseSkeletonAdapter
import com.hzsoft.lib.net.local.entity.UserTestRoom
import com.hzsoft.module.me.R

/**
 *
 *
 * @author zhouhuan
 * @time 2021/11/23
 */
class RoomTestAdapter :
    BaseSkeletonAdapter<UserTestRoom, BaseViewHolder>(R.layout.item_user) {
    override fun convert(holder: BaseViewHolder, item: UserTestRoom) {
        holder.setText(R.id.tv_name, "姓名：%s%s(%s)".format(item.firstName, item.lastName, item.id))
            .setText(R.id.tv_caption, "年龄：%s".format(item.age))
        Glide.with(context)
            .load(item.image)
            .placeholder(R.drawable.loading_anim)
            .error(R.drawable.loading_bg)
            .into(holder.getView(R.id.iv_recipe_item_image))
    }

    class RoomTestDiffCalculator(
        private val oldItems: List<UserTestRoom>,
        private val newItems: List<UserTestRoom>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]
            return oldItem.id == newItem.id
                    && oldItem.age == newItem.age
                    && oldItem.image == newItem.image
                    && oldItem.firstName == newItem.firstName
                    && oldItem.lastName == newItem.lastName
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }

    }
}
