package com.hzsoft.moudule.me.fragment

import com.hzsoft.lib.common.base.BaseFragment
import com.hzsoft.moudule.me.R

/**
 * Describe:
 * 首页
 *
 * @author zhouhuan
 * @Date 2020/12/3
 */
class MainMeFragment : BaseFragment() {

    companion object {
        fun newsInstance(): MainMeFragment {
            return MainMeFragment()
        }
    }

    override fun onBindLayout(): Int = R.layout.fragment_me_main

    override fun initData() {

    }

    override fun enableToolbar(): Boolean = true

    override fun getTootBarTitle(): String = resources.getString(R.string.module_me)
}