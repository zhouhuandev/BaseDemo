package com.hzsoft.lib.base.module.provider

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * Describe:
 * 个人中心
 *
 * @author zhouhuan
 * @Date 2020/12/3
 */
interface IMeProvider : IProvider {
    val mainMeFragment: Fragment
}