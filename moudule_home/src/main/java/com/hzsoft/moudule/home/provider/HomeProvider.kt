package com.hzsoft.moudule.home.provider

import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.hzsoft.lib.base.module.provider.IHomeProvider
import com.hzsoft.moudule.home.fragment.MainHomeFragment

/**
 * Describe:
 * 首页服务
 *
 * @author zhouhuan
 * @Date 2020/12/3
 */
@Route(path = "/home/main", name = "首页服务")
class HomeProvider : IHomeProvider {
    override val mainHomeFragment: Fragment
        get() = MainHomeFragment.newsInstance()

    override fun init(context: Context?) {

    }
}