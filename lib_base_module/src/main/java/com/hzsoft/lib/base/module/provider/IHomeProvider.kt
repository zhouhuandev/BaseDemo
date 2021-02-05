package com.hzsoft.lib.base.module.provider

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * Describe:
 * 首页
 *
 * @author zhouhuan
 * @Date 2020/12/3
 */
interface IHomeProvider : IProvider {
    val mainHomeFragment: Fragment
}