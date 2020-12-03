package com.hzsoft.moudule.me.provider

import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.hzsoft.lib.common.provider.IHomeProvider
import com.hzsoft.lib.common.provider.IMeProvider
import com.hzsoft.moudule.me.fragment.MainMeFragment

/**
 * Describe:
 * 个人中心服务
 *
 * @author zhouhuan
 * @Date 2020/12/3
 */
@Route(path = "/me/main", name = "个人中心服务")
class MeProvider : IMeProvider {
    override val mainMeFragment: Fragment
        get() = MainMeFragment.newsInstance()

    override fun init(context: Context?) {

    }

}