package com.hzsoft.lib.base.adapter

import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder

open class BaseViewBindingHolder<VB : ViewBinding>(val binding: VB) : BaseViewHolder(binding.root)